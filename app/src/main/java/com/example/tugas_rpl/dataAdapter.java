package com.example.tugas_rpl;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class dataAdapter extends RecyclerView.Adapter<dataAdapter.ViewHolder>{

    private Context context;
    private ArrayList<mahasiswaModel> list;

    public dataAdapter(Context context, ArrayList<mahasiswaModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public dataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.data_layout, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull dataAdapter.ViewHolder viewHolder, int i) {
        mahasiswaModel mahasiswaModel = list.get(i);
        viewHolder.nama.setText(mahasiswaModel.getNama());
        viewHolder.nim.setText(mahasiswaModel.getNim());
        viewHolder.prodi.setText(mahasiswaModel.getProdi());
        viewHolder.angkatan.setText(mahasiswaModel.getAngkatan());
        viewHolder.sks.setText(String.valueOf(mahasiswaModel.getSks()));
        viewHolder.ipk.setText(String.valueOf(mahasiswaModel.getIpk()));
        viewHolder.yudisium.setText(mahasiswaModel.isYudisium() ? "Sudah Acc" : "Belum Acc");


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nama, nim, prodi, angkatan, sks, ipk, yudisium, email;
        ImageView fotoProf;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nama = itemView.findViewById(R.id.dataNama);
            nim = itemView.findViewById(R.id.dataNim);
            prodi = itemView.findViewById(R.id.dataProdi);
            angkatan = itemView.findViewById(R.id.dataAngkatan);
            sks = itemView.findViewById(R.id.dataSks);
            ipk = itemView.findViewById(R.id.dataIPK); // Pastikan untuk menginisialisasi dataIpk
            yudisium = itemView.findViewById(R.id.dataYudisium);
            email = itemView.findViewById(R.id.dataEmail);

        }
    }
}
