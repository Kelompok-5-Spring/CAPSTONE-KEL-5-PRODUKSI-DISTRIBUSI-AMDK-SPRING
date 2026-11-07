/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import java.math.BigDecimal;
import model.type.JenisProduk;

// Superclass produk. Satu objek mewakili satu kemasan jadi.
public abstract class Produk {
    private final String idProduk;
    private String namaProduk;
    private BigDecimal hargaSatuan;
    private final String idProduksi;
    private String idDistribusi;

    protected Produk(String idProduk, String namaProduk, BigDecimal hargaSatuan,
            String idProduksi, String idDistribusi) {
        this.idProduk = teksWajib(idProduk, "ID produk");
        this.namaProduk = teksWajib(namaProduk, "Nama produk");
        if (hargaSatuan == null || hargaSatuan.signum() < 0) {
            throw new IllegalArgumentException("Harga satuan tidak boleh kosong atau negatif.");
        }
        this.hargaSatuan = hargaSatuan;
        this.idProduksi = teksWajib(idProduksi, "ID produksi");
        this.idDistribusi = (idDistribusi == null || idDistribusi.trim().isEmpty()) ? null : idDistribusi.trim();
    }

    public String getIdProduk(){ 
        return idProduk; }
    
    public String getNamaProduk(){ 
        return namaProduk; }
    
    public BigDecimal getHargaSatuan(){ 
        return hargaSatuan; }
    
    public String getIdProduksi(){ 
        return idProduksi; }
    
    public String getIdDistribusi(){
        return idDistribusi; }

    public final void setNamaProduk(String namaProduk) {
        this.namaProduk = teksWajib(namaProduk, "Nama produk");
    }

    public final void setHargaSatuan(BigDecimal hargaSatuan) {
        if (hargaSatuan == null || hargaSatuan.signum() < 0) {
            throw new IllegalArgumentException("Harga satuan tidak boleh kosong atau negatif.");
        }
        this.hargaSatuan = hargaSatuan;
    }

    public final void setIdDistribusi(String idDistribusi) {
        if (idDistribusi == null || idDistribusi.trim().isEmpty()) {
            this.idDistribusi = null;
        } else {
            this.idDistribusi = idDistribusi.trim();
        }
    }

    public abstract String getDeskripsiKemasan();
    public abstract JenisProduk getJenisProduk();
    public abstract String getSatuanJual();

    public BigDecimal hitungHarga(int jumlah) {
        if (jumlah <= 0) {
            throw new IllegalArgumentException("Jumlah harus lebih besar dari 0.");
        }
        return hargaSatuan.multiply(BigDecimal.valueOf(jumlah));
    }

    private static String teksWajib(String nilai, String namaKolom) {
        if (nilai == null || nilai.trim().isEmpty()) {
            throw new IllegalArgumentException(namaKolom + " wajib diisi.");
        }
        return nilai.trim();
    }

    @Override
    public String toString() { return idProduk + " - " + namaProduk; }
}
