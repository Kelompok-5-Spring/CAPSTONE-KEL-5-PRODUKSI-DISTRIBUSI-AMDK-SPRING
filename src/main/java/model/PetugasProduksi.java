/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import java.util.Arrays;
import java.util.List;
import model.type.Role;

public final class PetugasProduksi extends Petugas {
    private String spesialisasiProses;

    public PetugasProduksi(String id, String username, String nama, String hash,
            String spesialisasiProses) {
        super(id, username, nama, hash, Role.Produksi);
        setSpesialisasiProses(spesialisasiProses);
    }

    public String getSpesialisasiProses(){ 
        return spesialisasiProses; }

    public final void setSpesialisasiProses(String nilai) {
        String pilihan = teksWajib(nilai, "Spesialisasi proses");
        if (!pilihan.equals("Pengisian") && !pilihan.equals("Pengemasan")
                && !pilihan.equals("Quality Check")) {
            throw new IllegalArgumentException("Spesialisasi produksi harus Pengisian, Pengemasan, atau Quality Check.");
        }
        spesialisasiProses = pilihan;
    }

    @Override
    public List<String> getMenuTersedia() {
        return Arrays.asList("Produksi", "Laporan");
    }

    @Override
    public String getInfoPetugas() {
        return super.getInfoPetugas() + " | " + spesialisasiProses;
    }
}
