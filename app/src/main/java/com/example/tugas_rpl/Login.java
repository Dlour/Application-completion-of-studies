package com.example.tugas_rpl;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class Login extends AppCompatActivity {

    EditText LoginNim, loginPassword;
    Button loginButton;
    TextView signupRedirectText;

    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginNim = findViewById(R.id.login_username);
        loginPassword = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
        signupRedirectText = findViewById(R.id.signupRedirectText);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("users");

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateUsername() | !validatePassword()) {
                    return;
                } else {
                    checkUser();
                }
            }
        });

        signupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, sign_up.class);
                startActivity(intent);
            }
        });
    }

    public Boolean validateUsername() {
        String val = LoginNim.getText().toString();
        if (val.isEmpty()) {
            LoginNim.setError("Nim cannot be empty");
            return false;
        } else {
            LoginNim.setError(null);
            return true;
        }
    }

    public Boolean validatePassword() {
        String val = loginPassword.getText().toString();
        if (val.isEmpty()) {
            loginPassword.setError("Password cannot be empty");
            return false;
        } else {
            loginPassword.setError(null);
            return true;
        }
    }

    public void checkUser() {
        String userNim = LoginNim.getText().toString().trim();
        String userPassword = loginPassword.getText().toString().trim();
        Query checkUserDatabase = reference.orderByChild("nim").equalTo(userNim);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        String passwordFromDB = userSnapshot.child("password").getValue(String.class);
                        String passwordFromDBTrimmed = passwordFromDB != null ? passwordFromDB.trim() : null;
                        String userPasswordTrimmed = userPassword.trim();

                        if (passwordFromDBTrimmed != null && Objects.equals(passwordFromDBTrimmed, userPasswordTrimmed)) {
                            String nameFromDB = userSnapshot.child("name").getValue(String.class);
                            String emailFromDB = userSnapshot.child("email").getValue(String.class);
                            String nimFromDB = userSnapshot.child("nim").getValue(String.class);
                            String userLevel = userSnapshot.child("mahasiswa").getValue(String.class);
                            String userLevel2 = userSnapshot.child("kajur").getValue(String.class);
                            String userLevel3 = userSnapshot.child("admin").getValue(String.class);


                            if ("1".equals(userLevel)) {
                                Intent intent = new Intent(Login.this, MahasiswaActivity.class);
                                intent.putExtra("name", nameFromDB);
                                intent.putExtra("email", emailFromDB);
                                intent.putExtra("nim", nimFromDB);
                                startActivity(intent);
                            } else if ("1".equals(userLevel2)) {
                                Intent intent = new Intent(Login.this, KajurActivity.class);
                                intent.putExtra("name", nameFromDB);
                                intent.putExtra("email", emailFromDB);
                                intent.putExtra("nim", nimFromDB);
                                startActivity(intent);
                            } else if("1".equals(userLevel3)) {
                                Intent intent = new Intent(Login.this, MainActivity.class);
                                Toast.makeText(Login.this, "Selamat Datang Admin", Toast.LENGTH_SHORT).show();
                                startActivity(intent);
                            }else{
                                Toast.makeText(Login.this,"Error",Toast.LENGTH_SHORT).show();
                            }
                            finish();
                            return;
                        } else {
                            loginPassword.setError("Invalid Credentials");
                            loginPassword.requestFocus();
                        }
                    }
                } else {
                    LoginNim.setError("User does not exist");
                    LoginNim.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Database error: " + error.getMessage());
            }
        });
    }
}
