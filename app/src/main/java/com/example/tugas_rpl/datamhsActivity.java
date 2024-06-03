package com.example.tugas_rpl;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class datamhsActivity extends AppCompatActivity {

    FirebaseAuth fAuth;
    DatabaseReference db;
    private ArrayList<mahasiswaModel> models;
    private dataAdapter dataAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datamhs);

        recyclerView = findViewById(R.id.rec_datammahasiswa);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
        models = new ArrayList<>();
        dataAdapter = new dataAdapter(this, models);
        recyclerView.setAdapter(dataAdapter);

        fAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference("users");

        fetchDataFromFirebase();
    }

    private void fetchDataFromFirebase() {
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                models.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    mahasiswaModel mahasiswa = snapshot.getValue(mahasiswaModel.class);
                    if (mahasiswa != null) {
                        models.add(mahasiswa);
                    }
                }
                dataAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("datamhsActivity", "Failed to read data.", error.toException());
            }
        });
    }
}
