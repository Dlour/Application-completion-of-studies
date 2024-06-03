package com.example.tugas_rpl;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class mahasiswaAdapter extends RecyclerView.Adapter<mahasiswaAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<mahasiswaModel> list;
    private OnProfileButtonClickListener onProfileButtonClickListener;

    public mahasiswaAdapter(Context context, ArrayList<mahasiswaModel> list, OnProfileButtonClickListener listener) {
        this.context = context;
        this.list = list;
        this.onProfileButtonClickListener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.mahasiswa, viewGroup, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        mahasiswaModel mahasiswaModel = list.get(i);
        myViewHolder.nama_mhs.setText(mahasiswaModel.getNama());
        myViewHolder.nim_mhs.setText(mahasiswaModel.getNim());
        myViewHolder.prodi_mhs.setText(mahasiswaModel.getProdi());
        myViewHolder.angkatan_mhs.setText(mahasiswaModel.getAngkatan());
        myViewHolder.sks_mhs.setText(String.valueOf(mahasiswaModel.getSks()));
        myViewHolder.ipk_mhs.setText(String.valueOf(mahasiswaModel.getIpk()));
        myViewHolder.yudisium_mhs.setText(mahasiswaModel.isYudisium() ? "Sudah Acc" : "Belum Acc");

        myViewHolder.btn.setOnClickListener(v -> {
            Toast.makeText(context.getApplicationContext(), "ditekan", Toast.LENGTH_SHORT).show();
            if (onProfileButtonClickListener != null) {
                onProfileButtonClickListener.onProfileButtonClick(mahasiswaModel);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nama_mhs, nim_mhs, prodi_mhs, angkatan_mhs, sks_mhs, ipk_mhs, yudisium_mhs;
        Button btn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nama_mhs = itemView.findViewById(R.id.nama_mhs);
            nim_mhs = itemView.findViewById(R.id.nim_mhs);
            prodi_mhs = itemView.findViewById(R.id.prodi_mhs);
            angkatan_mhs = itemView.findViewById(R.id.angkatan_mhs);
            sks_mhs = itemView.findViewById(R.id.sks_mhs);
            ipk_mhs = itemView.findViewById(R.id.ipk_mhs);
            yudisium_mhs = itemView.findViewById(R.id.yudisium_mhs);
            btn = itemView.findViewById(R.id.btnubah);
        }
    }

    public interface OnProfileButtonClickListener {
        void onProfileButtonClick(mahasiswaModel mahasiswa);
    }
}
