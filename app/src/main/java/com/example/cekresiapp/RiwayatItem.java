package com.example.cekresiapp;

public class RiwayatItem {
    private String resi;
    private String kurir;
    private String status;
    private String deskripsi;
    private String tanggal;

    // Constructor kosong
    public RiwayatItem() {}

    // Constructor dengan parameter
    public RiwayatItem(String resi, String kurir, String status, String deskripsi, String tanggal) {
        this.resi = resi;
        this.kurir = kurir;
        this.status = status;
        this.deskripsi = deskripsi;
        this.tanggal = tanggal;
    }

    // Getter methods
    public String getResi() {
        return resi;
    }

    public String getKurir() {
        return kurir;
    }

    public String getStatus() {
        return status;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public String getTanggal() {
        return tanggal;
    }

    // Setter methods
    public void setResi(String resi) {
        this.resi = resi;
    }

    public void setKurir(String kurir) {
        this.kurir = kurir;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    // Method untuk mendapatkan warna status
    public int getStatusColor() {
        if (status == null) return android.R.color.darker_gray;

        String statusLower = status.toLowerCase();

        // Status terkirim/selesai - HIJAU
        if (statusLower.contains("terkirim") ||
                statusLower.contains("delivered") ||
                statusLower.contains("selesai") ||
                statusLower.contains("completed")) {
            return android.R.color.holo_green_dark;
        }
        // Status dalam perjalanan/transit - ORANGE
        else if (statusLower.contains("perjalanan") ||
                statusLower.contains("transit") ||
                statusLower.contains("on the way") ||
                statusLower.contains("shipment") ||
                statusLower.contains("dalam perjalanan") ||
                statusLower.contains("sedang dikirim") ||
                statusLower.contains("dalam pengiriman")) {
            return android.R.color.holo_orange_dark;
        }
        // Status dalam proses - BIRU
        else if (statusLower.contains("proses") ||
                statusLower.contains("processing") ||
                statusLower.contains("on proccess") ||
                statusLower.contains("diproses") ||
                statusLower.contains("sedang diproses") ||
                statusLower.contains("pickup") ||
                statusLower.contains("picked up")) {
            return android.R.color.holo_orange_dark;
        }
        // Status gagal/error - MERAH
        else if (statusLower.contains("gagal") ||
                statusLower.contains("failed") ||
                statusLower.contains("error") ||
                statusLower.contains("dibatalkan") ||
                statusLower.contains("cancelled")) {
            return android.R.color.holo_red_dark;
        }
        // Default - ABU-ABU
        else {
            return android.R.color.darker_gray;
        }
    }

    // Method untuk mendapatkan icon status
    public int getStatusIcon() {
        if (status == null) return R.drawable.ic_package;

        String statusLower = status.toLowerCase();

        // Status terkirim/selesai - CHECK CIRCLE
        if (statusLower.contains("terkirim") ||
                statusLower.contains("delivered") ||
                statusLower.contains("selesai") ||
                statusLower.contains("completed")) {
            return R.drawable.ic_check_circle;
        }
        // Status dalam perjalanan/transit - TRUCK ICON
        else if (statusLower.contains("perjalanan") ||
                statusLower.contains("transit") ||
                statusLower.contains("on the way") ||
                statusLower.contains("shipment") ||
                statusLower.contains("dalam perjalanan") ||
                statusLower.contains("sedang dikirim") ||
                statusLower.contains("dalam pengiriman")) {
            return R.drawable.baseline_local_shipping_24;
        }
        // Status dalam proses - PACKAGE ICON
        else if (statusLower.contains("proses") ||
                statusLower.contains("processing") ||
                statusLower.contains("on proccess") ||
                statusLower.contains("diproses") ||
                statusLower.contains("sedang diproses") ||
                statusLower.contains("pickup") ||
                statusLower.contains("picked up")) {
            return R.drawable.ic_courier;
        }
        // Status gagal/error - ERROR ICON (jika ada)
        else if (statusLower.contains("gagal") ||
                statusLower.contains("failed") ||
                statusLower.contains("error") ||
                statusLower.contains("dibatalkan") ||
                statusLower.contains("cancelled")) {
            return R.drawable.ic_error;
        }
        // Default - TRACK PACKAGE
        else {
            return R.drawable.ic_track_package;
        }
    }

    // Method BARU untuk mendapatkan warna icon (sama dengan status color)
    public int getIconColor() {
        return getStatusColor();
    }

    // Method untuk mendapatkan background color kurir
    public int getKurirBackgroundColor() {
        if (kurir == null) return R.color.gray_light;

        switch (kurir.toUpperCase()) {
            case "JNE":
                return R.color.blue_light;
            case "J&T":
            case "JNT":
                return R.color.green_light;
            case "SICEPAT":
                return R.color.purple_light;
            case "POS":
                return R.color.orange_light;
            case "TIKI":
                return R.color.red_light;
            case "ANTERAJA":
                return R.color.cyan_light;
            case "NINJA":
                return R.color.indigo_light;
            case "RPX":
                return R.color.brown_light;
            case "SPX":
                return R.color.orange_light;
            default:
                return R.color.gray_light;
        }
    }

    // Method untuk mendapatkan text color kurir
    public int getKurirTextColor() {
        if (kurir == null) return R.color.gray_dark;

        switch (kurir.toUpperCase()) {
            case "JNE":
                return R.color.blue_dark;
            case "J&T":
            case "JNT":
                return R.color.green_dark;
            case "SICEPAT":
                return R.color.purple_dark;
            case "POS":
                return R.color.orange_dark;
            case "TIKI":
                return R.color.red_dark;
            case "ANTERAJA":
                return R.color.cyan_dark;
            case "NINJA":
                return R.color.indigo_dark;
            case "RPX":
                return R.color.brown_dark;
            case "SPX":
                return R.color.orange_dark;
            default:
                return R.color.gray_dark;
        }
    }
}