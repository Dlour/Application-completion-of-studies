package com.example.tugas_rpl;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class KajurActivity extends AppCompatActivity {

    Button databtn,jurnalbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_kajur);

        databtn = findViewById(R.id.lihat_data);
        jurnalbtn = findViewById(R.id.list_jurnal);

        databtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),DataMahasiswaActivity.class);
                startActivity(i);
            }
        });

        jurnalbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), LihatJurnalActivity.class);
                startActivity(i);
            }
        });



    }
}