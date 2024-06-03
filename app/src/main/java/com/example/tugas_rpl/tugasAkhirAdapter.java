package com.example.tugas_rpl;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class tugasAkhirAdapter extends RecyclerView.Adapter<tugasAkhirAdapter.MyViewHolder> {

    Context context;
    ArrayList<tugasAkhirModel> tugasAkhirModelArrayList;
    private OnItemClickListener listener;

    public tugasAkhirAdapter(Context context, ArrayList<tugasAkhirModel> tugasAkhirModelArrayList) {
        this.context = context;
        this.tugasAkhirModelArrayList = tugasAkhirModelArrayList;
    }

    public interface OnItemClickListener {
        void onItemClick(String fileUrl);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public tugasAkhirAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.tugas_akhir, viewGroup, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull tugasAkhirAdapter.MyViewHolder myViewHolder, int i) {
        tugasAkhirModel tugasAkhirModel = tugasAkhirModelArrayList.get(i);
        myViewHolder.nama.setText(tugasAkhirModel.getNama());
        myViewHolder.nim.setText(tugasAkhirModel.getNim());
        myViewHolder.judul.setText(tugasAkhirModel.getFileName());

        myViewHolder.judul.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(tugasAkhirModel.getFileUrl());
            }
        });

        // Set the initial state of the ToggleButton
        updateToggleButton(myViewHolder.toggleButton, myViewHolder.toggleButton.isChecked());

        // Add a listener to change colors when the toggle button is clicked
        myViewHolder.toggleButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateToggleButton((ToggleButton) buttonView, isChecked);
        });
    }

    private void updateToggleButton(ToggleButton toggleButton, boolean isChecked) {
        if (isChecked) {
            toggleButton.setBackgroundColor(context.getResources().getColor(android.R.color.holo_green_dark));
            toggleButton.setTextColor(context.getResources().getColor(android.R.color.white));
        } else {
            toggleButton.setBackgroundColor(context.getResources().getColor(android.R.color.darker_gray));
            toggleButton.setTextColor(context.getResources().getColor(android.R.color.black));
        }
    }


    @Override
    public int getItemCount() {
        return tugasAkhirModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nama, nim, judul;
        ToggleButton toggleButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nama = itemView.findViewById(R.id.namaMhs);
            nim = itemView.findViewById(R.id.nimMhs);
            judul = itemView.findViewById(R.id.judulTugas);
            toggleButton = itemView.findViewById(R.id.togglebtn);
            toggleButton.setEnabled(true);

            // Set initial color state for the toggle button
            updateToggleButton(toggleButton, toggleButton.isChecked());

            // Add a listener to change colors when the toggle button is clicked
            toggleButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
                updateToggleButton((ToggleButton) buttonView, isChecked);
            });
        }
    }

}
