/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import config.DatabaseConnection;
import model.Produksi;
import model.type.StatusProduksi;

// Implementasi JDBC produksi. Perubahan menjadi Selesai dan pembentukan produk satu transaksi.
public class ProduksiJdbcDAO implements ProduksiDAO {
    private static final String SELECT_DATA = "SELECT id_produksi, jenis_produksi, tanggal_produksi, "
            + "status_produksi, jumlah_produksi, Petugas_Produksi_id_petugas FROM produksi ";

    @Override
    public void insert(Produksi produksi) {
        if (produksi.getStatusProduksi() != StatusProduksi.Direncanakan) {
            throw new IllegalArgumentException("Produksi baru harus berstatus Direncanakan.");
        }
        String sql = "INSERT INTO produksi (id_produksi, jenis_produksi, tanggal_produksi, "
                + "status_produksi, jumlah_produksi, Petugas_Produksi_id_petugas) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection c = DatabaseConnection.bukaKoneksi();
             PreparedStatement ps = c.prepareStatement(sql)) {
            isiParameter(ps, produksi);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Gagal menambahkan produksi: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Produksi> findAll() {
        List<Produksi> hasil = new ArrayList<>();
        try (Connection c = DatabaseConnection.bukaKoneksi();
             PreparedStatement ps = c.prepareStatement(SELECT_DATA + " ORDER BY tanggal_produksi DESC, id_produksi DESC");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) hasil.add(mapProduksi(rs));
            return hasil;
        } catch (SQLException e) {
            throw new IllegalStateException("Gagal mengambil data produksi: " + e.getMessage(), e);
        }
    }

    @Override
    public Produksi findById(String id) {
        try (Connection c = DatabaseConnection.bukaKoneksi();
             PreparedStatement ps = c.prepareStatement(SELECT_DATA + " WHERE id_produksi = ?")) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapProduksi(rs) : null;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Gagal mencari produksi: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(Produksi produksi) {
        String sql = "UPDATE produksi SET jenis_produksi = ?, tanggal_produksi = ?, jumlah_produksi = ?, "
                + "Petugas_Produksi_id_petugas = ? WHERE id_produksi = ? "
                + "AND status_produksi IN ('Direncanakan', 'Diproses')";
        try (Connection c = DatabaseConnection.bukaKoneksi();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, produksi.getJenisProduksi());
            ps.setDate(2, java.sql.Date.valueOf(produksi.getTanggalProduksi()));
            ps.setInt(3, produksi.getJumlahProduksi());
            ps.setString(4, produksi.getIdPetugasProduksi());
            ps.setString(5, produksi.getIdProduksi());
            if (ps.executeUpdate() == 0) {
                throw new IllegalArgumentException("Produksi tidak ditemukan atau statusnya sudah final.");
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Gagal mengubah produksi: " + e.getMessage(), e);
        }
    }

    @Override
    public void ubahStatusDanBuatProduk(String idProduksi, StatusProduksi statusBaru) {
        try (Connection c = DatabaseConnection.bukaKoneksi()) {
            c.setAutoCommit(false);
            try {
                Produksi produksi;
                try (PreparedStatement ps = c.prepareStatement(SELECT_DATA + " WHERE id_produksi = ? FOR UPDATE")) {
                    ps.setString(1, idProduksi);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) throw new IllegalArgumentException("Produksi tidak ditemukan.");
                        produksi = mapProduksi(rs);
                    }
                }

                produksi.ubahStatus(statusBaru);

                try (PreparedStatement ps = c.prepareStatement(
                        "UPDATE produksi SET status_produksi = ? WHERE id_produksi = ?")) {
                    ps.setString(1, statusBaru.getNilaiDatabase());
                    ps.setString(2, idProduksi);
                    ps.executeUpdate();
                }

                if (statusBaru == StatusProduksi.Selesai) {
                    ProdukJdbcDAO.buatProdukDariProduksi(c, produksi);
                }
                c.commit();
            } catch (SQLException | RuntimeException e) {
                c.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Gagal mengubah status produksi: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(String id) {
        try (Connection c = DatabaseConnection.bukaKoneksi()) {
            c.setAutoCommit(false);
            try {
                try (PreparedStatement ps = c.prepareStatement(
                        "SELECT status_produksi FROM produksi WHERE id_produksi = ? FOR UPDATE")) {
                    ps.setString(1, id);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) throw new IllegalArgumentException("Produksi tidak ditemukan.");
                        StatusProduksi status = StatusProduksi.fromDatabase(rs.getString(1));
                        if (status == StatusProduksi.Selesai) {
                            throw new IllegalArgumentException("Produksi selesai tidak dapat dihapus karena sudah menghasilkan produk.");
                        }
                    }
                }
                try (PreparedStatement ps = c.prepareStatement("DELETE FROM produksi WHERE id_produksi = ?")) {
                    ps.setString(1, id);
                    ps.executeUpdate();
                }
                c.commit();
            } catch (SQLException | RuntimeException e) {
                c.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Gagal menghapus produksi: " + e.getMessage(), e);
        }
    }

    @Override
    public String generateIdBaru() {
        Set<Integer> nomorTerpakai = new HashSet<>();
        try (Connection c = DatabaseConnection.bukaKoneksi();
             PreparedStatement ps = c.prepareStatement("SELECT id_produksi FROM produksi");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String id = rs.getString(1);
                if (id != null && id.startsWith("PRD")) {
                    try { nomorTerpakai.add(Integer.parseInt(id.substring(3))); }
                    catch (NumberFormatException e) { /* Abaikan ID lama yang tidak sesuai pola. */ }
                }
            }
            int nomor = 1;
            while (nomorTerpakai.contains(nomor)) nomor++;
            return String.format("PRD%03d", nomor);
        } catch (SQLException e) {
            throw new IllegalStateException("Gagal membuat ID produksi: " + e.getMessage(), e);
        }
    }

    private void isiParameter(PreparedStatement ps, Produksi p) throws SQLException {
        ps.setString(1, p.getIdProduksi());
        ps.setString(2, p.getJenisProduksi());
        ps.setDate(3, java.sql.Date.valueOf(p.getTanggalProduksi()));
        ps.setString(4, p.getStatusProduksi().getNilaiDatabase());
        ps.setInt(5, p.getJumlahProduksi());
        ps.setString(6, p.getIdPetugasProduksi());
    }

    private Produksi mapProduksi(ResultSet rs) throws SQLException {
        return new Produksi(rs.getString("id_produksi"), rs.getString("jenis_produksi"),
                rs.getDate("tanggal_produksi").toLocalDate(),
                StatusProduksi.fromDatabase(rs.getString("status_produksi")),
                rs.getInt("jumlah_produksi"), rs.getString("Petugas_Produksi_id_petugas"));
    }
}
