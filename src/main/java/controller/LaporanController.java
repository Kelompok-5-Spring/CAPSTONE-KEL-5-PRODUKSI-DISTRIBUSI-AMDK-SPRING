/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;
import java.time.LocalDate;
import java.time.DayOfWeek;
import java.util.Locale;
import dao.LaporanDAO;
import model.type.Role;
import model.type.StatusKirim;
import model.type.StatusProduksi;

// Mengolah pilihan periode dan menyusun ringkasan laporan produksi/distribusi.
public class LaporanController {
    private final LaporanDAO dao;
    private final AkunController akun;

    public LaporanController(LaporanDAO dao, AkunController akun) {
        this.dao = dao;
        this.akun = akun;
    }

    public String buatLaporanProduksi() {
        return buatLaporanProduksi(LocalDate.of(2000, 1, 1), LocalDate.of(2100, 12, 31));
    }

    // Overloading: laporan produksi untuk rentang tanggal tertentu.
    public String buatLaporanProduksi(LocalDate awal, LocalDate akhir) {
        akun.wajibRole(Role.Admin, Role.Produksi, Role.Gudang, Role.Distribusi);
        validasiRentangTanggal(awal, akhir);
        int total = dao.hitungJumlahProduksi(awal, akhir);
        int selesai = dao.hitungProduksiDenganStatus(StatusProduksi.Selesai, awal, akhir);
        int jumlahKemasan = dao.hitungJumlahKemasanSelesai(awal, akhir);
        int diproses = dao.hitungProduksiDenganStatus(StatusProduksi.Diproses, awal, akhir);
        int dibatalkan = dao.hitungProduksiDenganStatus(StatusProduksi.Dibatalkan, awal, akhir);

        return "LAPORAN PRODUKSI\n"
                + "Periode: " + awal + " s.d. " + akhir + "\n"
                + "Jumlah transaksi: " + total + "\n"
                + "Produksi selesai: " + selesai + "\n"
                + "Total kemasan dihasilkan: " + jumlahKemasan + "\n"
                + "Sedang diproses: " + diproses + "\n"
                + "Dibatalkan: " + dibatalkan;
    }

    public String buatLaporanDistribusi() {
        return buatLaporanDistribusi(LocalDate.of(2000, 1, 1), LocalDate.of(2100, 12, 31));
    }

    // Overloading: laporan distribusi untuk rentang tanggal tertentu.
    public String buatLaporanDistribusi(LocalDate awal, LocalDate akhir) {
        akun.wajibRole(Role.Admin, Role.Produksi, Role.Gudang, Role.Distribusi);
        validasiRentangTanggal(awal, akhir);
        int total = dao.hitungJumlahDistribusi(awal, akhir);
        int dalamPengiriman = dao.hitungDistribusiDenganStatus(StatusKirim.DALAM_PENGIRIMAN, awal, akhir);
        int terkirim = dao.hitungDistribusiDenganStatus(StatusKirim.TERKIRIM, awal, akhir);
        int tidakTerkirim = dao.hitungDistribusiDenganStatus(StatusKirim.TIDAK_TERKIRIM, awal, akhir);

        return "LAPORAN DISTRIBUSI\n"
                + "Periode: " + awal + " s.d. " + akhir + "\n"
                + "Jumlah transaksi: " + total + "\n"
                + "Dalam pengiriman: " + dalamPengiriman + "\n"
                + "Terkirim: " + terkirim + "\n"
                + "Tidak terkirim: " + tidakTerkirim;
    }

    public String buatLaporanPeriode(LocalDate tanggalAcuan, String jenisPeriode) {
        akun.wajibRole(Role.Admin, Role.Produksi, Role.Gudang, Role.Distribusi);
        if (tanggalAcuan == null) throw new IllegalArgumentException("Tanggal acuan wajib diisi.");
        String periode = wajibIsi(jenisPeriode, "Jenis periode").toLowerCase(Locale.ROOT);
        LocalDate awal;
        LocalDate akhir;

        if (periode.equals("minggu")) {
            DayOfWeek hari = tanggalAcuan.getDayOfWeek();
            awal = tanggalAcuan.minusDays(hari.getValue() - 1);
            akhir = awal.plusDays(6);
        } else if (periode.equals("bulan")) {
            awal = tanggalAcuan.withDayOfMonth(1);
            akhir = tanggalAcuan.withDayOfMonth(tanggalAcuan.lengthOfMonth());
        } else if (periode.equals("tahun")) {
            awal = tanggalAcuan.withDayOfYear(1);
            akhir = tanggalAcuan.withDayOfYear(tanggalAcuan.lengthOfYear());
        } else {
            throw new IllegalArgumentException("Periode harus Minggu, Bulan, atau Tahun.");
        }

        return "RINGKASAN PERIODE " + periode.toUpperCase(Locale.ROOT) + "\n"
                + "Tanggal: " + awal + " s.d. " + akhir + "\n\n"
                + buatLaporanProduksi(awal, akhir) + "\n\n"
                + buatLaporanDistribusi(awal, akhir);
    }

    private void validasiRentangTanggal(LocalDate awal, LocalDate akhir) {
        ValidasiInput.validasiRentangTanggal(awal, akhir);
    }

    private String wajibIsi(String nilai, String kolom) {
        return ValidasiInput.wajibIsi(nilai, kolom);
    }
}