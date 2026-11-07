/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import java.util.Arrays;
import java.util.List;
import model.type.Role;

public final class PetugasDistribusi extends Petugas {
    private String wilayahTugas;

    public PetugasDistribusi(String id, String username, String nama, String hash,
            String wilayahTugas) {
        super(id, username, nama, hash, Role.Distribusi);
        setWilayahTugas(wilayahTugas);
    }

    public String getWilayahTugas() { return wilayahTugas; }

    public final void setWilayahTugas(String nilai) {
        String pilihan = teksWajib(nilai, "Wilayah tugas");
        List<String> wilayah = Arrays.asList("Palaran", "Samarinda Ilir", "Samarinda Kota",
                "Samarinda Utara", "Samarinda Ulu", "Sungai Kunjang");
        if (!wilayah.contains(pilihan)) {
            throw new IllegalArgumentException("Wilayah tugas harus sesuai enam kecamatan Samarinda.");
        }
        wilayahTugas = pilihan;
    }

    public boolean sesuaiWilayah(Agen agen) {
        return agen != null && agen.ambilKecamatan() != null
                && wilayahTugas.equalsIgnoreCase(agen.ambilKecamatan());
    }

    @Override
    public List<String> getMenuTersedia() {
        return Arrays.asList("Agen", "Produk", "Distribusi", "Laporan");
    }

    @Override
    public String getInfoPetugas() {
        return super.getInfoPetugas() + " | " + wilayahTugas;
    }
}
