/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import config.DatabaseConnection;
import model.type.StatusKirim;
import model.type.StatusProduksi;

// Implementasi query rekap laporan produksi dan distribusi.
public class LaporanJdbcDAO implements LaporanDAO {
    @Override
    public int hitungJumlahProduksi(LocalDate awal, LocalDate akhir) {
        return hitung("SELECT COUNT(*) FROM produksi WHERE tanggal_produksi BETWEEN ? AND ?", awal, akhir, null);
    }

    @Override
    public int hitungProduksiDenganStatus(StatusProduksi status, LocalDate awal, LocalDate akhir) {
        return hitung("SELECT COUNT(*) FROM produksi WHERE status_produksi = ? AND tanggal_produksi BETWEEN ? AND ?",
                awal, akhir, status.getNilaiDatabase());
    }

    @Override
    public int hitungJumlahKemasanSelesai(LocalDate awal, LocalDate akhir) {
        String sql = "SELECT COALESCE(SUM(jumlah_produksi), 0) FROM produksi "
                + "WHERE status_produksi = 'Selesai' AND tanggal_produksi BETWEEN ? AND ?";
        try (Connection c = DatabaseConnection.bukaKoneksi();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDate(1, java.sql.Date.valueOf(awal));
            ps.setDate(2, java.sql.Date.valueOf(akhir));
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Gagal menghitung jumlah kemasan produksi: " + e.getMessage(), e);
        }
    }

    @Override
    public int hitungJumlahDistribusi(LocalDate awal, LocalDate akhir) {
        return hitung("SELECT COUNT(*) FROM distribusi WHERE tanggal_kirim BETWEEN ? AND ?", awal, akhir, null);
    }

    @Override
    public int hitungDistribusiDenganStatus(StatusKirim status, LocalDate awal, LocalDate akhir) {
        return hitung("SELECT COUNT(*) FROM distribusi WHERE status_kirim = ? AND tanggal_kirim BETWEEN ? AND ?",
                awal, akhir, status.getNilaiDatabase());
    }

    private int hitung(String sql, LocalDate awal, LocalDate akhir, String status) {
        try (Connection c = DatabaseConnection.bukaKoneksi();
             PreparedStatement ps = c.prepareStatement(sql)) {
            if (status == null) {
                ps.setDate(1, java.sql.Date.valueOf(awal));
                ps.setDate(2, java.sql.Date.valueOf(akhir));
            } else {
                ps.setString(1, status);
                ps.setDate(2, java.sql.Date.valueOf(awal));
                ps.setDate(3, java.sql.Date.valueOf(akhir));
            }
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Gagal mengambil data laporan: " + e.getMessage(), e);
        }
    }
}
