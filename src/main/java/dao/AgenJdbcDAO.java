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
import model.Agen;

// Implementasi JDBC untuk tabel agen.
public class AgenJdbcDAO implements AgenDAO {
    @Override
    public void insert(Agen agen) {
        String sql = "INSERT INTO agen (id_agen, nama_agen, no_telepon, alamat_lengkap) VALUES (?, ?, ?, ?)";
        try (Connection c = DatabaseConnection.bukaKoneksi();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, agen.getIdAgen());
            ps.setString(2, agen.getNamaAgen());
            ps.setString(3, agen.getNoTelp());
            ps.setString(4, agen.getAlamatLengkap());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Gagal menyimpan data agen: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Agen> findAll() {
        List<Agen> hasil = new ArrayList<>();
        String sql = "SELECT id_agen, nama_agen, no_telepon, alamat_lengkap FROM agen ORDER BY id_agen";
        try (Connection c = DatabaseConnection.bukaKoneksi();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                hasil.add(mapAgen(rs));
            }
            return hasil;
        } catch (SQLException e) {
            throw new IllegalStateException("Gagal mengambil data agen: " + e.getMessage(), e);
        }
    }

    @Override
    public Agen findById(String id) {
        String sql = "SELECT id_agen, nama_agen, no_telepon, alamat_lengkap FROM agen WHERE id_agen = ?";
        try (Connection c = DatabaseConnection.bukaKoneksi();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapAgen(rs) : null;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Gagal mencari agen: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(Agen agen) {
        String sql = "UPDATE agen SET nama_agen = ?, no_telepon = ?, alamat_lengkap = ? WHERE id_agen = ?";
        try (Connection c = DatabaseConnection.bukaKoneksi();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, agen.getNamaAgen());
            ps.setString(2, agen.getNoTelp());
            ps.setString(3, agen.getAlamatLengkap());
            ps.setString(4, agen.getIdAgen());
            if (ps.executeUpdate() == 0) {
                throw new IllegalArgumentException("Agen tidak ditemukan.");
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Gagal mengubah data agen: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(String id) {
        String cek = "SELECT COUNT(*) FROM detail_distribusi WHERE id_agen = ?";
        String hapus = "DELETE FROM agen WHERE id_agen = ?";
        try (Connection c = DatabaseConnection.bukaKoneksi()) {
            try (PreparedStatement ps = c.prepareStatement(cek)) {
                ps.setString(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    rs.next();
                    if (rs.getInt(1) > 0) {
                        throw new IllegalArgumentException("Agen tidak dapat dihapus karena memiliki riwayat pemesanan.");
                    }
                }
            }
            try (PreparedStatement ps = c.prepareStatement(hapus)) {
                ps.setString(1, id);
                if (ps.executeUpdate() == 0) {
                    throw new IllegalArgumentException("Agen tidak ditemukan.");
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Gagal menghapus agen: " + e.getMessage(), e);
        }
    }

    @Override
    public String generateIdBaru() {
        Set<Integer> nomorTerpakai = new HashSet<>();
        String sql = "SELECT id_agen FROM agen";
        try (Connection c = DatabaseConnection.bukaKoneksi();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String id = rs.getString(1);
                if (id != null && id.startsWith("AGN")) {
                    try { nomorTerpakai.add(Integer.parseInt(id.substring(3))); }
                    catch (NumberFormatException e) { /* Abaikan format lama yang tidak sesuai. */ }
                }
            }
            int nomor = 1;
            while (nomorTerpakai.contains(nomor)) nomor++;
            return String.format("AGN%03d", nomor);
        } catch (SQLException e) {
            throw new IllegalStateException("Gagal membuat ID agen: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean nomorTeleponDipakai(String nomorTelepon, String idYangDikecualikan) {
        String sql = "SELECT COUNT(*) FROM agen WHERE no_telepon = ? AND id_agen <> ?";
        try (Connection c = DatabaseConnection.bukaKoneksi();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, nomorTelepon);
            ps.setString(2, idYangDikecualikan == null ? "" : idYangDikecualikan);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Gagal memeriksa nomor telepon agen: " + e.getMessage(), e);
        }
    }

    private Agen mapAgen(ResultSet rs) throws SQLException {
        return new Agen(rs.getString("id_agen"), rs.getString("nama_agen"),
                rs.getString("no_telepon"), rs.getString("alamat_lengkap"));
    }
}
