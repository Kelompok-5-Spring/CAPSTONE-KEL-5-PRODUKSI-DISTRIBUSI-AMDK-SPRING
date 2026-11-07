/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import java.time.LocalDate;
import model.type.StatusProduksi;

// Transaksi produksi. Saat status menjadi Selesai, DAO membentuk record Produk.
public final class Produksi {
    private final String idProduksi;
    private String jenisProduksi;
    private LocalDate tanggalProduksi;
    private StatusProduksi statusProduksi;
    private int jumlahProduksi;
    private String idPetugasProduksi;

    public Produksi(String idProduksi, String jenisProduksi, LocalDate tanggalProduksi,
            StatusProduksi statusProduksi, int jumlahProduksi, String idPetugasProduksi) {
        this.idProduksi = teksWajib(idProduksi, "ID produksi");
        this.jenisProduksi = validasiJenisProduksi(jenisProduksi);
        this.tanggalProduksi = tanggalWajib(tanggalProduksi);
        if (statusProduksi == null) {
            throw new IllegalArgumentException("Status produksi wajib dipilih.");
        }
        this.statusProduksi = statusProduksi;
        this.jumlahProduksi = positif(jumlahProduksi);
        this.idPetugasProduksi = teksWajib(idPetugasProduksi, "ID petugas produksi");
    }

    public String getIdProduksi(){ 
        return idProduksi; }
    
    public String getJenisProduksi(){ 
        return jenisProduksi; }
    
    public LocalDate getTanggalProduksi(){ 
        return tanggalProduksi; }
    
    public StatusProduksi getStatusProduksi(){ 
        return statusProduksi; }
    
    public int getJumlahProduksi(){ 
        return jumlahProduksi; }
    
    public String getIdPetugasProduksi(){ 
        return idPetugasProduksi; }

    public final void setJenisProduksi(String jenisProduksi) {
        this.jenisProduksi = validasiJenisProduksi(jenisProduksi);
    }

    public final void setTanggalProduksi(LocalDate tanggalProduksi) {
        if (tanggalProduksi == null) {
            throw new IllegalArgumentException("Tanggal produksi wajib diisi.");
        }
        this.tanggalProduksi = tanggalProduksi;
    }

    public final void setJumlahProduksi(int jumlahProduksi) {
        if (jumlahProduksi <= 0) {
            throw new IllegalArgumentException("Jumlah produksi harus lebih besar dari 0.");
        }
        this.jumlahProduksi = jumlahProduksi;
    }

    public final void setIdPetugasProduksi(String idPetugasProduksi) {
        this.idPetugasProduksi = teksWajib(idPetugasProduksi, "ID petugas produksi");
    }

    // Memastikan status produksi tidak melompati tahapan.
    public void ubahStatus(StatusProduksi statusBaru) {
        if (statusProduksi == StatusProduksi.Direncanakan
                && (statusBaru == StatusProduksi.Diproses || statusBaru == StatusProduksi.Dibatalkan)) {
            statusProduksi = statusBaru;
            return;
        }
        if (statusProduksi == StatusProduksi.Diproses
                && (statusBaru == StatusProduksi.Selesai || statusBaru == StatusProduksi.Dibatalkan)) {
            statusProduksi = statusBaru;
            return;
        }
        throw new IllegalArgumentException("Perubahan status produksi tidak sesuai urutan.");
    }

    private static String validasiJenisProduksi(String jenisProduksi) {
        String nilai = teksWajib(jenisProduksi, "Jenis produksi");
        if (!nilai.equals("Spring Botol 600ml") && !nilai.equals("Spring Botol 1500ml")
                && !nilai.equals("Spring Galon 5L") && !nilai.equals("Spring Galon 19L")) {
            throw new IllegalArgumentException("Pilih salah satu dari empat jenis produk Spring.");
        }
        return nilai;
    }

    private static LocalDate tanggalWajib(LocalDate tanggal) {
        if (tanggal == null) throw new IllegalArgumentException("Tanggal produksi wajib diisi.");
        return tanggal;
    }

    private static int positif(int angka) {
        if (angka <= 0) throw new IllegalArgumentException("Jumlah produksi harus lebih besar dari 0.");
        return angka;
    }

    private static String teksWajib(String nilai, String namaKolom) {
        if (nilai == null || nilai.trim().isEmpty()) {
            throw new IllegalArgumentException(namaKolom + " wajib diisi.");
        }
        return nilai.trim();
    }
}