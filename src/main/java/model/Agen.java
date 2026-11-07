/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import java.util.Locale;

// Data master agen. Kecamatan diperoleh dari alamat, bukan disimpan sebagai atribut baru.
public final class Agen {
    private final String idAgen;
    private String namaAgen;
    private String noTelp;
    private String alamatLengkap;

    public Agen(String idAgen, String namaAgen, String noTelp, String alamatLengkap) {
        this.idAgen = teksWajib(idAgen, "ID agen");
        this.namaAgen = teksWajib(namaAgen, "Nama agen");
        this.noTelp = teksWajib(noTelp, "Nomor telepon");
        this.alamatLengkap = teksWajib(alamatLengkap, "Alamat lengkap");
    }

    public String getIdAgen(){ 
        return idAgen; }
    
    public String getNamaAgen(){ 
        return namaAgen; }
    
    public String getNoTelp(){ 
        return noTelp; }
    
    public String getAlamatLengkap() { return alamatLengkap; }

    public final void setNamaAgen(String namaAgen) {
        this.namaAgen = teksWajib(namaAgen, "Nama agen");
    }

    public final void setNoTelp(String noTelp) {
        this.noTelp = teksWajib(noTelp, "Nomor telepon");
    }

    public final void setAlamatLengkap(String alamatLengkap) {
        this.alamatLengkap = teksWajib(alamatLengkap, "Alamat lengkap");
    }

    // Mengambil kecamatan dari bagian alamat yang memuat nama kecamatan.
    public String ambilKecamatan() {
        String alamat = alamatLengkap.toLowerCase(Locale.ROOT);
        if (alamat.contains("palaran")) return "Palaran";
        if (alamat.contains("samarinda ilir")) return "Samarinda Ilir";
        if (alamat.contains("samarinda kota")) return "Samarinda Kota";
        if (alamat.contains("samarinda utara")) return "Samarinda Utara";
        if (alamat.contains("samarinda ulu")) return "Samarinda Ulu";
        if (alamat.contains("sungai kunjang")) return "Sungai Kunjang";
        return null;
    }

    private static String teksWajib(String nilai, String namaKolom) {
        if (nilai == null || nilai.trim().isEmpty()) {
            throw new IllegalArgumentException(namaKolom + " wajib diisi.");
        }
        return nilai.trim();
    }

    @Override
    public String toString() { return idAgen + " - " + namaAgen; }
}
