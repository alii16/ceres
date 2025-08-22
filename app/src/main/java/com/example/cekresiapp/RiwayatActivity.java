package com.example.cekresiapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RiwayatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RiwayatAdapter adapter;
    private List<RiwayatItem> riwayatList;
    private List<RiwayatItem> originalRiwayatList;
    private Button btnSemua, btnTerkirim, btnProses, btnGagal;
    private ImageView btnHapusSemua;
    private LinearLayout emptyState;
    private LottieAnimationView lottieAnimation;

    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "CekResiPrefs";
    private static final String KEY_RIWAYAT = "riwayat_data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat);

        // Inisialisasi komponen
        initViews();
        setupRecyclerView();
        setupBottomNavigation();
        setupFilterButtons();

        // Load data dari SharedPreferences
        loadRiwayatData();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerRiwayat);
        btnSemua = findViewById(R.id.btnSemua);
        btnTerkirim = findViewById(R.id.btnTerkirim);
        btnProses = findViewById(R.id.btnProses);
        btnGagal = findViewById(R.id.btnGagal);
        btnHapusSemua = findViewById(R.id.btnHapusSemua);
        emptyState = findViewById(R.id.emptyState);
        lottieAnimation = findViewById(R.id.lottieAnimation);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        riwayatList = new ArrayList<>();
        originalRiwayatList = new ArrayList<>();
    }

    private void setupRecyclerView() {
        adapter = new RiwayatAdapter(this, riwayatList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setSelectedItemId(R.id.nav_history);

        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                finish();
                return true;
            } else if (itemId == R.id.nav_search) {
                Toast.makeText(this, "Fitur Pencarian", Toast.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.nav_history) {
                return true;
            } else if (itemId == R.id.nav_profile) {
                // Navigasi ke ProfileActivity
                startActivity(new Intent(RiwayatActivity.this, ProfileActivity.class));
                return true;
            }
            return false;
        });
    }

    private void setupFilterButtons() {
        setActiveButton(btnSemua);

        btnSemua.setOnClickListener(v -> {
            setActiveButton(btnSemua);
            filterRiwayat("SEMUA");
        });

        btnTerkirim.setOnClickListener(v -> {
            setActiveButton(btnTerkirim);
            filterRiwayat("TERKIRIM");
        });

        btnProses.setOnClickListener(v -> {
            setActiveButton(btnProses);
            filterRiwayat("PROSES");
        });

        btnGagal.setOnClickListener(v -> {
            setActiveButton(btnGagal);
            filterRiwayat("GAGAL");
        });

        btnHapusSemua.setOnClickListener(v -> hapusSemuaRiwayat());
    }

    private void setActiveButton(Button activeButton) {
        // Reset semua button ke inactive state
        resetButton(btnSemua);
        resetButton(btnTerkirim);
        resetButton(btnProses);
        resetButton(btnGagal);

        // Set active button
        setButtonActive(activeButton);
    }

    private void resetButton(Button button) {
        button.setBackgroundResource(R.drawable.button_filter_inactive);
        button.setTextColor(ContextCompat.getColor(this, R.color.text_hint));
    }

    private void setButtonActive(Button button) {
        button.setBackgroundResource(R.drawable.button_filter_active);
        button.setTextColor(ContextCompat.getColor(this, android.R.color.white));
    }

    private void filterRiwayat(String filterType) {
        List<RiwayatItem> filteredList = new ArrayList<>();

        if (filterType.equals("SEMUA")) {
            filteredList.addAll(originalRiwayatList);
        } else {
            for (RiwayatItem item : originalRiwayatList) {
                String status = item.getStatus().toUpperCase();

                switch (filterType) {
                    case "TERKIRIM":
                        if (status.contains("DELIVERED") ||
                                status.contains("TERKIRIM") ||
                                status.contains("SELESAI") ||
                                status.contains("COMPLETED")) {
                            filteredList.add(item);
                        }
                        break;
                    case "PROSES":
                        if (status.contains("PROSES") ||
                                status.contains("PROCESSING") ||
                                status.contains("PERJALANAN") ||
                                status.contains("TRANSIT") ||
                                status.contains("ON THE WAY") ||
                                status.contains("SHIPMENT") ||
                                (!status.contains("DELIVERED") &&
                                        !status.contains("TERKIRIM") &&
                                        !status.contains("SELESAI") &&
                                        !status.contains("COMPLETED") &&
                                        !status.contains("GAGAL"))) {
                            filteredList.add(item);
                        }
                        break;
                    case "GAGAL":
                        if (status.contains("GAGAL") ||
                                status.contains("FAILED") ||
                                status.contains("CANCELED") ||
                                status.contains("BATAL") ||
                                status.contains("ERROR") ||
                                status.contains("TIDAK TERKIRIM")) {
                            filteredList.add(item);
                        }
                        break;
                }
            }
        }

        // Update adapter dengan data yang sudah difilter
        riwayatList.clear();
        riwayatList.addAll(filteredList);
        adapter.notifyDataSetChanged();

        // Update tampilan empty state
        updateEmptyState();

        // Debug log
        android.util.Log.d("FILTER_DEBUG", "Filter: " + filterType + ", Items: " + filteredList.size());
    }

    private void loadRiwayatData() {
        String json = sharedPreferences.getString(KEY_RIWAYAT, "[]");
        android.util.Log.d("RIWAYAT_DEBUG", "JSON loaded: " + json);

        try {
            Gson gson = new Gson();
            Type type = new TypeToken<List<RiwayatItem>>(){}.getType();
            List<RiwayatItem> loadedList = gson.fromJson(json, type);

            if (loadedList != null && !loadedList.isEmpty()) {
                originalRiwayatList.clear();
                originalRiwayatList.addAll(loadedList);

                riwayatList.clear();
                riwayatList.addAll(loadedList);

                adapter.notifyDataSetChanged();
                android.util.Log.d("RIWAYAT_DEBUG", "Data loaded: " + loadedList.size() + " items");
            } else {
                android.util.Log.d("RIWAYAT_DEBUG", "No data found");
            }
        } catch (Exception e) {
            android.util.Log.e("RIWAYAT_DEBUG", "Error loading data: " + e.getMessage());
            e.printStackTrace();
        }

        // Update tampilan empty state setelah load data
        updateEmptyState();
    }

    // Method untuk mengatur tampilan empty state
    private void updateEmptyState() {
        if (riwayatList.isEmpty()) {
            // Tampilkan empty state, sembunyikan RecyclerView
            emptyState.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);

            // Restart animasi Lottie
            if (lottieAnimation != null) {
                lottieAnimation.playAnimation();
            }
        } else {
            // Sembunyikan empty state, tampilkan RecyclerView
            emptyState.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            // Pause animasi Lottie untuk menghemat resource
            if (lottieAnimation != null) {
                lottieAnimation.pauseAnimation();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload data ketika kembali ke activity ini
        loadRiwayatData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Pause animasi ketika activity tidak terlihat
        if (lottieAnimation != null) {
            lottieAnimation.pauseAnimation();
        }
    }

    private void hapusSemuaRiwayat() {
        // Show confirmation dialog
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Konfirmasi")
                .setMessage("Apakah Anda yakin ingin menghapus semua riwayat?")
                .setPositiveButton("Ya", (dialog, which) -> {
                    // Hapus dari SharedPreferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(KEY_RIWAYAT, "[]");
                    editor.apply();

                    // Hapus dari list dan update UI
                    originalRiwayatList.clear();
                    riwayatList.clear();
                    adapter.notifyDataSetChanged();

                    // Update empty state setelah hapus semua
                    updateEmptyState();

                    Toast.makeText(this, "Semua riwayat telah dihapus", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Batal", null)
                .show();
    }

    // Method untuk menambah riwayat baru (dipanggil dari MainActivity)
    public static void tambahRiwayat(android.content.Context context, String resi, String kurir,
                                     String status, String deskripsi) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String json = prefs.getString(KEY_RIWAYAT, "[]");

        try {
            Gson gson = new Gson();
            Type type = new TypeToken<List<RiwayatItem>>(){}.getType();
            List<RiwayatItem> list = gson.fromJson(json, type);

            if (list == null) {
                list = new ArrayList<>();
            }

            // Buat item baru
            RiwayatItem newItem = new RiwayatItem();
            newItem.setResi(resi);
            newItem.setKurir(kurir.toUpperCase());
            newItem.setStatus(status);
            newItem.setDeskripsi(deskripsi);
            newItem.setTanggal(getCurrentDateTime());

            // Cek apakah resi sudah ada, jika ya update, jika tidak tambah baru
            boolean found = false;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getResi().equals(resi) &&
                        list.get(i).getKurir().equalsIgnoreCase(kurir)) {
                    list.set(i, newItem); // Update existing
                    found = true;
                    break;
                }
            }

            if (!found) {
                list.add(0, newItem); // Tambah di posisi pertama (terbaru di atas)
            }

            // Batasi maksimal 100 item untuk menghemat memori
            if (list.size() > 100) {
                list = list.subList(0, 100);
            }

            // Simpan kembali
            String updatedJson = gson.toJson(list);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(KEY_RIWAYAT, updatedJson);
            editor.apply();

            android.util.Log.d("RIWAYAT_DEBUG", "Riwayat saved: " + resi + " - " + status);

        } catch (Exception e) {
            android.util.Log.e("RIWAYAT_DEBUG", "Error saving riwayat: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static String getCurrentDateTime() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd MMM yyyy, HH:mm",
                java.util.Locale.getDefault());
        return sdf.format(new java.util.Date());
    }
}