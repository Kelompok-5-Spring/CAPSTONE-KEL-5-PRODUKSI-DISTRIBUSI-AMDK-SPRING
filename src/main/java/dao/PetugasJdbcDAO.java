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
import model.Petugas;
import model.PetugasAdmin;
import model.PetugasDistribusi;
import model.PetugasGudang;
import model.PetugasProduksi;
import model.type.Role;

// Implementasi JDBC untuk petugas dan empat tabel subtype.
public class PetugasJdbcDAO implements PetugasDAO {
    private static final String SELECT_DATA = "SELECT p.id_petugas, p.username, p.password, p.role, p.nama, "
            + "pa.spesialisasi_admin, pp.spesialisasi_proses, pg.spesialisasi_tugas, pd.wilayah_tugas "
            + "FROM petugas p "
            + "LEFT JOIN petugas_admin pa ON pa.id_petugas = p.id_petugas "
            + "LEFT JOIN petugas_produksi pp ON pp.id_petugas = p.id_petugas "
            + "LEFT JOIN petugas_gudang pg ON pg.id_petugas = p.id_petugas "
            + "LEFT JOIN petugas_distribusi pd ON pd.id_petugas = p.id_petugas ";

    @Override
    public void insert(Petugas petugas) {
        String sql = "INSERT INTO petugas (id_petugas, username, password, role, nama) VALUES (?, ?, ?, ?, ?)";
        try (Connection c = DatabaseConnection.bukaKoneksi()) {
            c.setAutoCommit(false);
            try {
                try (PreparedStatement ps = c.prepareStatement(sql)) {
                    ps.setString(1, petugas.getIdPetugas());
                    ps.setString(2, petugas.getUsername());
                    ps.setString(3, petugas.getPasswordHash());
                    ps.setString(4, petugas.getRole().getNilaiDatabase());
                    ps.setString(5, petugas.getNama());
                    ps.executeUpdate();
                }
                insertSubtype(c, petugas);
                c.commit();
            } catch (SQLException | RuntimeException e) {
                c.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Gagal menyimpan akun petugas: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Petugas> findAll() {
        List<Petugas> hasil = new ArrayList<>();
        try (Connection c = DatabaseConnection.bukaKoneksi();
             PreparedStatement ps = c.prepareStatement(SELECT_DATA + " ORDER BY p.id_petugas");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) hasil.add(mapPetugas(rs));
            return hasil;
        } catch (SQLException e) {
            throw new IllegalStateException("Gagal mengambil akun petugas: " + e.getMessage(), e);
        }
    }

    @Override
    public Petugas findById(String id) {
        try (Connection c = DatabaseConnection.bukaKoneksi();
             PreparedStatement ps = c.prepareStatement(SELECT_DATA + " WHERE p.id_petugas = ?")) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapPetugas(rs) : null;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Gagal mencari petugas: " + e.getMessage(), e);
        }
    }

    @Override
    public Petugas findByUsername(String username) {
        try (Connection c = DatabaseConnection.bukaKoneksi();
             PreparedStatement ps = c.prepareStatement(SELECT_DATA + " WHERE LOWER(p.username) = LOWER(?)")) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapPetugas(rs) : null;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Gagal mencari username: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(Petugas petugas) {
        String sql = "UPDATE petugas SET username = ?, password = ?, role = ?, nama = ? WHERE id_petugas = ?";
        try (Connection c = DatabaseConnection.bukaKoneksi()) {
            c.setAutoCommit(false);
            try {
                periksaPerubahanRole(c, petugas);
                try (PreparedStatement ps = c.prepareStatement(sql)) {
                    ps.setString(1, petugas.getUsername());
                    ps.setString(2, petugas.getPasswordHash());
                    ps.setString(3, petugas.getRole().getNilaiDatabase());
                    ps.setString(4, petugas.getNama());
                    ps.setString(5, petugas.getIdPetugas());
                    if (ps.executeUpdate() == 0) {
                        throw new IllegalArgumentException("Akun petugas tidak ditemukan.");
                    }
                }
                hapusSubtype(c, petugas.getIdPetugas());
                insertSubtype(c, petugas);
                c.commit();
            } catch (SQLException | RuntimeException e) {
                c.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Gagal mengubah akun petugas: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(String id) {
        String cek = "SELECT "
                + "(SELECT COUNT(*) FROM produksi WHERE Petugas_Produksi_id_petugas = ?) + "
                + "(SELECT COUNT(*) FROM distribusi WHERE Petugas_Gudang_id_petugas = ?) + "
                + "(SELECT COUNT(*) FROM distribusi WHERE Petugas_Distribusi_id_petugas = ?)";
        try (Connection c = DatabaseConnection.bukaKoneksi()) {
            c.setAutoCommit(false);
            try {
                try (PreparedStatement ps = c.prepareStatement(cek)) {
                    ps.setString(1, id);
                    ps.setString(2, id);
                    ps.setString(3, id);
                    try (ResultSet rs = ps.executeQuery()) {
                        rs.next();
                        if (rs.getInt(1) > 0) {
                            throw new IllegalArgumentException("Petugas tidak dapat dihapus karena sudah tercatat dalam transaksi.");
                        }
                    }
                }
                hapusSubtype(c, id);
                try (PreparedStatement ps = c.prepareStatement("DELETE FROM petugas WHERE id_petugas = ?")) {
                    ps.setString(1, id);
                    if (ps.executeUpdate() == 0) {
                        throw new IllegalArgumentException("Petugas tidak ditemukan.");
                    }
                }
                c.commit();
            } catch (SQLException | RuntimeException e) {
                c.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Gagal menghapus petugas: " + e.getMessage(), e);
        }
    }

    @Override
    public String generateIdBaru() {
        Set<Integer> nomorTerpakai = new HashSet<>();
        try (Connection c = DatabaseConnection.bukaKoneksi();
             PreparedStatement ps = c.prepareStatement("SELECT id_petugas FROM petugas");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String id = rs.getString(1);
                if (id != null && id.startsWith("PTG")) {
                    try { nomorTerpakai.add(Integer.parseInt(id.substring(3))); }
                    catch (NumberFormatException e) { /* Abaikan ID lama yang tidak sesuai pola. */ }
                }
            }
            int nomor = 1;
            while (nomorTerpakai.contains(nomor)) nomor++;
            return String.format("PTG%03d", nomor);
        } catch (SQLException e) {
            throw new IllegalStateException("Gagal membuat ID petugas: " + e.getMessage(), e);
        }
    }

    private void insertSubtype(Connection c, Petugas petugas) throws SQLException {
        if (petugas instanceof PetugasAdmin) {
            PetugasAdmin p = (PetugasAdmin) petugas;
            simpanSubtype(c, "INSERT INTO petugas_admin (id_petugas, spesialisasi_admin) VALUES (?, ?)",
                    p.getIdPetugas(), p.getSpesialisasiAdmin());
        } else if (petugas instanceof PetugasProduksi) {
            PetugasProduksi p = (PetugasProduksi) petugas;
            simpanSubtype(c, "INSERT INTO petugas_produksi (id_petugas, spesialisasi_proses) VALUES (?, ?)",
                    p.getIdPetugas(), p.getSpesialisasiProses());
        } else if (petugas instanceof PetugasGudang) {
            PetugasGudang p = (PetugasGudang) petugas;
            simpanSubtype(c, "INSERT INTO petugas_gudang (id_petugas, spesialisasi_tugas) VALUES (?, ?)",
                    p.getIdPetugas(), p.getSpesialisasiTugas());
        } else if (petugas instanceof PetugasDistribusi) {
            PetugasDistribusi p = (PetugasDistribusi) petugas;
            simpanSubtype(c, "INSERT INTO petugas_distribusi (id_petugas, wilayah_tugas) VALUES (?, ?)",
                    p.getIdPetugas(), p.getWilayahTugas());
        } else {
            throw new IllegalArgumentException("Jenis petugas tidak dikenali.");
        }
    }

    private void simpanSubtype(Connection c, String sql, String id, String detail) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.setString(2, detail);
            ps.executeUpdate();
        }
    }

    private void hapusSubtype(Connection c, String id) throws SQLException {
        String[] tabel = {"petugas_admin", "petugas_produksi", "petugas_gudang", "petugas_distribusi"};
        for (String namaTabel : tabel) {
            try (PreparedStatement ps = c.prepareStatement("DELETE FROM " + namaTabel + " WHERE id_petugas = ?")) {
                ps.setString(1, id);
                ps.executeUpdate();
            }
        }
    }

    private void periksaPerubahanRole(Connection c, Petugas petugasBaru) throws SQLException {
        String sql = SELECT_DATA + " WHERE p.id_petugas = ?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, petugasBaru.getIdPetugas());
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) throw new IllegalArgumentException("Petugas tidak ditemukan.");
                Role roleLama = Role.fromDatabase(rs.getString("role"));
                if (roleLama != petugasBaru.getRole()) {
                    String cek = "SELECT "
                            + "(SELECT COUNT(*) FROM produksi WHERE Petugas_Produksi_id_petugas = ?) + "
                            + "(SELECT COUNT(*) FROM distribusi WHERE Petugas_Gudang_id_petugas = ?) + "
                            + "(SELECT COUNT(*) FROM distribusi WHERE Petugas_Distribusi_id_petugas = ?)";
                    try (PreparedStatement cekPs = c.prepareStatement(cek)) {
                        cekPs.setString(1, petugasBaru.getIdPetugas());
                        cekPs.setString(2, petugasBaru.getIdPetugas());
                        cekPs.setString(3, petugasBaru.getIdPetugas());
                        try (ResultSet cekRs = cekPs.executeQuery()) {
                            cekRs.next();
                            if (cekRs.getInt(1) > 0) {
                                throw new IllegalArgumentException("Role petugas tidak dapat diubah karena sudah digunakan dalam transaksi.");
                            }
                        }
                    }
                }
            }
        }
    }

    private Petugas mapPetugas(ResultSet rs) throws SQLException {
        String id = rs.getString("id_petugas");
        String username = rs.getString("username");
        String password = rs.getString("password");
        String nama = rs.getString("nama");
        Role role = Role.fromDatabase(rs.getString("role"));
        if (role == Role.Admin) {
            return new PetugasAdmin(id, username, nama, password, rs.getString("spesialisasi_admin"));
        } else if (role == Role.Produksi) {
            return new PetugasProduksi(id, username, nama, password, rs.getString("spesialisasi_proses"));
        } else if (role == Role.Gudang) {
            return new PetugasGudang(id, username, nama, password, rs.getString("spesialisasi_tugas"));
        } else {
            return new PetugasDistribusi(id, username, nama, password, rs.getString("wilayah_tugas"));
        }
    }
}
