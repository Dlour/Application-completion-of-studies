package com.example.tugas_rpl;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;

public class DataMahasiswaActivity extends AppCompatActivity {

    private Fragment mahasiswaFrag;

    Button ubahbtn;
    ImageView imageView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_data_mahasiswa2);

        imageView = findViewById(R.id.backdata);

        mahasiswaFrag = new MahasiswaFragment();
        loadFragment(mahasiswaFrag);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), KajurActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        Log.d(TAG, "Loading Fragment...");
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_mahasiswa2, fragment);
        transaction.commit();
        Log.d(TAG, "Fragment loaded.");
    }


}
