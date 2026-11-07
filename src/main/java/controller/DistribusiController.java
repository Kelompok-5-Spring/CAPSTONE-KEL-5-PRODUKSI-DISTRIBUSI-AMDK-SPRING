/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import dao.AgenDAO;
import dao.DistribusiDAO;
import dao.PetugasDAO;
import model.Agen;
import model.DetailDistribusi;
import model.Distribusi;
import model.Petugas;
import model.PetugasDistribusi;
import model.PetugasGudang;
import model.type.Role;
import model.type.StatusKirim;

// Mengelola transaksi distribusi, pemesanan multi-agen, dan penentuan petugas otomatis.
public class DistribusiController {
    private final DistribusiDAO dao;
    private final AgenDAO agenDAO;
    private final PetugasDAO petugasDAO;
    private final AkunController akun;

    public DistribusiController(DistribusiDAO dao, AgenDAO agenDAO,
            PetugasDAO petugasDAO, AkunController akun) {
        this.dao = dao;
        this.agenDAO = agenDAO;
        this.petugasDAO = petugasDAO;
        this.akun = akun;
    }

    public List<Distribusi> loadDistribusi() {
        akun.wajibRole(Role.Admin, Role.Gudang, Role.Distribusi);
        if (akun.getPetugasAktif().getRole() == Role.Distribusi) {
            return dao.findByPetugasDistribusi(akun.getPetugasAktif().getIdPetugas());
        }
        return dao.findAll();
    }

    public Distribusi cariDistribusi(String id) {
        akun.wajibRole(Role.Admin, Role.Gudang, Role.Distribusi);
        String idValid = validasiKode(id, "ID distribusi", 10);
        Distribusi distribusi = dao.findById(idValid);
        periksaKepemilikan(distribusi);
        return distribusi;
    }

    public List<DetailDistribusi> loadDetailDistribusi(String idDistribusi) {
        Distribusi distribusi = cariDistribusi(idDistribusi);
        if (distribusi == null) throw new IllegalArgumentException("Distribusi tidak ditemukan.");
        return dao.findDetails(idDistribusi);
    }

    // Admin membuat transaksi. Sistem menentukan petugas gudang dan petugas distribusi.
    public Distribusi simpanDistribusi(LocalDate tanggalKirim, List<DetailDistribusi> pesanan) {
        akun.wajibRole(Role.Admin);
        if (tanggalKirim == null) throw new IllegalArgumentException("Tanggal kirim wajib diisi.");
        if (pesanan == null || pesanan.isEmpty()) {
            throw new IllegalArgumentException("Minimal satu agen dan satu jenis produk harus dipesan.");
        }

        String kecamatan = null;
        Set<String> kombinasiUnik = new HashSet<>();
        for (DetailDistribusi detail : pesanan) {
            if (detail == null) throw new IllegalArgumentException("Rincian pesanan tidak boleh kosong.");
            Agen agen = agenDAO.findById(validasiKode(detail.getIdAgen(), "ID agen", 10));
            if (agen == null) throw new IllegalArgumentException("Agen " + detail.getIdAgen() + " tidak ditemukan.");
            String kecamatanAgen = agen.ambilKecamatan();
            if (kecamatanAgen == null) {
                throw new IllegalArgumentException("Alamat agen " + agen.getNamaAgen() + " tidak memuat kecamatan yang terdaftar.");
            }
            if (kecamatan == null) kecamatan = kecamatanAgen;
            if (!kecamatan.equalsIgnoreCase(kecamatanAgen)) {
                throw new IllegalArgumentException("Satu transaksi distribusi hanya boleh melayani satu kecamatan.");
            }
            String kunci = agen.getIdAgen() + "|" + detail.getJenisProduk().getNilaiDatabase();
            if (!kombinasiUnik.add(kunci)) {
                throw new IllegalArgumentException("Agen dan jenis produk yang sama tidak boleh dicatat dua kali dalam satu distribusi.");
            }
            if (detail.getJumlahPesan() <= 0) {
                throw new IllegalArgumentException("Jumlah pesan harus lebih besar dari 0.");
            }
        }

        PetugasGudang petugasGudang = cariPetugasGudangOutbound();
        PetugasDistribusi petugasDistribusi = cariPetugasDistribusi(kecamatan);
        String idDistribusi = dao.generateIdBaru();
        List<String> daftarIdDetail = dao.generateIdDetailBaru(pesanan.size());

        Distribusi distribusi = new Distribusi(idDistribusi, tanggalKirim,
                StatusKirim.DALAM_PENGIRIMAN, petugasGudang.getIdPetugas(),
                petugasDistribusi.getIdPetugas());

        List<DetailDistribusi> detailSimpan = new ArrayList<>();
        for (int i = 0; i < pesanan.size(); i++) {
            DetailDistribusi sumber = pesanan.get(i);
            detailSimpan.add(new DetailDistribusi(daftarIdDetail.get(i),
                    sumber.getIdAgen(), idDistribusi, sumber.getJenisProduk(),
                    sumber.getJumlahPesan(), sumber.getTanggalPesan()));
        }

        dao.insertWithDetails(distribusi, detailSimpan);
        return distribusi;
    }

