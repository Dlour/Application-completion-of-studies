package com.example.tugas_rpl;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class UploadActivity extends AppCompatActivity {

    Button uploadBtn;
    EditText pdf_name, judul;

    StorageReference storageReference;
    TextView namaMhs, nimMhs;
    FirebaseFirestore db;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_upload);

        uploadBtn = findViewById(R.id.uploadbtn);
        pdf_name = findViewById(R.id.filepdf);
        judul = findViewById(R.id.filepdf); // Sesuaikan ini jika id berbeda
        namaMhs = findViewById(R.id.namaMhs1);
        nimMhs = findViewById(R.id.nimMhs1);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "Pengguna tidak login", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        db = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        db.collection("users").document(user.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String userNIM = documentSnapshot.getString("nim");
                            String namaUser = documentSnapshot.getString("nama");

                            namaMhs.setText(namaUser);
                            nimMhs.setText(userNIM);
                        }
                    }
                });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFiles();
            }
        });
    }

    private void selectFiles() {
        Intent i = new Intent();
        i.setType("application/pdf");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select PDF Files..."), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            UploadFiles(data.getData());
        }
    }

    // UploadActivity.java

    private void UploadFiles(Uri data) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "Pengguna tidak login", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            return;
        }

        db.collection("users").document(user.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String userNIM = documentSnapshot.getString("nim");
                            String namaUser = documentSnapshot.getString("nama");


                            // Update teks pada TextView namaMhs dan nimMhs
                            namaMhs.setText(namaUser);
                            nimMhs.setText(userNIM);

                            // Menyusun nama file dengan NIM pengguna
                             String fileName = pdf_name.getText().toString().trim();
                            if (fileName.isEmpty()) {
                                Toast.makeText(UploadActivity.this, "Masukkan nama file", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                return;
                            }

                            fileName = namaUser + "_" + userNIM + "_" + fileName + ".pdf";

                            StorageReference reference = storageReference.child("Uploads/" + fileName);
                            String finalFileName = fileName;
                            reference.putFile(data)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                                            while (!uriTask.isComplete()) ;
                                            Uri url = uriTask.getResult();

                                            pdfClass pdfClass = new pdfClass(pdf_name.getText().toString(), url.toString());

                                            saveFileMetadataToFirestore(finalFileName,url.toString(),userNIM,namaUser);

                                            // Kirim data ke MahasiswaActivity
                                            Intent intent = new Intent(UploadActivity.this, MahasiswaActivity.class);
                                            intent.putExtra("namaUser", namaUser);
                                            intent.putExtra("userNIM", userNIM);
                                            intent.putExtra("fileName", pdf_name.getText().toString());
                                            intent.putExtra("fileUrl", url.toString());
                                            startActivity(intent);
                                            finish();

                                            progressDialog.dismiss();
                                        }
                                    })
                                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                            progressDialog.setMessage("Uploaded: " + (int) progress + "%");
                                        }
                                    });
                        } else {
                            Toast.makeText(UploadActivity.this, "Dokumen pengguna tidak ditemukan", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });
    }

    // Menyimpan metadata file ke Firestore
    private void saveFileMetadataToFirestore(String fileName, String fileUrl, String userNIM, String namaUser) {
        // Akses instance Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth mauth = FirebaseAuth.getInstance();

        String userID = mauth.getCurrentUser().getUid();

        // Membuat objek metadata file
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("fileName", fileName);
        metadata.put("fileUrl", fileUrl);
        metadata.put("userNIM", userNIM);
        metadata.put("namaUser", namaUser);
        metadata.put("user_id", userID);

        // Menambahkan metadata ke Firestore
        db.collection("tugasmhs")
                .add(metadata)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        // Metadata file berhasil disimpan ke Firestore
                        Log.d("UploadActivity", "Metadata file berhasil disimpan ke Firestore");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Gagal menyimpan metadata file ke Firestore
                        Log.e("UploadActivity", "Error: " + e.getMessage());
                    }
                });
    }


}
