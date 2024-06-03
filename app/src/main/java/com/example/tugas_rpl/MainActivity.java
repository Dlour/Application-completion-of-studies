package com.example.tugas_rpl;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    Button dataMhs, dataKaprodi, tugas;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataMhs = findViewById(R.id.btndata); // Inisialisasi button
        dataKaprodi = findViewById(R.id.btndataKaprodi); // Inisialisasi button
        tugas = findViewById(R.id.btntugas); // Inisialisasi button

        dataMhs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), datamhsActivity.class));
            }
        });

        tugas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LihatJurnalActivity.class));
            }
        });
    }
}
