/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import java.util.Arrays;
import java.util.List;
import model.type.Role;

public final class PetugasGudang extends Petugas {
    private String spesialisasiTugas;

    public PetugasGudang(String id, String username, String nama, String hash,
            String spesialisasiTugas) {
        super(id, username, nama, hash, Role.Gudang);
        setSpesialisasiTugas(spesialisasiTugas);
    }

    public String getSpesialisasiTugas() { return spesialisasiTugas; }

    public final void setSpesialisasiTugas(String nilai) {
        String pilihan = teksWajib(nilai, "Spesialisasi tugas");
        if (!pilihan.equals("Inbound") && !pilihan.equals("Outbound")
                && !pilihan.equals("Stock Checker")) {
            throw new IllegalArgumentException("Spesialisasi gudang harus Inbound, Outbound, atau Stock Checker.");
        }
        spesialisasiTugas = pilihan;
    }

    @Override
    public List<String> getMenuTersedia() {
        return Arrays.asList("Agen", "Produk", "Produksi", "Distribusi", "Laporan");
    }

    @Override
    public String getInfoPetugas() {
        return super.getInfoPetugas() + " | " + spesialisasiTugas;
    }
}
