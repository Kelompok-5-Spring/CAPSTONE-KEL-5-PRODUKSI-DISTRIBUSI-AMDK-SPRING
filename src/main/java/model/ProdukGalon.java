/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import java.math.BigDecimal;
import model.type.JenisProduk;

// Subclass produk galon.
public final class ProdukGalon extends Produk {
    private BigDecimal volumeLiter;
    private String jenisGalon;

    public ProdukGalon(String id, String nama, BigDecimal harga, String idProduksi,
            String idDistribusi, BigDecimal volumeLiter, String jenisGalon) {
        super(id, nama, harga, idProduksi, idDistribusi);
        setVolumeLiter(volumeLiter);
        setJenisGalon(jenisGalon);
    }

    public BigDecimal getVolumeLiter(){ 
        return volumeLiter; }
    
    public String getJenisGalon(){ 
        return jenisGalon; }

    public final void setVolumeLiter(BigDecimal volumeLiter) {
        if (volumeLiter == null || (volumeLiter.compareTo(BigDecimal.valueOf(5)) != 0
                && volumeLiter.compareTo(BigDecimal.valueOf(19)) != 0)) {
            throw new IllegalArgumentException("Volume galon hanya 5 L atau 19 L.");
        }
        this.volumeLiter = volumeLiter;
    }

    public final void setJenisGalon(String jenisGalon) {
        if (jenisGalon == null || (!jenisGalon.equals("Isi Ulang")
                && !jenisGalon.equals("Sekali Pakai"))) {
            throw new IllegalArgumentException("Jenis galon harus Isi Ulang atau Sekali Pakai.");
        }
        this.jenisGalon = jenisGalon;
    }

    @Override
    public String getDeskripsiKemasan() {
        return volumeLiter.stripTrailingZeros().toPlainString() + " liter, " + jenisGalon;
    }

    @Override
    public JenisProduk getJenisProduk() {
        return volumeLiter.compareTo(BigDecimal.valueOf(5)) == 0
                ? JenisProduk.GALON_5L : JenisProduk.GALON_19L;
    }

    @Override
    public String getSatuanJual() { return "GALON"; }
}