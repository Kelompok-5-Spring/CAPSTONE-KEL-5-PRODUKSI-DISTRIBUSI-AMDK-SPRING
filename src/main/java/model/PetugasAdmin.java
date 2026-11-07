/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import java.util.Arrays;
import java.util.List;
import model.type.Role;

public final class PetugasAdmin extends Petugas {
    private String spesialisasiAdmin;

    public PetugasAdmin(String id, String username, String nama, String hash,
            String spesialisasiAdmin) {
        super(id, username, nama, hash, Role.Admin);
        setSpesialisasiAdmin(spesialisasiAdmin);
    }

    public String getSpesialisasiAdmin(){ 
        return spesialisasiAdmin; }

    public final void setSpesialisasiAdmin(String nilai) {
        String pilihan = teksWajib(nilai, "Spesialisasi admin");
        if (!pilihan.equals("Pengawas") && !pilihan.equals("Manajemen")) {
            throw new IllegalArgumentException("Spesialisasi admin harus Pengawas atau Manajemen.");
        }
        spesialisasiAdmin = pilihan;
    }

    @Override
    public List<String> getMenuTersedia() {
        return Arrays.asList("Akun", "Agen", "Produk", "Produksi", "Distribusi", "Laporan");
    }

    @Override
    public String getInfoPetugas() {
        return super.getInfoPetugas() + " | " + spesialisasiAdmin;
    }
}
