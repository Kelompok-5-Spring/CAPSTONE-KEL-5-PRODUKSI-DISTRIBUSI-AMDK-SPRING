/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import java.math.BigDecimal;
import model.type.JenisProduk;

// Subclass produk botol.
public final class ProdukBotol extends Produk {
    private BigDecimal volumeMl;
    private int pcsPerDus;

    public ProdukBotol(String id, String nama, BigDecimal harga, String idProduksi,
            String idDistribusi, BigDecimal volumeMl, int pcsPerDus) {
        super(id, nama, harga, idProduksi, idDistribusi);
        setVolumeMl(volumeMl);
        setPcsPerDus(pcsPerDus);
    }

    public BigDecimal getVolumeMl(){ 
        return volumeMl; }
    
    public int getPcsPerDus(){ 
        return pcsPerDus; }

    public final void setVolumeMl(BigDecimal volumeMl) {
        if (volumeMl == null || (volumeMl.compareTo(BigDecimal.valueOf(600)) != 0
                && volumeMl.compareTo(BigDecimal.valueOf(1500)) != 0)) {
            throw new IllegalArgumentException("Volume botol hanya 600 ml atau 1500 ml.");
        }
        this.volumeMl = volumeMl;
    }

    public final void setPcsPerDus(int pcsPerDus) {
        if (pcsPerDus <= 0) {
            throw new IllegalArgumentException("Pcs per dus harus lebih besar dari 0.");
        }
        this.pcsPerDus = pcsPerDus;
    }

    @Override
    public String getDeskripsiKemasan() {
        return volumeMl.stripTrailingZeros().toPlainString() + " ml, " + pcsPerDus + " pcs/dus";
    }

    @Override
    public JenisProduk getJenisProduk() {
        return volumeMl.compareTo(BigDecimal.valueOf(600)) == 0
                ? JenisProduk.BOTOL_600ML : JenisProduk.BOTOL_1500ML;
    }

    @Override
    public String getSatuanJual() { return "DUS"; }
}
