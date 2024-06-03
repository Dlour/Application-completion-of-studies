package com.example.tugas_rpl;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MahasiswaActivity extends AppCompatActivity {

    Button btn;

    Button ubahbtn;
    private Fragment mahasiswaLand;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Cek apakah EdgeToEdge diimplementasikan dengan benar
        try {
            EdgeToEdge.enable(this);
        } catch (Exception e) {
            Log.e(TAG, "EdgeToEdge implementation error: ", e);
        }

        setContentView(R.layout.activity_mahasiswa);
        btn = findViewById(R.id.btnupload);

        ubahbtn = findViewById(R.id.ubah_data);






        // Terima data dari UploadActivity
        Intent intent = getIntent();
        String namaUser = intent.getStringExtra("namaUser");
        String userNIM = intent.getStringExtra("userNIM");
        String fileName = intent.getStringExtra("fileName");
        String fileUrl = intent.getStringExtra("fileUrl");

        // Periksa apakah pengguna telah terautentikasi


        // Kirim data ke fragment
        mahasiswaLand = new mahasiswaLandFragment();
        loadFragment(mahasiswaLand);
    }

    private void loadFragment(Fragment fragment) {
        Log.d("MahasiswaActivity", "Loading fragment...");
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_mahasiswa_land, fragment);
        transaction.commit();
        Log.d("MahasiswaActivity", "Fragment loaded.");
    }
    boolean inUser() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

}
