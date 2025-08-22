# 📦 CeRes - Cek Resi Paket Indonesia

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-green.svg" alt="Platform">
  <img src="https://img.shields.io/badge/Language-Java-orange.svg" alt="Language">
  <img src="https://img.shields.io/badge/API-BinderByte-blue.svg" alt="API">
  <img src="https://img.shields.io/badge/License-MIT-yellow.svg" alt="License">
</p>

CeRes adalah aplikasi pelacakan paket multi-kurir Indonesia yang mendukung 8+ kurir populer (JNE, JNT, SiCepat, POS, AnterAja, TIKI, Ninja Express, SPX) dalam satu platform. Tampilan modern dengan timeline real-time yang mudah dipahami. Cocok untuk online seller, buyer, dan UMKM.

## 📱 Screenshots

| Home Screen | Tracking Result | Timeline View | History |
|-------------|----------------|---------------|---------|
| ![Home](screenshots/home.png) | ![Result](screenshots/result.png) | ![Timeline](screenshots/timeline.png) | ![History](screenshots/history.png) |

## ✨ Fitur

- 🚚 **Multi-Kurir Support**: JNE, JNT, SiCepat, POS Indonesia, AnterAja, TIKI, Ninja Express, Shopee Express
- 📊 **Modern UI**: Material Design 3 dengan card-based layout
- 🕐 **Real-Time Tracking**: Update status paket secara langsung
- 📋 **Timeline View**: Riwayat pengiriman dengan visualisasi timeline
- 💾 **Auto Save History**: Simpan otomatis riwayat pelacakan
- 🎨 **Dynamic Status**: Warna status yang berubah sesuai kondisi paket
- 📱 **Responsive Design**: Grid layout yang menyesuaikan layar
- 🔄 **Error Handling**: Notifikasi error yang user-friendly

## 🛠️ Teknologi

- **Language**: Java
- **Platform**: Android (API Level 21+)
- **UI Framework**: Material Design Components
- **HTTP Client**: OkHttp3
- **JSON Parser**: org.json
- **API**: BinderByte Tracking API

## 📋 Prerequisites

- Android Studio Arctic Fox atau lebih baru
- JDK 8 atau lebih baru
- Android SDK API Level 21+
- Koneksi internet untuk API calls

## 🚀 Instalasi

### 1. Clone Repository
```bash
git clone https://github.com/alii16/ceres.git
cd ceres-app
```

### 2. Buka di Android Studio
- Buka Android Studio
- Pilih **Open an Existing Project**
- Navigasi ke folder project dan klik **OK**
- Tunggu Gradle sync selesai

### 3. Setup API Key

#### 3.1. Dapatkan API Key dari BinderByte
1. Kunjungi [binderbyte.com](https://binderbyte.com)
2. Daftar akun baru atau login
3. Pilih **API Tracking** di dashboard
4. Copy API Key Anda

#### 3.2. Masukkan API Key ke Project
Buka file `MainActivity.java` dan ganti API Key:

```java
// Ganti dengan API Key Anda dari binderbyte.com
private final String API_KEY = "YOUR_API_KEY_HERE";
```

## 🔧 Konfigurasi

### API Rate Limits
BinderByte API memiliki batasan:
- **Free Plan**: 1000 requests/month
- **Pro Plan**: 10000 requests/month
- **Business Plan**: Unlimited requests

### Supported Couriers
```java
// Kode kurir yang didukung:
"jne"       // JNE
"jnt"       // J&T Express
"sicepat"   // SiCepat
"pos"       // POS Indonesia
"anteraja"  // AnterAja
"tiki"      // TIKI
"ninja"     // Ninja Express
"spx"       // Shopee Express
```

## 🚀 Build & Run

### Development Build
```bash
# Debug build
./gradlew assembleDebug

# Install ke device/emulator
./gradlew installDebug
```

### Production Build
```bash
# Release build
./gradlew assembleRelease

# Generate signed APK
./gradlew bundleRelease
```

## 📂 Struktur Project

```
app/
├── src/main/
│   ├── java/com/example/cekresiapp/
│   │   ├── MainActivity.java          # Main activity dengan tracking logic
│   │   ├── RiwayatActivity.java       # History activity
│   │   └── ProfileActivity.java       # Profile activity
│   ├── res/
│   │   ├── layout/                    # XML layouts
│   │   ├── drawable/                  # Icons dan backgrounds
│   │   ├── values/                    # Colors, strings, styles
│   │   └── menu/                      # Bottom navigation menu
│   └── AndroidManifest.xml
└── build.gradle
```

## 🐛 Troubleshooting

### Common Issues

**1. API Key Error**
```
Error: API key tidak valid
```
*Solution*: Pastikan API key dari binderbyte.com sudah benar dan aktif

**2. Network Error**
```
Error: Tidak ada koneksi internet
```
*Solution*: Periksa koneksi internet dan permission INTERNET di manifest

**3. Parsing Error**
```
Error: Parsing gagal
```
*Solution*: API response mungkin berubah format, periksa log untuk detail

**4. Resource Not Found**
```
Error: Resource drawable not found
```
*Solution*: Pastikan semua file drawable sudah dibuat di res/drawable/

## 📱 Testing

### Manual Testing
1. Test dengan nomor resi valid dari berbagai kurir
2. Test dengan nomor resi invalid
3. Test tanpa koneksi internet
4. Test dengan API key invalid

### Test Cases
- ✅ Input validation (resi kosong, kurir kosong)
- ✅ Network connectivity check
- ✅ API response handling
- ✅ UI state management
- ✅ History save/load functionality

## 🤝 Contributing

1. Fork repository ini
2. Buat feature branch (`git checkout -b feature/amazing-feature`)
3. Commit perubahan (`git commit -m 'Add amazing feature'`)
4. Push ke branch (`git push origin feature/amazing-feature`)
5. Buat Pull Request

## 📄 License

Distributed under the MIT License. See `LICENSE` for more information.

## 📞 Contact

- **Developer**: Your Name
- **Email**: your.email@example.com
- **Project Link**: [https://github.com/alii16/ceres](https://github.com/alii16/ceres)

## 🙏 Acknowledgments

- [BinderByte](https://binderbyte.com) untuk API tracking
- [Material Design](https://material.io/) untuk design guidelines
- [OkHttp](https://square.github.io/okhttp/) untuk HTTP client

---

<p align="center">Made with💡by Ali Polanunu</p>
