/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import config.DatabaseConnection;
import model.Produk;
import model.ProdukBotol;
import model.ProdukGalon;
import model.Produksi;
import model.type.JenisProduk;
import model.type.StatusKirim;

//Implementasi JDBC untuk tabel produk dan tabel subtype produk_botol/produk_galon. */
public class ProdukJdbcDAO implements ProdukDAO {
    private static final String SELECT_ALL = "SELECT p.id_produk, p.nama_produk, p.harga_satuan, p.Produksi_id_produksi, "
            + "pb.volume_ml, pb.pcs_per_dus, pb.id_distribusi AS distribusi_botol, "
            + "pg.volume_liter, pg.jenis_galon, pg.id_distribusi AS distribusi_galon "
            + "FROM produk p LEFT JOIN produk_botol pb ON pb.id_produk = p.id_produk "
            + "LEFT JOIN produk_galon pg ON pg.id_produk = p.id_produk ";

    @Override
    public void insert(Produk produk) {
        try (Connection c = DatabaseConnection.bukaKoneksi()) {
            c.setAutoCommit(false);
            insertProduk(c, produk);
            c.commit();
        } catch (SQLException ex) {
            throw new IllegalStateException("Gagal menambahkan produk: " + ex.getMessage(), ex);
        }
    }

    static void insertProduk(Connection c, Produk produk) throws SQLException {
        String sql = "INSERT INTO produk (id_produk, nama_produk, harga_satuan, Produksi_id_produksi) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, produk.getIdProduk());
            ps.setString(2, produk.getNamaProduk());
            ps.setBigDecimal(3, produk.getHargaSatuan());
            ps.setString(4, produk.getIdProduksi());
            ps.executeUpdate();
        }
        insertSubtype(c, produk);
    }

    private static void insertSubtype(Connection c, Produk produk) throws SQLException {
        if (produk instanceof ProdukBotol botol) {
            String sql = "INSERT INTO produk_botol (id_produk, volume_ml, pcs_per_dus, id_distribusi) VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setString(1, botol.getIdProduk());
                ps.setBigDecimal(2, botol.getVolumeMl());
                ps.setInt(3, botol.getPcsPerDus());
                ps.setString(4, botol.getIdDistribusi());
                ps.executeUpdate();
            }
        } else if (produk instanceof ProdukGalon galon) {
            String sql = "INSERT INTO produk_galon (id_produk, volume_liter, jenis_galon, id_distribusi) VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setString(1, galon.getIdProduk());
                ps.setBigDecimal(2, galon.getVolumeLiter());
                ps.setString(3, galon.getJenisGalon());
                ps.setString(4, galon.getIdDistribusi());
                ps.executeUpdate();
            }
        } else {
            throw new IllegalArgumentException("Jenis subclass Produk tidak dikenali.");
        }
    }

    @Override
    public List<Produk> findAll() {
        List<Produk> hasil = new ArrayList<>();
        try (Connection c = DatabaseConnection.bukaKoneksi();
             PreparedStatement ps = c.prepareStatement(SELECT_ALL + " ORDER BY p.id_produk");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) hasil.add(mapProduk(rs));
            return hasil;
        } catch (SQLException ex) {
            throw new IllegalStateException("Gagal mengambil data produk: " + ex.getMessage(), ex);
        }
    }

    @Override
    public Produk findById(String id) {
        try (Connection c = DatabaseConnection.bukaKoneksi();
             PreparedStatement ps = c.prepareStatement(SELECT_ALL + " WHERE p.id_produk = ?")) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? mapProduk(rs) : null; }
        } catch (SQLException ex) {
            throw new IllegalStateException("Gagal mencari produk: " + ex.getMessage(), ex);
        }
    }

    @Override
    public List<Produk> findByJenis(JenisProduk jenis) {
        List<Produk> hasil = new ArrayList<>();
        for (Produk produk : findAll()) if (produk.getJenisProduk() == jenis) hasil.add(produk);
        return hasil;
    }

    @Override
    public int hitungStokTersedia(JenisProduk jenis) {
        String sql;
        if (jenis.isBotol()) {
            sql = "SELECT COUNT(*) FROM produk_botol WHERE volume_ml = ? AND id_distribusi IS NULL";
        } else {
            sql = "SELECT COUNT(*) FROM produk_galon WHERE volume_liter = ? AND id_distribusi IS NULL";
        }
        try (Connection c = DatabaseConnection.bukaKoneksi(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setBigDecimal(1, BigDecimal.valueOf(jenis.getVolume()));
            try (ResultSet rs = ps.executeQuery()) { rs.next(); return rs.getInt(1); }
        } catch (SQLException ex) {
            throw new IllegalStateException("Gagal menghitung stok tersedia: " + ex.getMessage(), ex);
        }
    }

    @Override
    public int hitungProdukDenganStatus(JenisProduk jenis, StatusKirim status) {
        String tabel = jenis.isBotol() ? "produk_botol" : "produk_galon";
        String kolomVolume = jenis.isBotol() ? "volume_ml" : "volume_liter";
        String sql = "SELECT COUNT(*) FROM " + tabel + " x JOIN distribusi d ON d.id_distribusi = x.id_distribusi "
                + "WHERE x." + kolomVolume + " = ? AND d.status_kirim = ?";
        try (Connection c = DatabaseConnection.bukaKoneksi(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setBigDecimal(1, BigDecimal.valueOf(jenis.getVolume()));
            ps.setString(2, status.getNilaiDatabase());
            try (ResultSet rs = ps.executeQuery()) { rs.next(); return rs.getInt(1); }
        } catch (SQLException ex) {
            throw new IllegalStateException("Gagal menghitung produk berdasarkan status distribusi: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void update(Produk produk) {
        String sql = "UPDATE produk SET nama_produk = ?, harga_satuan = ?, Produksi_id_produksi = ? WHERE id_produk = ?";
        try (Connection c = DatabaseConnection.bukaKoneksi()) {
            c.setAutoCommit(false);
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setString(1, produk.getNamaProduk());
                ps.setBigDecimal(2, produk.getHargaSatuan());
                ps.setString(3, produk.getIdProduksi());
                ps.setString(4, produk.getIdProduk());
                if (ps.executeUpdate() == 0) throw new IllegalArgumentException("Produk tidak ditemukan.");
            }
            if (produk instanceof ProdukBotol botol) {
                try (PreparedStatement ps = c.prepareStatement(
                        "UPDATE produk_botol SET volume_ml = ?, pcs_per_dus = ? WHERE id_produk = ?")) {
                    ps.setBigDecimal(1, botol.getVolumeMl());
                    ps.setInt(2, botol.getPcsPerDus());
                    ps.setString(3, botol.getIdProduk());
                    if (ps.executeUpdate() == 0) throw new IllegalArgumentException("Detail produk botol tidak ditemukan.");
                }
            } else if (produk instanceof ProdukGalon galon) {
                try (PreparedStatement ps = c.prepareStatement(
                        "UPDATE produk_galon SET volume_liter = ?, jenis_galon = ? WHERE id_produk = ?")) {
                    ps.setBigDecimal(1, galon.getVolumeLiter());
                    ps.setString(2, galon.getJenisGalon());
                    ps.setString(3, galon.getIdProduk());
                    if (ps.executeUpdate() == 0) throw new IllegalArgumentException("Detail produk galon tidak ditemukan.");
                }
            }
            c.commit();
        } catch (SQLException ex) {
            throw new IllegalStateException("Gagal memperbarui produk: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void delete(String id) {
        try (Connection c = DatabaseConnection.bukaKoneksi()) {
            c.setAutoCommit(false);
            String distribusi = null;
            try (PreparedStatement ps = c.prepareStatement(
                    "SELECT id_distribusi FROM produk_botol WHERE id_produk = ? UNION ALL "
                    + "SELECT id_distribusi FROM produk_galon WHERE id_produk = ?")) {
                ps.setString(1, id); ps.setString(2, id);
                try (ResultSet rs = ps.executeQuery()) { if (rs.next()) distribusi = rs.getString(1); }
            }
            if (distribusi != null) throw new IllegalArgumentException("Produk yang sudah dialokasikan ke distribusi tidak dapat dihapus.");
            try (PreparedStatement ps = c.prepareStatement("DELETE FROM produk_botol WHERE id_produk = ?")) {
                ps.setString(1, id); ps.executeUpdate();
            }
            try (PreparedStatement ps = c.prepareStatement("DELETE FROM produk_galon WHERE id_produk = ?")) {
                ps.setString(1, id); ps.executeUpdate();
            }
            try (PreparedStatement ps = c.prepareStatement("DELETE FROM produk WHERE id_produk = ?")) {
                ps.setString(1, id);
                if (ps.executeUpdate() == 0) throw new IllegalArgumentException("Produk tidak ditemukan.");
            }
            c.commit();
        } catch (SQLException ex) {
            throw new IllegalStateException("Gagal menghapus produk: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void buatProdukDariProduksi(Produksi produksi) {
        try (Connection c = DatabaseConnection.bukaKoneksi()) {
            c.setAutoCommit(false);
            buatProdukDariProduksi(c, produksi);
            c.commit();
        } catch (SQLException ex) {
            throw new IllegalStateException("Gagal membentuk produk dari produksi: " + ex.getMessage(), ex);
        }
    }

    static void buatProdukDariProduksi(Connection c, Produksi produksi) throws SQLException {
        String nama = produksi.getJenisProduksi();
        String sqlRef = "SELECT p.harga_satuan, pb.volume_ml, pb.pcs_per_dus, pg.volume_liter, pg.jenis_galon "
                + "FROM produk p LEFT JOIN produk_botol pb ON pb.id_produk = p.id_produk "
                + "LEFT JOIN produk_galon pg ON pg.id_produk = p.id_produk "
                + "WHERE p.nama_produk = ? LIMIT 1";
        BigDecimal harga;
        BigDecimal volume;
        int pcs = 0;
        String jenisGalon = null;
        boolean botol;
        try (PreparedStatement ps = c.prepareStatement(sqlRef)) {
            ps.setString(1, nama);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) throw new IllegalStateException("Belum ada data referensi harga dan kemasan untuk " + nama + ". Tambahkan satu produk contoh terlebih dahulu.");
                harga = rs.getBigDecimal("harga_satuan");
                botol = rs.getBigDecimal("volume_ml") != null;
                if (botol) {
                    volume = rs.getBigDecimal("volume_ml");
                    pcs = rs.getInt("pcs_per_dus");
                } else {
                    volume = rs.getBigDecimal("volume_liter");
                    jenisGalon = rs.getString("jenis_galon");
                }
            }
        }

        int nextNumber = nextProductNumber(c);
        for (int i = 0; i < produksi.getJumlahProduksi(); i++) {
            String id = String.format("PDK%03d", nextNumber++);
            Produk produk = botol
                    ? new ProdukBotol(id, nama, harga, produksi.getIdProduksi(), null, volume, pcs)
                    : new ProdukGalon(id, nama, harga, produksi.getIdProduksi(), null, volume, jenisGalon);
            insertProduk(c, produk);
        }
    }

    private static int nextProductNumber(Connection c) throws SQLException {
        String sql = "SELECT COALESCE(MAX(CAST(SUBSTRING(id_produk, 4) AS UNSIGNED)), 0) + 1 FROM produk";
        try (Statement st = c.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            rs.next(); return rs.getInt(1);
        }
    }

    private Produk mapProduk(ResultSet rs) throws SQLException {
        String id = rs.getString("id_produk");
        String nama = rs.getString("nama_produk");
        BigDecimal harga = rs.getBigDecimal("harga_satuan");
        String idProduksi = rs.getString("Produksi_id_produksi");
        BigDecimal volumeMl = rs.getBigDecimal("volume_ml");
        if (volumeMl != null) {
            return new ProdukBotol(id, nama, harga, idProduksi, rs.getString("distribusi_botol"),
                    volumeMl, rs.getInt("pcs_per_dus"));
        }
        BigDecimal volumeLiter = rs.getBigDecimal("volume_liter");
        if (volumeLiter != null) {
            return new ProdukGalon(id, nama, harga, idProduksi, rs.getString("distribusi_galon"),
                    volumeLiter, rs.getString("jenis_galon"));
        }
        throw new IllegalStateException("Produk " + id + " tidak memiliki detail subtype botol atau galon.");
    }
}
