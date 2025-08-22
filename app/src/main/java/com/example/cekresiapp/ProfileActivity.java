package com.example.cekresiapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvTotalPaket, tvTerkirim;
    private LinearLayout cardEditProfil, cardUbahPassword, cardNotifikasi, cardBahasa, cardFAQ, cardHubungi, cardRating;
    private LinearLayout llKeluar;
    private BottomNavigationView bottomNavigation;

    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "CekResiPrefs";
    private static final String KEY_RIWAYAT = "riwayat_data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initViews();
        setupBottomNavigation();
        setupClickListeners();
        loadStatistics();
    }

    private void initViews() {
        tvTotalPaket = findViewById(R.id.tvTotalPaket);
        tvTerkirim = findViewById(R.id.tvTerkirim);

        cardEditProfil = findViewById(R.id.cardEditProfil);
        cardUbahPassword = findViewById(R.id.cardUbahPassword);
        cardNotifikasi = findViewById(R.id.cardNotifikasi);
        cardBahasa = findViewById(R.id.cardBahasa);
        cardFAQ = findViewById(R.id.cardFAQ);
        cardHubungi = findViewById(R.id.cardHubungi);
        cardRating = findViewById(R.id.cardRating);
        llKeluar = findViewById(R.id.llKeluar);

        bottomNavigation = findViewById(R.id.bottomNavigation);
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
    }

    private void setupBottomNavigation() {
        bottomNavigation.setSelectedItemId(R.id.nav_profile);

        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_search) {
                Toast.makeText(this, "Fitur Pencarian", Toast.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.nav_history) {
                startActivity(new Intent(ProfileActivity.this, RiwayatActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_profile) {
                return true;
            }
            return false;
        });
    }

    private void setupClickListeners() {
        cardEditProfil.setOnClickListener(v -> {
            Toast.makeText(this, "Edit Profil - Coming Soon", Toast.LENGTH_SHORT).show();
        });

        cardUbahPassword.setOnClickListener(v -> {
            Toast.makeText(this, "Ubah Password - Coming Soon", Toast.LENGTH_SHORT).show();
        });

        cardNotifikasi.setOnClickListener(v -> {
            Toast.makeText(this, "Pengaturan Notifikasi", Toast.LENGTH_SHORT).show();
        });

        cardBahasa.setOnClickListener(v -> {
            Toast.makeText(this, "Pengaturan Bahasa", Toast.LENGTH_SHORT).show();
        });

        cardFAQ.setOnClickListener(v -> {
            Toast.makeText(this, "FAQ - Pertanyaan yang Sering Diajukan", Toast.LENGTH_SHORT).show();
        });

        cardHubungi.setOnClickListener(v -> {
            Toast.makeText(this, "Hubungi Kami - Contact Support", Toast.LENGTH_SHORT).show();
        });

        cardRating.setOnClickListener(v -> {
            Toast.makeText(this, "Beri Rating di Play Store", Toast.LENGTH_SHORT).show();
        });

        llKeluar.setOnClickListener(v -> {
            showLogoutDialog();
        });
    }

    private void loadStatistics() {
        String json = sharedPreferences.getString(KEY_RIWAYAT, "[]");

        try {
            Gson gson = new Gson();
            Type type = new TypeToken<List<RiwayatItem>>(){}.getType();
            List<RiwayatItem> riwayatList = gson.fromJson(json, type);

            int totalPaket = 0;
            int terkirim = 0;

            if (riwayatList != null) {
                totalPaket = riwayatList.size();

                for (RiwayatItem item : riwayatList) {
                    String status = item.getStatus().toUpperCase();
                    if (status.contains("DELIVERED") ||
                            status.contains("TERKIRIM") ||
                            status.contains("SELESAI") ||
                            status.contains("COMPLETED")) {
                        terkirim++;
                    }
                }
            }

            tvTotalPaket.setText(String.valueOf(totalPaket));
            tvTerkirim.setText(String.valueOf(terkirim));

        } catch (Exception e) {
            android.util.Log.e("PROFILE_DEBUG", "Error loading statistics: " + e.getMessage());
            tvTotalPaket.setText("0");
            tvTerkirim.setText("0");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadStatistics(); // Refresh statistics when returning to this activity
        if (bottomNavigation != null) {
            bottomNavigation.setSelectedItemId(R.id.nav_profile);
        }
    }

    private void showLogoutDialog() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Keluar Aplikasi")
                .setMessage("Apakah Anda yakin ingin keluar dari aplikasi?")
                .setPositiveButton("Ya", (dialog, which) -> {
                    // Clear any session data if needed
                    Toast.makeText(this, "Terima kasih telah menggunakan CekResi Pro", Toast.LENGTH_LONG).show();
                    finishAffinity(); // Close all activities
                })
                .setNegativeButton("Batal", null)
                .show();
    }
}