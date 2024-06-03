package com.example.tugas_rpl;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

public class mahasiswaLandFragment extends Fragment {

    private mahasiswaAdapter mahasiswaAdapter;
    private ArrayList<mahasiswaModel> list;
    private DatabaseReference database;
    private TextView nama, nim, prodi, angkatan, sks, ipk, yudisium;

    Button btn, listbtn, ubahbtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_mahasiswa_land, container, false);

        list = new ArrayList<>();
        mahasiswaAdapter = new mahasiswaAdapter(getContext(), list, new mahasiswaAdapter.OnProfileButtonClickListener() {
            @Override
            public void onProfileButtonClick(mahasiswaModel mahasiswa) {

            }
        });

        btn = root.findViewById(R.id.btnupload);
        listbtn = root.findViewById(R.id.listtugas);
        nama = root.findViewById(R.id.nama_mhs);
        nim = root.findViewById(R.id.nim_mhs);
        prodi = root.findViewById(R.id.prodi_mhs);
        angkatan = root.findViewById(R.id.angkatan_mhs);
        sks = root.findViewById(R.id.sks_mhs);
        ipk = root.findViewById(R.id.ipk_mhs);
        yudisium = root.findViewById(R.id.yudisium_mhs);
        ubahbtn = root.findViewById(R.id.btnubah);

        ubahbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment newFragment = new ProfileFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_mahasiswa_land, newFragment);
                transaction.addToBackStack(null); // Optional: Add this if you want to allow the user to navigate back
                transaction.commit();
            }
        });

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userID = currentUser.getUid();
            database = FirebaseDatabase.getInstance().getReference("users");

            database.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (snapshot.hasChild("nama")
                                && snapshot.hasChild("nim")
                                && snapshot.hasChild("prodi")
                                && snapshot.hasChild("angkatan")
                                && snapshot.hasChild("sks")
                                && snapshot.hasChild("foto_profil")
                                && snapshot.hasChild("mahasiswa")
                                && snapshot.hasChild("yudisium")
                                && snapshot.hasChild("ipk")) {

                            // Ambil nilai dari dataSnapshot
                            String namaValue = snapshot.child("nama").getValue(String.class);
                            String nimValue = snapshot.child("nim").getValue(String.class);
                            String mahasiswa = snapshot.child("mahasiswa").getValue(String.class);
                            String prodiValue = snapshot.child("prodi").getValue(String.class);
                            String foto = snapshot.child("foto_profil").getValue(String.class);
                            String angkatanValue = snapshot.child("angkatan").getValue(String.class);
                            Integer sksValue = snapshot.child("sks").getValue(Integer.class);
                            Integer ipkValue = snapshot.child("ipk").getValue(Integer.class);
                            Boolean yudisiumValue = snapshot.child("yudisium").getValue(Boolean.class);

                            // Buat objek mahasiswaModel
                            mahasiswaModel MahasiswaModel = new mahasiswaModel(namaValue, nimValue, "", "", prodiValue, angkatanValue, sksValue, yudisiumValue, mahasiswa, ipkValue);

                            // Tambahkan objek ke dalam list
                            list.add(MahasiswaModel);

                            // Perbarui TextView dengan data yang diambil
                            updateTextViews(namaValue, nimValue, prodiValue, angkatanValue, sksValue, ipkValue, yudisiumValue);
                        } else {
                            Log.d("DataEmpty", "Data not complete for: " + snapshot.getKey());
                        }
                    }
                    // Pemberitahuan adapter bahwa data telah berubah
                    mahasiswaAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Penanganan kesalahan jika pembacaan data gagal
                    Log.e(TAG, "Failed to read data from Firebase.", databaseError.toException());
                }
            });
        } else {
            Log.e(TAG, "User not authenticated.");
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aksi yang akan dijalankan ketika tombol ditekan
                // Contoh: Beralih ke UploadActivity
                Intent intent = new Intent(getActivity(), UploadActivity.class);
                startActivity(intent);
            }
        });

        listbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ListTugasActivity.class);
                startActivity(i);
            }
        });

        return root;
    }

    private void updateTextViews(String namaValue, String nimValue, String prodiValue, String angkatanValue, Integer sksValue, Integer ipkValue, Boolean yudisiumValue) {
        nama.setText(namaValue);
        nim.setText(nimValue);
        prodi.setText(prodiValue);
        angkatan.setText(angkatanValue);
        sks.setText(String.valueOf(sksValue));
        ipk.setText(String.valueOf(ipkValue));
        yudisium.setText(yudisiumValue ? "Yes" : "No");
    }

    boolean inUser() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }
}
