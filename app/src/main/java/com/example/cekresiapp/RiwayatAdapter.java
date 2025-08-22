package com.example.cekresiapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RiwayatAdapter extends RecyclerView.Adapter<RiwayatAdapter.RiwayatViewHolder> {

    private Context context;
    private List<RiwayatItem> riwayatList;

    public RiwayatAdapter(Context context, List<RiwayatItem> riwayatList) {
        this.context = context;
        this.riwayatList = riwayatList;
    }

    @NonNull
    @Override
    public RiwayatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_riwayat, parent, false);
        return new RiwayatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RiwayatViewHolder holder, int position) {
        RiwayatItem item = riwayatList.get(position);

        // Set data
        holder.tvResi.setText(item.getResi());
        holder.tvKurir.setText(item.getKurir());
        holder.tvStatus.setText(item.getStatus());
        holder.tvDeskripsi.setText(item.getDeskripsi());
        holder.tvTanggal.setText(item.getTanggal());

        // Set icon status dengan warna yang sesuai
        holder.ivStatusIcon.setImageResource(item.getStatusIcon());

        // Set warna icon sesuai status
        int iconColor = ContextCompat.getColor(context, item.getIconColor());
        DrawableCompat.setTint(holder.ivStatusIcon.getDrawable().mutate(), iconColor);

        // Set warna kurir
        holder.tvKurir.setBackgroundResource(item.getKurirBackgroundColor());
        holder.tvKurir.setTextColor(ContextCompat.getColor(context, item.getKurirTextColor()));

        // Set warna status
        holder.tvStatus.setTextColor(ContextCompat.getColor(context, item.getStatusColor()));

        // Debug log untuk memastikan warna sesuai
        android.util.Log.d("STATUS_DEBUG", "Resi: " + item.getResi() +
                ", Status: " + item.getStatus() +
                ", Icon Color: " + item.getIconColor());

        // Click listener untuk detail
        holder.btnDetail.setOnClickListener(v -> {
            // Implementasi detail - bisa navigate ke DetailActivity atau show dialog
            Toast.makeText(context, "Detail untuk resi: " + item.getResi(), Toast.LENGTH_SHORT).show();
        });

        // Click listener untuk item
        holder.itemView.setOnClickListener(v -> {
            Toast.makeText(context, "Resi: " + item.getResi(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return riwayatList.size();
    }

    // Method untuk update list (untuk filtering)
    public void updateList(List<RiwayatItem> newList) {
        riwayatList.clear();
        riwayatList.addAll(newList);
        notifyDataSetChanged();
    }

    // ViewHolder class
    public static class RiwayatViewHolder extends RecyclerView.ViewHolder {
        ImageView ivStatusIcon;
        TextView tvResi, tvKurir, tvStatus, tvDeskripsi, tvTanggal, btnDetail;

        public RiwayatViewHolder(@NonNull View itemView) {
            super(itemView);

            ivStatusIcon = itemView.findViewById(R.id.ivStatusIcon);
            tvResi = itemView.findViewById(R.id.tvResi);
            tvKurir = itemView.findViewById(R.id.tvKurir);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvDeskripsi = itemView.findViewById(R.id.tvDeskripsi);
            tvTanggal = itemView.findViewById(R.id.tvTanggal);
            btnDetail = itemView.findViewById(R.id.btnDetail);
        }
    }
}