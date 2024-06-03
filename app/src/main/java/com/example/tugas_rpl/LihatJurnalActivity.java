package com.example.tugas_rpl;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class LihatJurnalActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private tugasAkhirAdapter adapter;
    private ArrayList<tugasAkhirModel> tugasAkhirList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lihat_jurnal);

        recyclerView = findViewById(R.id.rec_jurnal);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        tugasAkhirList = new ArrayList<>();
        adapter = new tugasAkhirAdapter(this, tugasAkhirList);

        adapter.setOnItemClickListener(fileUrl -> {
            // Mulai pengunduhan file menggunakan URL fileUrl
            DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            Uri uri = Uri.parse(fileUrl);
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, uri.getLastPathSegment());
            downloadManager.enqueue(request);

            Toast.makeText(LihatJurnalActivity.this, "Mengunduh file...", Toast.LENGTH_SHORT).show();
        });

        recyclerView.setAdapter(adapter);

        fetchDataFromFirestore();
    }

    private void fetchDataFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("tugasmhs")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String namaUser = document.getString("namaUser");
                            String userNIM = document.getString("userNIM");
                            String fileName = document.getString("fileName");
                            String fileUrl = document.getString("fileUrl");

                            tugasAkhirList.add(new tugasAkhirModel(namaUser, userNIM, fileName, fileUrl));
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(LihatJurnalActivity.this, "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
