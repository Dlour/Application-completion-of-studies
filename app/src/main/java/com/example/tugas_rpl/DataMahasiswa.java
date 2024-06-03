package com.example.tugas_rpl;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class DataMahasiswa extends AppCompatActivity {

    EditText Prodi, Angkatan, Sks, Yudisium;
    Button signupButton;

    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseFirestore firestore;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_mahasiswa);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("users");
        firestore = FirebaseFirestore.getInstance();

        Prodi = findViewById(R.id.prodi);
        Angkatan = findViewById(R.id.angkatan);
        Sks = findViewById(R.id.sks);
        Yudisium = findViewById(R.id.yudisium);
        signupButton = findViewById(R.id.signup_button);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Terima data dari Intent
                Intent intent = getIntent();
                String name = intent.getStringExtra("name");
                String email = intent.getStringExtra("email");
                String nim = intent.getStringExtra("nim");
                String password = intent.getStringExtra("password");
                String mahasiswa = intent.getStringExtra("mahasiswa");

                String prodi = Prodi.getText().toString().trim();
                String angkatan = Angkatan.getText().toString().trim();
                int sks = Integer.parseInt(Sks.getText().toString().trim());
                boolean yudisium = Boolean.parseBoolean(Yudisium.getText().toString().trim());

                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    String userID = user.getUid();

                    mahasiswaModel helperClass = new mahasiswaModel(name, nim, email, password, prodi, angkatan, sks, yudisium, mahasiswa,0);

                    // Simpan data ke Realtime Database
                    reference.child(userID).setValue(helperClass)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(DataMahasiswa.this, "Akun berhasil disimpan di Realtime Database!", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(DataMahasiswa.this, "Gagal menyimpan akun di Realtime Database: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });

                    // Simpan data ke Firestore
                    firestore.collection("users").document(userID).set(helperClass)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(DataMahasiswa.this, "Akun berhasil disimpan di Firestore!", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(DataMahasiswa.this, "Gagal menyimpan akun di Firestore: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });

                    // Redirect ke halaman login setelah menyimpan data
                    Intent i = new Intent(DataMahasiswa.this, Login.class);
                    startActivity(i);
                } else {
                    Toast.makeText(DataMahasiswa.this, "Gagal mendapatkan pengguna saat ini.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
