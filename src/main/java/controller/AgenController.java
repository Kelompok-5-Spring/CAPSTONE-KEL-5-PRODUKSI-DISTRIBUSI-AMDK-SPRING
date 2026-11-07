/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import dao.AgenDAO;
import model.Agen;
import model.Petugas;
import model.PetugasDistribusi;
import model.type.Role;

// Mengelola data Agen, validasi, pencarian, dan pembatasan wilayah petugas distribusi.
public class AgenController {
    private final AgenDAO dao;
    private final AkunController akun;

    public AgenController(AgenDAO dao, AkunController akun) {
        this.dao = dao;
        this.akun = akun;
    }

    public List<Agen> loadAgen() {
        akun.wajibRole(Role.Admin, Role.Gudang, Role.Distribusi);
        List<Agen> semua = dao.findAll();
        Petugas aktif = akun.getPetugasAktif();
        if (aktif.getRole() != Role.Distribusi) return semua;

        List<Agen> hasil = new ArrayList<>();
        PetugasDistribusi petugas = (PetugasDistribusi) aktif;
        for (Agen agen : semua) {
            if (petugas.sesuaiWilayah(agen)) hasil.add(agen);
        }
        return hasil;
    }

    public Agen cariAgen(String id) {
        akun.wajibRole(Role.Admin, Role.Gudang, Role.Distribusi);
        Agen agen = dao.findById(validasiKode(id, "ID agen", 10));
        if (agen != null && akun.getPetugasAktif().getRole() == Role.Distribusi) {
            PetugasDistribusi petugas = (PetugasDistribusi) akun.getPetugasAktif();
            if (!petugas.sesuaiWilayah(agen)) {
                throw new SecurityException("Agen berada di luar wilayah tugas petugas.");
            }
        }
        return agen;
    }

    // Overloading: pencarian berdasarkan nama dan kecamatan.
    public List<Agen> cariAgen(String nama, String kecamatan) {
        String namaCari = wajibIsi(nama, "Nama agen").toLowerCase(Locale.ROOT);
        String wilayahCari = wajibIsi(kecamatan, "Kecamatan");
        List<Agen> hasil = new ArrayList<>();
        for (Agen agen : loadAgen()) {
            if (agen.getNamaAgen().toLowerCase(Locale.ROOT).contains(namaCari)
                    && agen.ambilKecamatan() != null
                    && agen.ambilKecamatan().equalsIgnoreCase(wilayahCari)) {
                hasil.add(agen);
            }
        }
        return hasil;
    }

    public Agen simpanAgen(String nama, String nomorTelepon, String alamatLengkap) {
        akun.wajibRole(Role.Admin);
        String namaValid = validasiTeks(nama, "Nama agen", 2, 100);
        String telepon = validasiNomorTelepon(nomorTelepon);
        String alamat = validasiAlamat(alamatLengkap);
        if (dao.nomorTeleponDipakai(telepon, null)) {
            throw new IllegalArgumentException("Nomor telepon sudah digunakan agen lain.");
        }
        Agen agen = new Agen(dao.generateIdBaru(), namaValid, telepon, alamat);
        dao.insert(agen);
        return agen;
    }

    public void ubahAgen(String id, String nama, String nomorTelepon, String alamatLengkap) {
        akun.wajibRole(Role.Admin);
        String idValid = validasiKode(id, "ID agen", 10);
        if (dao.findById(idValid) == null) throw new IllegalArgumentException("Agen tidak ditemukan.");
        String namaValid = validasiTeks(nama, "Nama agen", 2, 100);
        String telepon = validasiNomorTelepon(nomorTelepon);
        String alamat = validasiAlamat(alamatLengkap);
        if (dao.nomorTeleponDipakai(telepon, idValid)) {
            throw new IllegalArgumentException("Nomor telepon sudah digunakan agen lain.");
        }
        dao.update(new Agen(idValid, namaValid, telepon, alamat));
    }

    public void hapusAgen(String id) {
        akun.wajibRole(Role.Admin);
        dao.delete(validasiKode(id, "ID agen", 10));
    }

    private String validasiNomorTelepon(String nilai) {
        return ValidasiInput.nomorTeleponIndonesia(nilai);
    }

    private String validasiAlamat(String nilai) {
        String hasil = ValidasiInput.alamat(nilai);
        String alamatKecil = hasil.toLowerCase(Locale.ROOT);
        boolean adaKecamatan = alamatKecil.contains("palaran")
                || alamatKecil.contains("samarinda ilir")
                || alamatKecil.contains("samarinda kota")
                || alamatKecil.contains("samarinda utara")
                || alamatKecil.contains("samarinda ulu")
                || alamatKecil.contains("sungai kunjang");
        if (!adaKecamatan) {
            throw new IllegalArgumentException("Alamat harus mencantumkan salah satu kecamatan Samarinda yang terdaftar.");
        }
        return hasil;
    }

    private String validasiTeks(String nilai, String kolom, int minimum, int maksimum) {
        return ValidasiInput.teksDenganBatas(nilai, kolom, minimum, maksimum);
    }

    private String validasiKode(String nilai, String kolom, int maksimum) {
        return ValidasiInput.kodeIdentitas(nilai, kolom, maksimum);
    }

    private String wajibIsi(String nilai, String kolom) {
        return ValidasiInput.wajibIsi(nilai, kolom);
    }
}