    public void ubahTanggalKirim(String id, LocalDate tanggalKirim) {
        akun.wajibRole(Role.Admin);
        String idValid = validasiKode(id, "ID distribusi", 10);
        if (tanggalKirim == null) throw new IllegalArgumentException("Tanggal kirim wajib diisi.");
        Distribusi lama = dao.findById(idValid);
        if (lama == null) throw new IllegalArgumentException("Distribusi tidak ditemukan.");
        if (lama.getStatusKirim() != StatusKirim.DALAM_PENGIRIMAN) {
            throw new IllegalArgumentException("Distribusi yang sudah final tidak dapat diubah.");
        }
        dao.updateTanggalKirim(idValid, tanggalKirim);
    }

    public void ubahStatusDistribusi(String id, StatusKirim statusBaru,
            boolean barangSudahKembali) {
        akun.wajibRole(Role.Admin, Role.Distribusi);
        String idValid = validasiKode(id, "ID distribusi", 10);
        Distribusi distribusi = dao.findById(idValid);
        if (distribusi == null) throw new IllegalArgumentException("Distribusi tidak ditemukan.");
        periksaKepemilikan(distribusi);
        if (statusBaru == null) throw new IllegalArgumentException("Status pengiriman wajib dipilih.");
        dao.ubahStatus(idValid, statusBaru, barangSudahKembali);
    }

    public void hapusDistribusi(String id) {
        akun.wajibRole(Role.Admin);
        dao.delete(validasiKode(id, "ID distribusi", 10));
    }

    private PetugasGudang cariPetugasGudangOutbound() {
        for (Petugas petugas : petugasDAO.findAll()) {
            if (petugas instanceof PetugasGudang) {
                PetugasGudang gudang = (PetugasGudang) petugas;
                if (gudang.getSpesialisasiTugas().equals("Outbound")) return gudang;
            }
        }
        throw new IllegalArgumentException("Petugas Gudang dengan tugas Outbound belum terdaftar.");
    }

    private PetugasDistribusi cariPetugasDistribusi(String kecamatan) {
        for (Petugas petugas : petugasDAO.findAll()) {
            if (petugas instanceof PetugasDistribusi) {
                PetugasDistribusi pengirim = (PetugasDistribusi) petugas;
                if (pengirim.getWilayahTugas().equalsIgnoreCase(kecamatan)) return pengirim;
            }
        }
        throw new IllegalArgumentException("Belum ada Petugas Distribusi untuk wilayah " + kecamatan + ".");
    }

    private void periksaKepemilikan(Distribusi distribusi) {
        if (distribusi != null && akun.getPetugasAktif().getRole() == Role.Distribusi
                && !distribusi.getIdPetugasDistribusi().equals(akun.getPetugasAktif().getIdPetugas())) {
            throw new SecurityException("Distribusi ini bukan penugasan Anda.");
        }
    }

    private String validasiKode(String nilai, String kolom, int maksimum) {
        return ValidasiInput.kodeIdentitas(nilai, kolom, maksimum);
    }

    private String wajibIsi(String nilai, String kolom) {
        return ValidasiInput.wajibIsi(nilai, kolom);
    }
}
