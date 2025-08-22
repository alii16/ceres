package com.example.cekresiapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.content.Intent;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    // UI Components
    private TextInputEditText editResi, editKurir;
    private MaterialButton btnCek;
    private TextView txtHasilDefault;
    private ProgressBar progressBar;
    private BottomNavigationView bottomNavigation;

    // Result Components
    private LinearLayout containerHasil;
    private LinearLayout containerPackageInfo, containerTimeline;
    private MaterialCardView cardStatus, cardShippingInfo;
    private TextView txtStatus, txtStatusDate, txtResi;
    private TextView txtKurirInfo, txtServiceInfo, txtOriginInfo, txtDestinationInfo;
    private TextView txtShipperInfo, txtReceiverInfo;
    private LinearLayout timelineContainer;
    private ImageView imgStatusIcon;

    // Chips
    private Chip chipJNE, chipJNT, chipSiCepat, chipPOS, chipAnterAja, chipTIKI, chipNinja, chipSPX;

    // API Key (from binderbyte.com)
    private final String API_KEY = "a0fead842849309175d20fad8b27524f1c4935c34bb11fceeeec64ac4a4de1c4";

    // HTTP client
    private final OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI
        initializeViews();
        initializeChips();
        setupChipClickListeners();
        setupButtonClickListener();
        setupBottomNavigation();
    }

    private void initializeViews() {
        editResi = findViewById(R.id.editResi);
        editKurir = findViewById(R.id.editKurir);
        btnCek = findViewById(R.id.btnCek);
        progressBar = findViewById(R.id.progressBar);
        bottomNavigation = findViewById(R.id.bottomNavigation);

        // Result components
        containerHasil = findViewById(R.id.containerHasil);
        txtHasilDefault = findViewById(R.id.txtHasilDefault);
        cardStatus = findViewById(R.id.cardStatus);
        containerPackageInfo = findViewById(R.id.containerPackageInfo);
        cardShippingInfo = findViewById(R.id.cardShippingInfo);
        containerTimeline = findViewById(R.id.containerTimeline);

        // Status card components
        txtStatus = findViewById(R.id.txtStatus);
        txtStatusDate = findViewById(R.id.txtStatusDate);
        txtResi = findViewById(R.id.txtResi);
        imgStatusIcon = findViewById(R.id.imgStatusIcon);

        // Package info components
        txtKurirInfo = findViewById(R.id.txtKurirInfo);
        txtServiceInfo = findViewById(R.id.txtServiceInfo);
        txtOriginInfo = findViewById(R.id.txtOriginInfo);
        txtDestinationInfo = findViewById(R.id.txtDestinationInfo);

        // Shipping info components
        txtShipperInfo = findViewById(R.id.txtShipperInfo);
        txtReceiverInfo = findViewById(R.id.txtReceiverInfo);

        // Timeline container
        timelineContainer = findViewById(R.id.timelineContainer);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (bottomNavigation != null) {
            bottomNavigation.setSelectedItemId(R.id.nav_home);
        }
    }

    private void setupBottomNavigation() {
        bottomNavigation.setSelectedItemId(R.id.nav_home);

        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                return true;
            } else if (itemId == R.id.nav_history) {
                startActivity(new Intent(MainActivity.this, RiwayatActivity.class));
                return true;
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                return true;
            }
            return false;
        });
    }

    private void initializeChips() {
        chipJNE = findViewById(R.id.chipJNE);
        chipJNT = findViewById(R.id.chipJNT);
        chipSiCepat = findViewById(R.id.chipSiCepat);
        chipPOS = findViewById(R.id.chipPOS);
        chipAnterAja = findViewById(R.id.chipAnterAja);
        chipTIKI = findViewById(R.id.chipTIKI);
        chipNinja = findViewById(R.id.chipNinja);
        chipSPX = findViewById(R.id.chipSPX);
    }

    private void setupChipClickListeners() {
        chipJNE.setOnClickListener(v -> setKurirValue("jne"));
        chipJNT.setOnClickListener(v -> setKurirValue("jnt"));
        chipSiCepat.setOnClickListener(v -> setKurirValue("sicepat"));
        chipPOS.setOnClickListener(v -> setKurirValue("pos"));
        chipAnterAja.setOnClickListener(v -> setKurirValue("anteraja"));
        chipTIKI.setOnClickListener(v -> setKurirValue("tiki"));
        chipNinja.setOnClickListener(v -> setKurirValue("ninja"));
        chipSPX.setOnClickListener(v -> setKurirValue("spx"));
    }

    private void setKurirValue(String kurirValue) {
        editKurir.setText(kurirValue);
        editKurir.setSelection(kurirValue.length());
        Toast.makeText(this, "Kurir dipilih: " + kurirValue.toUpperCase(), Toast.LENGTH_SHORT).show();
        editKurir.clearFocus();
    }

    private void setupButtonClickListener() {
        btnCek.setOnClickListener(v -> {
            String resi = editResi.getText().toString().trim();
            String kurir = editKurir.getText().toString().trim();

            if (!isNetworkAvailable()) {
                Snackbar.make(v, "⚠ Tidak ada koneksi internet. Mohon periksa jaringan Anda.", Snackbar.LENGTH_LONG)
                        .setBackgroundTint(getResources().getColor(R.color.md_theme_error))
                        .setTextColor(getResources().getColor(R.color.md_theme_onError))
                        .show();
                return;
            }

            if (!resi.isEmpty() && !kurir.isEmpty()) {
                cekResi(resi, kurir);
            } else {
                Toast.makeText(this, "⚠️ Mohon isi nomor resi dan kode kurir.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            return capabilities != null &&
                    (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));
        } else {
            android.net.NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
    }

    private void hideAllResultViews() {
        txtHasilDefault.setVisibility(View.VISIBLE);
        cardStatus.setVisibility(View.GONE);
        containerPackageInfo.setVisibility(View.GONE);
        cardShippingInfo.setVisibility(View.GONE);
        containerTimeline.setVisibility(View.GONE);
    }

    private void showResultViews() {
        txtHasilDefault.setVisibility(View.GONE);
        cardStatus.setVisibility(View.VISIBLE);
        containerPackageInfo.setVisibility(View.VISIBLE);
        cardShippingInfo.setVisibility(View.VISIBLE);
        containerTimeline.setVisibility(View.VISIBLE);
    }

    private void updateStatusCard(String status, String date, String resi, String statusType) {
        txtStatus.setText(status);
        txtStatusDate.setText(date);
        txtResi.setText(resi);

        // Update status card color based on status
        int bgColor, textColor, strokeColor, iconRes;

        if (statusType.toLowerCase().contains("delivered") || statusType.toLowerCase().contains("terkirim")) {
            bgColor = R.color.success_alpha_10;
            textColor = R.color.md_theme_success;
            strokeColor = R.color.md_theme_success;
            iconRes = R.drawable.ic_check_circle;
        } else if (statusType.toLowerCase().contains("transit") || statusType.toLowerCase().contains("perjalanan")) {
            bgColor = R.color.warning_alpha_10;
            textColor = R.color.md_theme_warning;
            strokeColor = R.color.md_theme_warning;
            iconRes = R.drawable.ic_courier;
        } else {
            bgColor = R.color.info_alpha_10;
            textColor = R.color.md_theme_primary;
            strokeColor = R.color.md_theme_primary;
            iconRes = R.drawable.ic_info;
        }

        cardStatus.setCardBackgroundColor(ContextCompat.getColor(this, bgColor));
        cardStatus.setStrokeColor(ContextCompat.getColor(this, strokeColor));
        txtStatus.setTextColor(ContextCompat.getColor(this, textColor));
        imgStatusIcon.setImageResource(iconRes);
        imgStatusIcon.setColorFilter(ContextCompat.getColor(this, textColor));
    }

    private void createTimelineItem(String date, String desc, boolean isLast, boolean isLatest) {
        View timelineView = LayoutInflater.from(this).inflate(R.layout.timeline_item, null);

        TextView txtTimelineDate = timelineView.findViewById(R.id.txtTimelineDate);
        TextView txtTimelineDesc = timelineView.findViewById(R.id.txtTimelineDesc);
        TextView txtTimelineStatus = timelineView.findViewById(R.id.txtTimelineStatus);
        View timelineDot = timelineView.findViewById(R.id.timelineDot);
        View timelineLine = timelineView.findViewById(R.id.timelineLine);

        txtTimelineDate.setText(date);
        txtTimelineDesc.setText(desc);

        // Extract status from description for status badge
        String statusBadge = extractStatus(desc);
        if (!statusBadge.isEmpty()) {
            txtTimelineStatus.setText(statusBadge);
            txtTimelineStatus.setVisibility(View.VISIBLE);
        } else {
            txtTimelineStatus.setVisibility(View.GONE);
        }

        // Set dot color based on position
        if (isLatest) {
            timelineDot.setBackground(ContextCompat.getDrawable(this, R.drawable.timeline_dot_active));
        } else {
            timelineDot.setBackground(ContextCompat.getDrawable(this, R.drawable.timeline_dot_completed));
        }

        // Hide line for last item
        if (isLast) {
            timelineLine.setVisibility(View.GONE);
        }

        timelineContainer.addView(timelineView);
    }

    private String extractStatus(String description) {
        description = description.toLowerCase();
        if (description.contains("terkirim") || description.contains("delivered")) {
            return "DELIVERED";
        } else if (description.contains("transit") || description.contains("perjalanan")) {
            return "TRANSIT";
        } else if (description.contains("dikirim") || description.contains("pickup")) {
            return "SHIPPED";
        } else if (description.contains("diterima") || description.contains("received")) {
            return "RECEIVED";
        }
        return "";
    }

    private void cekResi(String resi, String kurir) {
        progressBar.setVisibility(View.VISIBLE);
        hideAllResultViews();
        txtHasilDefault.setText("Memuat informasi pelacakan...");
        txtHasilDefault.setVisibility(View.VISIBLE);

        String url = "https://api.binderbyte.com/v1/track?api_key=" + API_KEY +
                "&courier=" + kurir + "&awb=" + resi;

        Log.d("CEKRESI_LOG", "🔗 URL: " + url);

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    txtHasilDefault.setText("⚠ Gagal koneksi: " + e.getMessage());
                    txtHasilDefault.setVisibility(View.VISIBLE);
                    Snackbar.make(findViewById(android.R.id.content), "Koneksi gagal: " + e.getMessage(), Snackbar.LENGTH_LONG)
                            .setBackgroundTint(getResources().getColor(R.color.md_theme_error))
                            .setTextColor(getResources().getColor(R.color.md_theme_onError))
                            .show();
                });
                RiwayatActivity.tambahRiwayat(MainActivity.this, resi, kurir,
                        "GAGAL", "Gagal koneksi: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                runOnUiThread(() -> progressBar.setVisibility(View.GONE));

                String result = response.body() != null ? response.body().string() : "";
                Log.d("CEKRESI_LOG", "🧾 RESPON: " + result);

                if (result.trim().isEmpty()) {
                    runOnUiThread(() -> {
                        txtHasilDefault.setText("⚠️ Respon kosong dari server.");
                        txtHasilDefault.setVisibility(View.VISIBLE);
                        Snackbar.make(findViewById(android.R.id.content), "⚠️ Respon kosong dari server.", Snackbar.LENGTH_LONG)
                                .setBackgroundTint(getResources().getColor(R.color.md_theme_warning))
                                .setTextColor(getResources().getColor(R.color.md_theme_onPrimary))
                                .show();
                    });
                    RiwayatActivity.tambahRiwayat(MainActivity.this, resi, kurir,
                            "GAGAL", "Respon kosong dari server");
                    return;
                }

                try {
                    JSONObject json = new JSONObject(result);
                    int status = json.optInt("status", 0);
                    String message = json.optString("message");

                    if (status == 200) {
                        JSONObject data = json.getJSONObject("data");
                        JSONObject summary = data.getJSONObject("summary");
                        JSONObject detail = data.getJSONObject("detail");
                        JSONArray historyArray = data.getJSONArray("history");

                        runOnUiThread(() -> {
                            showResultViews();

                            // Update status card
                            updateStatusCard(
                                    cekKosong(summary.optString("status")),
                                    cekKosong(summary.optString("date")),
                                    cekKosong(summary.optString("awb")),
                                    cekKosong(summary.optString("status"))
                            );

                            // Update package info
                            txtKurirInfo.setText(cekKosong(summary.optString("courier")).toUpperCase());
                            txtServiceInfo.setText(cekKosong(summary.optString("service")));
                            txtOriginInfo.setText(cekKosong(detail.optString("origin")));
                            txtDestinationInfo.setText(cekKosong(detail.optString("destination")));

                            // Update shipping info
                            txtShipperInfo.setText(cekKosong(detail.optString("shipper")));
                            txtReceiverInfo.setText(cekKosong(detail.optString("receiver")));

                            // Clear previous timeline
                            timelineContainer.removeAllViews();

                            // Add timeline items
                            for (int i = 0; i < historyArray.length(); i++) {
                                try {
                                    JSONObject item = historyArray.getJSONObject(i);
                                    String timelineDate = cekKosong(item.optString("date"));
                                    String timelineDesc = cekKosong(item.optString("desc"));
                                    boolean isLast = (i == historyArray.length() - 1);
                                    boolean isLatest = (i == 0); // First item is latest

                                    createTimelineItem(timelineDate, timelineDesc, isLast, isLatest);
                                } catch (Exception e) {
                                    Log.e("CEKRESI_LOG", "Error creating timeline item: " + e.getMessage());
                                }
                            }
                        });

                        // Save to history
                        String statusPaket = summary.optString("status", "-");
                        String deskripsi = "";
                        if (historyArray.length() > 0) {
                            JSONObject latestHistory = historyArray.getJSONObject(0);
                            deskripsi = latestHistory.optString("desc", statusPaket);
                        } else {
                            deskripsi = statusPaket;
                        }

                        RiwayatActivity.tambahRiwayat(MainActivity.this, resi, kurir.toUpperCase(),
                                statusPaket, deskripsi);

                    } else {
                        runOnUiThread(() -> {
                            String errorMessage = "⚠ Gagal: " + message;
                            txtHasilDefault.setText(errorMessage);
                            txtHasilDefault.setVisibility(View.VISIBLE);
                            Snackbar.make(findViewById(android.R.id.content), errorMessage, Snackbar.LENGTH_LONG)
                                    .setBackgroundTint(getResources().getColor(R.color.md_theme_error))
                                    .setTextColor(getResources().getColor(R.color.md_theme_onError))
                                    .show();
                        });
                        RiwayatActivity.tambahRiwayat(MainActivity.this, resi, kurir,
                                "GAGAL", message);
                    }

                } catch (Exception e) {
                    runOnUiThread(() -> {
                        String parseErrorMessage = "⚠ Parsing gagal: " + e.getMessage();
                        txtHasilDefault.setText(parseErrorMessage);
                        txtHasilDefault.setVisibility(View.VISIBLE);
                        Snackbar.make(findViewById(android.R.id.content), parseErrorMessage, Snackbar.LENGTH_LONG)
                                .setBackgroundTint(getResources().getColor(R.color.md_theme_error))
                                .setTextColor(getResources().getColor(R.color.md_theme_onError))
                                .show();
                    });
                    RiwayatActivity.tambahRiwayat(MainActivity.this, resi, kurir,
                            "GAGAL", "Parsing gagal: " + e.getMessage());
                }
            }
        });
    }

    private String cekKosong(String teks) {
        return (teks == null || teks.trim().isEmpty() || teks.trim().equalsIgnoreCase("null")) ? "-" : teks;
    }
};