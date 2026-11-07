/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import dao.PetugasDAO;
import dao.ProduksiDAO;
import model.Petugas;
import model.Produksi;
import model.type.Role;
import model.type.StatusProduksi;

// Mengelola transaksi produksi dan validasi petugas produksi.
public class ProduksiController {
    private final ProduksiDAO dao;
    private final PetugasDAO petugasDAO;
    private final AkunController akun;

    public ProduksiController(ProduksiDAO dao, PetugasDAO petugasDAO, AkunController akun) {
        this.dao = dao;
        this.petugasDAO = petugasDAO;
        this.akun = akun;
    }

    public List<Produksi> loadProduksi() {
        akun.wajibRole(Role.Admin, Role.Produksi, Role.Gudang);
        List<Produksi> semua = dao.findAll();
        if (akun.getPetugasAktif().getRole() != Role.Produksi) return semua;
        List<Produksi> hasil = new ArrayList<>();
        for (Produksi produksi : semua) {
            if (produksi.getIdPetugasProduksi().equals(akun.getPetugasAktif().getIdPetugas())) {
                hasil.add(produksi);
            }
        }
        return hasil;
    }

    public Produksi cariProduksi(String id) {
        akun.wajibRole(Role.Admin, Role.Produksi, Role.Gudang);
        Produksi produksi = dao.findById(validasiKode(id, "ID produksi", 10));
        if (produksi != null) periksaKepemilikan(produksi);
        return produksi;
    }

    public Produksi simpanProduksi(String jenis, LocalDate tanggal,
            String jumlah, String idPetugasProduksi) {
        akun.wajibRole(Role.Admin, Role.Produksi);
        String jenisValid = validasiJenisProduksi(jenis);
        LocalDate tanggalValid = tanggalWajib(tanggal, "Tanggal produksi");
        int jumlahValid = parseIntegerPositif(jumlah, "Jumlah produksi");
        String idPetugas = validasiKode(idPetugasProduksi, "ID petugas produksi", 10);
        periksaPetugasProduksi(idPetugas);
        periksaPetugasAktif(idPetugas);

        Produksi produksi = new Produksi(dao.generateIdBaru(), jenisValid, tanggalValid,
                StatusProduksi.Direncanakan, jumlahValid, idPetugas);
        dao.insert(produksi);
        return produksi;
    }

    public void ubahProduksi(String id, String jenis, LocalDate tanggal,
            String jumlah, String idPetugasProduksi) {
        akun.wajibRole(Role.Admin, Role.Produksi);
        String idValid = validasiKode(id, "ID produksi", 10);
        Produksi lama = dao.findById(idValid);
        if (lama == null) throw new IllegalArgumentException("Produksi tidak ditemukan.");
        periksaKepemilikan(lama);
        if (lama.getStatusProduksi() == StatusProduksi.Selesai
                || lama.getStatusProduksi() == StatusProduksi.Dibatalkan) {
            throw new IllegalArgumentException("Produksi dengan status final tidak dapat diubah.");
        }

        String jenisValid = validasiJenisProduksi(jenis);
        LocalDate tanggalValid = tanggalWajib(tanggal, "Tanggal produksi");
        int jumlahValid = parseIntegerPositif(jumlah, "Jumlah produksi");
        String idPetugas = validasiKode(idPetugasProduksi, "ID petugas produksi", 10);
        periksaPetugasProduksi(idPetugas);
        periksaPetugasAktif(idPetugas);

        Produksi baru = new Produksi(idValid, jenisValid, tanggalValid,
                lama.getStatusProduksi(), jumlahValid, idPetugas);
        dao.update(baru);
    }

    public void ubahStatusProduksi(String id, StatusProduksi statusBaru) {
        akun.wajibRole(Role.Admin, Role.Produksi);
        String idValid = validasiKode(id, "ID produksi", 10);
        Produksi produksi = dao.findById(idValid);
        if (produksi == null) throw new IllegalArgumentException("Produksi tidak ditemukan.");
        periksaKepemilikan(produksi);
        if (statusBaru == null) throw new IllegalArgumentException("Status produksi wajib dipilih.");
        dao.ubahStatusDanBuatProduk(idValid, statusBaru);
    }

    public void hapusProduksi(String id) {
        akun.wajibRole(Role.Admin);
        dao.delete(validasiKode(id, "ID produksi", 10));
    }

    public List<Produksi> getProduksiByTanggal(LocalDate awal, LocalDate akhir) {
        akun.wajibRole(Role.Admin, Role.Produksi, Role.Gudang);
        validasiRentangTanggal(awal, akhir);
        List<Produksi> hasil = new ArrayList<>();
        for (Produksi produksi : loadProduksi()) {
            if (!produksi.getTanggalProduksi().isBefore(awal)
                    && !produksi.getTanggalProduksi().isAfter(akhir)) {
                hasil.add(produksi);
            }
        }
        return hasil;
    }

    private void periksaPetugasProduksi(String id) {
        Petugas petugas = petugasDAO.findById(id);
        if (petugas == null || petugas.getRole() != Role.Produksi) {
            throw new IllegalArgumentException("ID harus milik petugas Produksi yang terdaftar.");
        }
    }

    private void periksaPetugasAktif(String idPetugas) {
        if (akun.getPetugasAktif().getRole() == Role.Produksi
                && !akun.getPetugasAktif().getIdPetugas().equals(idPetugas)) {
            throw new SecurityException("Petugas Produksi hanya dapat mengelola produksi atas namanya sendiri.");
        }
    }

    private void periksaKepemilikan(Produksi produksi) {
        if (akun.getPetugasAktif().getRole() == Role.Produksi
                && !produksi.getIdPetugasProduksi().equals(akun.getPetugasAktif().getIdPetugas())) {
            throw new SecurityException("Produksi ini bukan tanggung jawab Anda.");
        }
    }

    private String validasiJenisProduksi(String jenis) {
        String hasil = wajibIsi(jenis, "Jenis produksi");
        if (!hasil.equals("Spring Botol 600ml") && !hasil.equals("Spring Botol 1500ml")
                && !hasil.equals("Spring Galon 5L") && !hasil.equals("Spring Galon 19L")) {
            throw new IllegalArgumentException("Pilih salah satu dari empat jenis produk Spring.");
        }
        return hasil;
    }

    private int parseIntegerPositif(String nilai, String kolom) {
        return ValidasiInput.parseIntegerPositif(nilai, kolom);
    }

    private LocalDate tanggalWajib(LocalDate tanggal, String kolom) {
        return ValidasiInput.tanggalWajib(tanggal, kolom);
    }

    private void validasiRentangTanggal(LocalDate awal, LocalDate akhir) {
        ValidasiInput.validasiRentangTanggal(awal, akhir);
    }

    private String validasiKode(String nilai, String kolom, int maksimum) {
        return ValidasiInput.kodeIdentitas(nilai, kolom, maksimum);
    }

    private String wajibIsi(String nilai, String kolom) {
        return ValidasiInput.wajibIsi(nilai, kolom);
    }
}