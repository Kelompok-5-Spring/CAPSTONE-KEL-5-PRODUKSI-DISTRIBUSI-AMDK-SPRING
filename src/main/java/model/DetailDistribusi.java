/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import java.time.LocalDate;
import model.type.JenisProduk;

// Rincian jumlah pesanan setiap Agen pada satu transaksi Distribusi.
public final class DetailDistribusi {
    private String idDetailDistribusi;
    private final String idAgen;
    private String idDistribusi;
    private final JenisProduk jenisProduk;
    private int jumlahPesan;
    private LocalDate tanggalPesan;

    // Constructor untuk data pesanan sementara sebelum ID dibuat otomatis.
    public DetailDistribusi(String idAgen, JenisProduk jenisProduk,
            int jumlahPesan, LocalDate tanggalPesan) {
        this(null, idAgen, null, jenisProduk, jumlahPesan, tanggalPesan);
    }

    // Constructor untuk data yang sudah tersimpan di database.
    public DetailDistribusi(String idDetailDistribusi, String idAgen, String idDistribusi,
            JenisProduk jenisProduk, int jumlahPesan, LocalDate tanggalPesan) {
        this.idDetailDistribusi = idDetailDistribusi;
        this.idAgen = teksWajib(idAgen, "ID agen");
        this.idDistribusi = idDistribusi;
        if (jenisProduk == null) {
            throw new IllegalArgumentException("Jenis produk wajib dipilih.");
        }
        this.jenisProduk = jenisProduk;
        if (jumlahPesan <= 0) throw new IllegalArgumentException("Jumlah pesan harus lebih besar dari 0.");
        this.jumlahPesan = jumlahPesan;
        if (tanggalPesan == null) throw new IllegalArgumentException("Tanggal pesan wajib diisi.");
        this.tanggalPesan = tanggalPesan;
    }

    public String getIdDetailDistribusi(){ 
        return idDetailDistribusi; }
    
    public String getIdAgen(){ 
        return idAgen; }
    
    public String getIdDistribusi(){ 
        return idDistribusi; }
    
    public JenisProduk getJenisProduk(){ 
        return jenisProduk; }
    
    public int getJumlahPesan(){ 
        return jumlahPesan; }
    
    public LocalDate getTanggalPesan(){ 
        return tanggalPesan; }

    public final void setIdDetailDistribusi(String id) {
        idDetailDistribusi = teksWajib(id, "ID detail distribusi");
    }

    public final void setIdDistribusi(String id) {
        idDistribusi = teksWajib(id, "ID distribusi");
    }

    public final void setJumlahPesan(int jumlahPesan) {
        if (jumlahPesan <= 0) {
            throw new IllegalArgumentException("Jumlah pesan harus lebih besar dari 0.");
        }
        this.jumlahPesan = jumlahPesan;
    }

    public final void setTanggalPesan(LocalDate tanggalPesan) {
        if (tanggalPesan == null) {
            throw new IllegalArgumentException("Tanggal pesan wajib diisi.");
        }
        this.tanggalPesan = tanggalPesan;
    }

    private static String teksWajib(String nilai, String namaKolom) {
        if (nilai == null || nilai.trim().isEmpty()) {
            throw new IllegalArgumentException(namaKolom + " wajib diisi.");
        }
        return nilai.trim();
    }
}
