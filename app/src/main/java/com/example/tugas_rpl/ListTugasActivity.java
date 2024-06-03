package com.example.tugas_rpl;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ToggleButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class ListTugasActivity extends AppCompatActivity {
    private RecyclerView recyclerView;

    ToggleButton toggleButton;
    private tugasAkhirAdapter adapter;
    private ArrayList<tugasAkhirModel> tugasAkhirList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_tugas);
        recyclerView = findViewById(R.id.rec_tugas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        tugasAkhirList = new ArrayList<>();
        fetchDataFromFirestore();
        adapter = new tugasAkhirAdapter(this, tugasAkhirList);
        recyclerView.setAdapter(adapter);
    }
    private void fetchDataFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userID = user.getUid();

            db.collection("tugasmhs")
                    .whereEqualTo("user_id", userID)
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

                        }
                    });
        }
    }
}
