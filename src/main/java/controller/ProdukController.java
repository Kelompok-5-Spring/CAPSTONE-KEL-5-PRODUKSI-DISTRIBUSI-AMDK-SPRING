/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;
import java.math.BigDecimal;
import java.util.List;
import dao.ProdukDAO;
import model.Produk;
import model.ProdukBotol;
import model.ProdukGalon;
import model.type.JenisProduk;
import model.type.Role;
import model.type.StatusKirim;

// Mengelola data dan ringkasan stok produk. Produk baru dibentuk otomatis dari Produksi.
public class ProdukController {
    private final ProdukDAO dao;
    private final AkunController akun;

    public ProdukController(ProdukDAO dao, AkunController akun) {
        this.dao = dao;
        this.akun = akun;
    }

    public List<Produk> loadProduk() {
        akun.wajibRole(Role.Admin, Role.Produksi, Role.Gudang, Role.Distribusi);
        return dao.findAll();
    }

    public List<Produk> loadProduk(JenisProduk jenis) {
        akun.wajibRole(Role.Admin, Role.Produksi, Role.Gudang, Role.Distribusi);
        if (jenis == null) throw new IllegalArgumentException("Jenis produk wajib dipilih.");
        return dao.findByJenis(jenis);
    }

    public Produk cariProduk(String id) {
        akun.wajibRole(Role.Admin, Role.Produksi, Role.Gudang, Role.Distribusi);
        return dao.findById(validasiKode(id, "ID produk", 15));
    }

    public int hitungStokTersedia(JenisProduk jenis) {
        akun.wajibRole(Role.Admin, Role.Produksi, Role.Gudang, Role.Distribusi);
        return dao.hitungStokTersedia(jenis);
    }

    public int hitungProdukDenganStatus(JenisProduk jenis, StatusKirim status) {
        akun.wajibRole(Role.Admin, Role.Produksi, Role.Gudang, Role.Distribusi);
        return dao.hitungProdukDenganStatus(jenis, status);
    }

    /** Memperbarui nama dan harga tanpa mengubah ID, jenis, kemasan, atau asal produksi. */
    public void ubahNamaDanHargaProduk(String id, String nama, String harga) {
        akun.wajibRole(Role.Admin, Role.Gudang);
        String idValid = validasiKode(id, "ID produk", 15);
        Produk lama = dao.findById(idValid);
        if (lama == null) throw new IllegalArgumentException("Produk tidak ditemukan.");
        if (lama.getIdDistribusi() != null) {
            throw new IllegalArgumentException("Nama atau harga produk yang sudah dialokasikan ke distribusi tidak dapat diubah.");
        }

        String namaValid = validasiTeks(nama, "Nama produk", 2, 100);
        BigDecimal hargaValid = parseDesimalTidakNegatif(harga, "Harga satuan");

        Produk produkBaru;
        if (lama instanceof ProdukBotol botol) {
            produkBaru = new ProdukBotol(lama.getIdProduk(), namaValid, hargaValid,
                    lama.getIdProduksi(), null, botol.getVolumeMl(), botol.getPcsPerDus());
        } else if (lama instanceof ProdukGalon galon) {
            produkBaru = new ProdukGalon(lama.getIdProduk(), namaValid, hargaValid,
                    lama.getIdProduksi(), null, galon.getVolumeLiter(), galon.getJenisGalon());
        } else {
            throw new IllegalStateException("Jenis data produk tidak dikenali.");
        }
        dao.update(produkBaru);
    }

    /** Kompatibilitas untuk pemanggil lama yang hanya mengubah harga. */
    public void ubahHargaProduk(String id, String harga) {
        akun.wajibRole(Role.Admin, Role.Gudang);
        Produk lama = dao.findById(validasiKode(id, "ID produk", 15));
        if (lama == null) throw new IllegalArgumentException("Produk tidak ditemukan.");
        ubahNamaDanHargaProduk(id, lama.getNamaProduk(), harga);
    }

    public void ubahProduk(String id, String nama, String harga,
            JenisProduk jenis, String volume, String detailKemasan) {
        akun.wajibRole(Role.Admin, Role.Gudang);
        String idValid = validasiKode(id, "ID produk", 15);
        Produk lama = dao.findById(idValid);
        if (lama == null) throw new IllegalArgumentException("Produk tidak ditemukan.");
        if (lama.getIdDistribusi() != null) {
            throw new IllegalArgumentException("Produk yang sudah dialokasikan tidak dapat diubah.");
        }
        if (jenis == null) throw new IllegalArgumentException("Jenis produk wajib dipilih.");
        if (lama.getJenisProduk() != jenis) {
            throw new IllegalArgumentException("Jenis dan volume produk tidak dapat diubah karena mengikuti data produksi asal.");
        }

        String namaValid = validasiTeks(nama, "Nama produk", 2, 100);
        if (!namaValid.equals(jenis.getNamaProduk())) {
            throw new IllegalArgumentException("Nama produk harus sesuai dengan jenis produk yang dipilih.");
        }
        BigDecimal hargaValid = parseDesimalTidakNegatif(harga, "Harga satuan");
        BigDecimal volumeValid = parseDesimalPositif(volume, "Volume");
        Produk produkBaru;
        if (jenis.isBotol()) {
            int pcs = parseIntegerPositif(detailKemasan, "Pcs per dus");
            produkBaru = new ProdukBotol(idValid, namaValid, hargaValid,
                    lama.getIdProduksi(), null, volumeValid, pcs);
        } else {
            String jenisGalon = wajibIsi(detailKemasan, "Jenis galon");
            produkBaru = new ProdukGalon(idValid, namaValid, hargaValid,
                    lama.getIdProduksi(), null, volumeValid, jenisGalon);
        }
        if (produkBaru.getJenisProduk() != jenis) {
            throw new IllegalArgumentException("Volume dan jenis produk tidak sesuai.");
        }
        dao.update(produkBaru);
    }

    public void hapusProduk(String id) {
        akun.wajibRole(Role.Admin);
        dao.delete(validasiKode(id, "ID produk", 15));
    }

    // Produk tidak ditambahkan manual. Record produk muncul saat produksi berstatus Selesai.
    private String validasiKode(String nilai, String kolom, int maksimum) {
        return ValidasiInput.kodeIdentitas(nilai, kolom, maksimum);
    }

    private String validasiTeks(String nilai, String kolom, int minimum, int maksimum) {
        return ValidasiInput.teksDenganBatas(nilai, kolom, minimum, maksimum);
    }

    private String wajibIsi(String nilai, String kolom) {
        return ValidasiInput.wajibIsi(nilai, kolom);
    }

    private BigDecimal parseDesimalPositif(String nilai, String kolom) {
        return ValidasiInput.parseDesimalPositif(nilai, kolom);
    }

    private BigDecimal parseDesimalTidakNegatif(String nilai, String kolom) {
        return ValidasiInput.parseDesimalTidakNegatif(nilai, kolom);
    }



    private int parseIntegerPositif(String nilai, String kolom) {
        return ValidasiInput.parseIntegerPositif(nilai, kolom);
    }
}