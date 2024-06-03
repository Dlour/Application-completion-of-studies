package com.example.tugas_rpl;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class sign_up extends AppCompatActivity {

    EditText signupName, signupNim, signupEmail, signupPassword;
    Button nextButton;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signupName = findViewById(R.id.signup_name);
        signupEmail = findViewById(R.id.signup_email);
        signupNim = findViewById(R.id.signup_username);
        signupPassword = findViewById(R.id.signup_password);
        nextButton = findViewById(R.id.next);

        mAuth = FirebaseAuth.getInstance();

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = signupName.getText().toString().trim();
                final String email = signupEmail.getText().toString().trim();
                final String nim = signupNim.getText().toString().trim();
                final String password = signupPassword.getText().toString().trim();
                final String mahasiswa = "1";

                // Validasi input
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(nim) || TextUtils.isEmpty(password)) {
                    Toast.makeText(sign_up.this, "Harap isi semua field", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Buat akun pengguna baru
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(sign_up.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign up sukses, lanjutkan ke halaman DataMahasiswa
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Toast.makeText(sign_up.this, "Pendaftaran berhasil.", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(sign_up.this, DataMahasiswa.class);
                                    intent.putExtra("name", name);
                                    intent.putExtra("email", email);
                                    intent.putExtra("nim", nim);
                                    intent.putExtra("password", password);
                                    intent.putExtra("mahasiswa", mahasiswa);
                                    startActivity(intent);
                                } else {
                                    // Sign up gagal, tampilkan pesan kesalahan
                                    Toast.makeText(sign_up.this, "Pendaftaran gagal. " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}
