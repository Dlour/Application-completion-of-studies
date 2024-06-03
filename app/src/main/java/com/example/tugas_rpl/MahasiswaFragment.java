package com.example.tugas_rpl;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MahasiswaFragment extends Fragment {

    private RecyclerView datamhs_rec;
    private Button ubahbtn;
    private DatabaseReference database;
    private ArrayList<mahasiswaModel> list;
    private mahasiswaAdapter mahasiswaAdapter;

    public MahasiswaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_mahasiswa, container, false);

        datamhs_rec = root.findViewById(R.id.rec_mahasiswa);
        datamhs_rec.setHasFixedSize(true);
        datamhs_rec.setLayoutManager(new LinearLayoutManager(getContext()));
        list = new ArrayList<>();
        mahasiswaAdapter = new mahasiswaAdapter(getContext(), list, new mahasiswaAdapter.OnProfileButtonClickListener() {
            @Override
            public void onProfileButtonClick(mahasiswaModel mahasiswa) {
                openProfileFragment();
            }
        });
        datamhs_rec.setAdapter(mahasiswaAdapter);

        ubahbtn = root.findViewById(R.id.ubah_data);

        ubahbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inUser()) {
                    openProfileFragment();
                } else {
                    startActivity(new Intent(getContext(), Login.class));
                    getActivity().finish();
                }
            }
        });

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            database = FirebaseDatabase.getInstance().getReference("users");

            database.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    list.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String nama = snapshot.child("nama").getValue(String.class);
                        String nim = snapshot.child("nim").getValue(String.class);
                        String prodi = snapshot.child("prodi").getValue(String.class);
                        String angkatan = snapshot.child("angkatan").getValue(String.class);
                        Integer sks = snapshot.child("sks").getValue(Integer.class);
                        Double ipk = snapshot.child("ipk").getValue(Double.class);
                        Boolean yudisium = snapshot.child("yudisium").getValue(Boolean.class);

                        // Log jika data tidak lengkap
                        if (nama == null || nim == null || prodi == null || angkatan == null || sks == null || ipk == null || yudisium == null) {
                            Log.d("DataEmpty", "Data not complete for: " + snapshot.getKey());
                            continue;
                        }

                        mahasiswaModel MahasiswaModel = new mahasiswaModel(nama, nim, "", "", prodi, angkatan, sks, yudisium, "", ipk);
                        list.add(MahasiswaModel);
                        Log.d("Firebase", "Added MahasiswaModel: " + MahasiswaModel.toString());
                    }
                    mahasiswaAdapter.notifyDataSetChanged();
                    Log.d("Firebase", "Data changed, adapter notified");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("Firebase", "Failed to read data from Firebase.", databaseError.toException());
                }
            });

        } else {
            Log.e("TAG", "User not authenticated.");
        }

        return root;
    }

    private boolean inUser() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    private void openProfileFragment() {
        ProfileFragment profileFragment = new ProfileFragment();
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_mahasiswa2, profileFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
