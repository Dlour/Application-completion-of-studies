package com.example.tugas_rpl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class ProfileFragment extends Fragment {
    ImageView profile;

    EditText nama, nim, prodi, angkatan, sks, ipk, yudisium;

    TextView logout;

    ProgressBar progressBar;

    Button upbtn;

    ActivityResultLauncher<Intent> imagePick;
    mahasiswaModel currentMahasiswa;
    StorageReference storageReference;
    private DatabaseReference database;

    mahasiswaAdapter mahasiswaAdapter;

    private FirebaseFirestore fstore;
    private ArrayList<mahasiswaModel> list;
    Uri selectedImageUri;
    String currentUserId;



    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        imagePick = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if (data != null && data.getData() != null) {
                    selectedImageUri = data.getData();
                    setProfilePic(getContext(), selectedImageUri, profile);
                }
            }
        });

        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        currentUserId = FirebaseAuth.getInstance().getUid(); // Mendapatkan user ID saat ini
        fstore = FirebaseFirestore.getInstance();
    }

    public static void setProfilePic(Context context, Uri imageUri, ImageView imageView) {
        Glide.with(context).load(imageUri).apply(RequestOptions.circleCropTransform()).into(imageView);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        profile = view.findViewById(R.id.foto_profile);
        nama = view.findViewById(R.id.nama_mahasiswa);
        nim = view.findViewById(R.id.nim_mahasiswa);
        prodi = view.findViewById(R.id.prodi_mahasiswa);
        angkatan = view.findViewById(R.id.angkatan_mahasiswa);
        sks = view.findViewById(R.id.sks_mahasiswa);
        yudisium = view.findViewById(R.id.yudisium_mahasiswa);
        ipk = view.findViewById(R.id.ipk_mahasiswa);
        logout = view.findViewById(R.id.logoutbtn);
        progressBar = view.findViewById(R.id.progress);
        upbtn = view.findViewById(R.id.updateProfile);


        list = new ArrayList<>();
        mahasiswaAdapter = new mahasiswaAdapter(getContext(), list, mahasiswaModel -> {
            // Handle the profile button click
        });


        logout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent i = new Intent(getActivity(), Login.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        });

        profile.setOnClickListener(v -> {
            ImagePicker.with(this).compress(512).maxResultSize(512, 512)
                    .createIntent(intent -> {
                        imagePick.launch(intent);
                        return null;
                    });
        });

        upbtn.setOnClickListener(v -> setUpdateProfile());

        getUserData();

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            database = FirebaseDatabase.getInstance().getReference("users");

            database.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (snapshot.hasChild("nama")
                                && snapshot.hasChild("nim")
                                && snapshot.hasChild("prodi")
                                && snapshot.hasChild("angkatan")
                                && snapshot.hasChild("sks")
                                && snapshot.hasChild("mahasiswa")
                                && snapshot.hasChild("foto_profil")
                                && snapshot.hasChild("yudisium")
                                && snapshot.hasChild("ipk")) {

                            // Ambil nilai dari dataSnapshot
                            String nama = snapshot.child("nama").getValue(String.class);
                            String nim = snapshot.child("nim").getValue(String.class);
                            String mahasiswa = snapshot.child("mahasiswa").getValue(String.class);
                            String prodi = snapshot.child("prodi").getValue(String.class);
                            String angkatan = snapshot.child("angkatan").getValue(String.class);
                            int sks = snapshot.child("sks").getValue(Integer.class);
                            int ipk = snapshot.child("ipk").getValue(Integer.class);
                            boolean yudisium = snapshot.child("yudisium").getValue(Boolean.class);

                            // Buat objek mahasiswaModel
                            mahasiswaModel MahasiswaModel = new mahasiswaModel(nama, nim, "", "", prodi, angkatan, sks, yudisium, mahasiswa, ipk);

                            // Tambahkan objek ke dalam list
                            list.add(MahasiswaModel);
                        } else {
                            Log.d("DataEmpty", "Data not complete for: " + snapshot.getKey());
                        }
                    }
                    // Pemberitahuan adapter bahwa data telah berubah
                    mahasiswaAdapter.notifyDataSetChanged(); // This line will now work correctly
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Penanganan kesalahan jika pembacaan data gagal
                    Log.e("TAG", "Failed to read data from Firebase.", databaseError.toException());
                }
            });
        } else {
            Log.e("TAG", "User not authenticated.");
        }

        return view;
    }

    private void setUpdateProfile() {
        String namaBaru = nama.getText().toString();
        if (namaBaru.isEmpty() || namaBaru.length() < 3) {
            nama.setError("Harus Lebih dari 3 karakter");
            return;
        }
        if (currentMahasiswa != null) {
            currentMahasiswa.setNama(namaBaru);
            setInProgress(true);
            if (selectedImageUri != null) {
                getCurrentProfilePicStorageRef().putFile(selectedImageUri).addOnCompleteListener(task -> updateToFirestore());
            } else {
                updateToFirestore();
            }
        } else {
            Toast.makeText(getContext(), "Data mahasiswa tidak tersedia untuk diperbarui", Toast.LENGTH_SHORT).show();
        }
    }


    private void updateToFirestore() {
        currentUserDetails().set(currentMahasiswa)
                .addOnCompleteListener(task -> {
                    setInProgress(false);
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Update Berhasil", Toast.LENGTH_SHORT).show();
                        checkLevel(currentUserId);
                    } else {
                        Toast.makeText(getContext(), "Update Gagal", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkLevel(String uid) {
        DocumentReference df = fstore.collection("users").document(uid);
        df.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.getString("mahasiswa") != null) {
                startActivity(new Intent(getContext(), MahasiswaActivity.class));
            }
            if (documentSnapshot.getString("kajur") != null) {
                startActivity(new Intent(getContext(), KajurActivity.class));
            }
        });
    }

    private DocumentReference currentUserDetails() {
        return fstore.collection("users").document(FirebaseAuth.getInstance().getUid());
    }

    private StorageReference getCurrentProfilePicStorageRef() {
        return storageReference.child("profileImages").child(FirebaseAuth.getInstance().getUid() + ".jpeg");
    }

    private void setInProgress(boolean inProgress) {
        if (inProgress) {
            progressBar.setVisibility(View.VISIBLE);
            upbtn.setEnabled(false);
        } else {
            progressBar.setVisibility(View.GONE);
            upbtn.setEnabled(true);
        }
    }

    private void getUserData() {
        setInProgress(true);
        currentUserDetails().get().addOnCompleteListener(task -> {
            setInProgress(false);
            if (task.isSuccessful()) {
                DocumentSnapshot doc = task.getResult();
                currentMahasiswa = doc.toObject(mahasiswaModel.class);
                if (currentMahasiswa != null) {
                    nama.setText(currentMahasiswa.getNama());
                    nim.setText(currentMahasiswa.getNim());
                    prodi.setText(currentMahasiswa.getProdi());
                    angkatan.setText(currentMahasiswa.getAngkatan());
                    sks.setText(String.valueOf(currentMahasiswa.getSks()));
                    ipk.setText(String.valueOf(currentMahasiswa.getIpk()));
                    yudisium.setText(String.valueOf(currentMahasiswa.isYudisium() ?"Sudah Acc" :"Belum Acc"));

                }
            } else {
                Toast.makeText(getContext(), "Failed to fetch user data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
