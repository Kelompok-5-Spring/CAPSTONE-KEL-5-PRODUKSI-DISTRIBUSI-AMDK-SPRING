# SPRING — Integrasi View, Controller, dan Role

## Lingkungan
- JDK 21
- Apache Maven 3.9+
- MySQL 8.x
- Database: `db_amdk_spring`

## Menjalankan aplikasi
1. Pastikan MySQL aktif dan database `db_amdk_spring` beserta tabel-tabel project sudah dibuat.
2. Import SQL schema/dump project ke database tersebut. Arsip sumber ini tidak menyertakan dump SQL.
3. Buka folder `spring` sebagai Maven Project di NetBeans atau IntelliJ IDEA.
4. Pastikan IDE menggunakan JDK 21.
5. Jalankan `mvn clean package`.
6. Jalankan `mvn exec:java` atau class `main.MainApp` dari IDE.

## Konfigurasi database
Aplikasi membaca konfigurasi dari environment variable atau `src/main/resources/database.properties`.

Environment variable:
- `SPRING_DB_URL`
- `SPRING_DB_USER`
- `SPRING_DB_PASSWORD`

Default lokal:
- URL: `jdbc:mysql://localhost:3306/db_amdk_spring`
- Username: `root`
- Password: kosong

Jangan menyimpan password database produksi di source code atau membagikan kredensial.

## Integrasi fitur dan hak akses
- **Admin:** dashboard admin, data agen, data produk, manajemen produksi, distribusi, stok gudang, manajemen akun, dan pantau laporan.
- **Produksi:** dashboard produksi, pencatatan dan perubahan status produksi, data produk untuk referensi, serta laporan.
- **Gudang:** dashboard gudang, data agen, data produk, pemantauan stok/mutasi, distribusi sesuai izin controller, serta laporan.
- **Distribusi:** dashboard distribusi, daftar agen sesuai wilayah petugas, pembaruan status pengiriman, data produk, serta laporan sesuai izin controller.
- Form agen aktif menggunakan `AgenPanel`; Admin dapat CRUD, sementara Gudang dan Distribusi hanya dapat melihat data sesuai wilayah/hak akses.
- Filter agen mendukung nama dan kecamatan.
- Grafik dashboard Admin membandingkan produksi, pengiriman, dan stok untuk tiap jenis produk. Dashboard Produksi menampilkan proporsi hasil selesai dengan progress bar yang dihitung dari data produksi.
- Status `Selesai` pada produksi membentuk record produk sesuai alur controller/DAO.
- Batas minimum stok tidak ditampilkan sebagai angka karena skema database yang tersedia tidak menyimpan nilai batas minimum.
- Form lama `Admin*` yang merupakan tampilan duplikat diarahkan ke halaman aktif agar tidak ada dua implementasi transaksi yang berbeda.

## NetBeans GUI Builder
File `.form` dan blok `initComponents()` dari tampilan drag-and-drop dipertahankan. Integrasi event/controller berada di constructor dan method di luar blok kode GUI Builder. Hindari mengedit blok `initComponents()` secara manual agar desain tetap bisa dibuka di NetBeans.

## Status pemeriksaan
Seluruh file Java berhasil dikompilasi dengan JDK 21. Pengujian transaksi langsung belum dapat diklaim karena dump/schema database dan server MySQL tidak disertakan pada arsip sumber.
