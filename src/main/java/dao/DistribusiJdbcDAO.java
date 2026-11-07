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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import config.DatabaseConnection;
import model.DetailDistribusi;
import model.Distribusi;
import model.type.JenisProduk;
import model.type.StatusKirim;

// Implementasi JDBC distribusi, detail pesanan, alokasi, dan pengembalian produk.
public class DistribusiJdbcDAO implements DistribusiDAO {
    private static final String SELECT_DATA = "SELECT id_distribusi, tanggal_kirim, status_kirim, "
            + "Petugas_Gudang_id_petugas, Petugas_Distribusi_id_petugas FROM distribusi ";

    @Override
    public void insert(Distribusi distribusi) {
        throw new IllegalArgumentException("Distribusi harus disimpan bersama minimal satu Detail_Distribusi.");
    }

    @Override
    public void insertWithDetails(Distribusi distribusi, List<DetailDistribusi> details) {
        if (distribusi.getStatusKirim() != StatusKirim.DALAM_PENGIRIMAN) {
            throw new IllegalArgumentException("Distribusi baru harus berstatus Dalam Pengiriman.");
        }
        if (details == null || details.isEmpty()) {
            throw new IllegalArgumentException("Minimal satu rincian pesanan harus ditambahkan.");
        }

        try (Connection c = DatabaseConnection.bukaKoneksi()) {
            c.setAutoCommit(false);
            try {
                insertHeader(c, distribusi);
                for (DetailDistribusi detail : details) {
                    if (!distribusi.getIdDistribusi().equals(detail.getIdDistribusi())) {
                        throw new IllegalArgumentException("ID distribusi pada detail tidak sesuai.");
                    }
                    insertDetail(c, detail);
                    alokasikanProduk(c, distribusi.getIdDistribusi(),
                            detail.getJenisProduk(), detail.getJumlahPesan());
                }
                c.commit();
            } catch (SQLException | RuntimeException e) {
                c.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Distribusi gagal disimpan. Periksa stok, agen, dan petugas. "
                    + e.getMessage(), e);
        }
    }

    private void insertHeader(Connection c, Distribusi d) throws SQLException {
        String sql = "INSERT INTO distribusi (id_distribusi, tanggal_kirim, status_kirim, "
                + "Petugas_Gudang_id_petugas, Petugas_Distribusi_id_petugas) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, d.getIdDistribusi());
            ps.setDate(2, java.sql.Date.valueOf(d.getTanggalKirim()));
            ps.setString(3, d.getStatusKirim().getNilaiDatabase());
            ps.setString(4, d.getIdPetugasGudang());
            ps.setString(5, d.getIdPetugasDistribusi());
            ps.executeUpdate();
        }
    }

    private void insertDetail(Connection c, DetailDistribusi d) throws SQLException {
        String sql = "INSERT INTO detail_distribusi (id_detail_distribusi, id_agen, id_distribusi, "
                + "jenis_produk, jumlah_pesan, tanggal_pesan) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, d.getIdDetailDistribusi());
            ps.setString(2, d.getIdAgen());
            ps.setString(3, d.getIdDistribusi());
            ps.setString(4, d.getJenisProduk().getNilaiDatabase());
            ps.setInt(5, d.getJumlahPesan());
            ps.setDate(6, java.sql.Date.valueOf(d.getTanggalPesan()));
            ps.executeUpdate();
        }
    }

    private void alokasikanProduk(Connection c, String idDistribusi,
            JenisProduk jenis, int jumlah) throws SQLException {
        String tabel = jenis.isBotol() ? "produk_botol" : "produk_galon";
        String kolomVolume = jenis.isBotol() ? "volume_ml" : "volume_liter";
        String sqlPilih = "SELECT id_produk FROM " + tabel + " WHERE " + kolomVolume
                + " = ? AND id_distribusi IS NULL ORDER BY id_produk LIMIT ? FOR UPDATE";
        List<String> idProduk = new ArrayList<>();

        try (PreparedStatement ps = c.prepareStatement(sqlPilih)) {
            ps.setBigDecimal(1, BigDecimal.valueOf(jenis.getVolume()));
            ps.setInt(2, jumlah);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) idProduk.add(rs.getString(1));
            }
        }

        if (idProduk.size() != jumlah) {
            throw new IllegalArgumentException("Stok " + jenis.getNamaProduk()
                    + " tidak mencukupi. Dibutuhkan " + jumlah + ", tersedia " + idProduk.size() + ".");
        }

        StringBuilder sqlUpdate = new StringBuilder("UPDATE ").append(tabel)
                .append(" SET id_distribusi = ? WHERE id_distribusi IS NULL AND id_produk IN (");
        for (int i = 0; i < idProduk.size(); i++) {
            if (i > 0) sqlUpdate.append(",");
            sqlUpdate.append("?");
        }
        sqlUpdate.append(")");

        try (PreparedStatement ps = c.prepareStatement(sqlUpdate.toString())) {
            ps.setString(1, idDistribusi);
            for (int i = 0; i < idProduk.size(); i++) ps.setString(i + 2, idProduk.get(i));
            if (ps.executeUpdate() != jumlah) {
                throw new IllegalStateException("Alokasi produk tidak lengkap.");
            }
        }
    }

    @Override
    public List<Distribusi> findAll() {
        List<Distribusi> hasil = new ArrayList<>();
        try (Connection c = DatabaseConnection.bukaKoneksi();
             PreparedStatement ps = c.prepareStatement(SELECT_DATA + " ORDER BY tanggal_kirim DESC, id_distribusi DESC");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) hasil.add(mapDistribusi(rs));
            return hasil;
        } catch (SQLException e) {
            throw new IllegalStateException("Gagal mengambil data distribusi: " + e.getMessage(), e);
        }
    }

    @Override
    public Distribusi findById(String id) {
        try (Connection c = DatabaseConnection.bukaKoneksi();
             PreparedStatement ps = c.prepareStatement(SELECT_DATA + " WHERE id_distribusi = ?")) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapDistribusi(rs) : null;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Gagal mencari distribusi: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Distribusi> findByPetugasDistribusi(String idPetugas) {
        List<Distribusi> hasil = new ArrayList<>();
        try (Connection c = DatabaseConnection.bukaKoneksi();
             PreparedStatement ps = c.prepareStatement(SELECT_DATA
                     + " WHERE Petugas_Distribusi_id_petugas = ? ORDER BY tanggal_kirim DESC, id_distribusi DESC")) {
            ps.setString(1, idPetugas);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) hasil.add(mapDistribusi(rs));
            }
            return hasil;
        } catch (SQLException e) {
            throw new IllegalStateException("Gagal mengambil distribusi petugas: " + e.getMessage(), e);
        }
    }

    @Override
    public List<DetailDistribusi> findDetails(String idDistribusi) {
        List<DetailDistribusi> hasil = new ArrayList<>();
        String sql = "SELECT id_detail_distribusi, id_agen, id_distribusi, jenis_produk, "
                + "jumlah_pesan, tanggal_pesan FROM detail_distribusi "
                + "WHERE id_distribusi = ? ORDER BY id_detail_distribusi";
        try (Connection c = DatabaseConnection.bukaKoneksi();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, idDistribusi);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    hasil.add(new DetailDistribusi(rs.getString("id_detail_distribusi"),
                            rs.getString("id_agen"), rs.getString("id_distribusi"),
                            JenisProduk.fromDatabase(rs.getString("jenis_produk")),
                            rs.getInt("jumlah_pesan"), rs.getDate("tanggal_pesan").toLocalDate()));
                }
            }
            return hasil;
        } catch (SQLException e) {
            throw new IllegalStateException("Gagal mengambil detail distribusi: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(Distribusi distribusi) {
        updateTanggalKirim(distribusi.getIdDistribusi(), distribusi.getTanggalKirim());
    }

    @Override
    public void updateTanggalKirim(String idDistribusi, java.time.LocalDate tanggalKirim) {
        String sql = "UPDATE distribusi SET tanggal_kirim = ? WHERE id_distribusi = ? "
                + "AND status_kirim = 'Dalam Pengiriman'";
        try (Connection c = DatabaseConnection.bukaKoneksi();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDate(1, java.sql.Date.valueOf(tanggalKirim));
            ps.setString(2, idDistribusi);
            if (ps.executeUpdate() == 0) {
                throw new IllegalArgumentException("Distribusi tidak ditemukan atau statusnya sudah final.");
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Gagal mengubah tanggal distribusi: " + e.getMessage(), e);
        }
    }

    @Override
    public void ubahStatus(String idDistribusi, StatusKirim statusBaru, boolean barangSudahKembali) {
        if (statusBaru == null) {
            throw new IllegalArgumentException("Status pengiriman wajib dipilih.");
        }
        if (statusBaru == StatusKirim.TIDAK_TERKIRIM && !barangSudahKembali) {
            throw new IllegalArgumentException(
                    "Produk hanya dikembalikan ke stok setelah diterima kembali oleh gudang.");
        }

        try (Connection c = DatabaseConnection.bukaKoneksi()) {
            c.setAutoCommit(false);
            try {
                StatusKirim statusLama;
                try (PreparedStatement ps = c.prepareStatement(
                        "SELECT status_kirim FROM distribusi WHERE id_distribusi = ? FOR UPDATE")) {
                    ps.setString(1, idDistribusi);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) throw new IllegalArgumentException("Distribusi tidak ditemukan.");
                        statusLama = StatusKirim.fromDatabase(rs.getString(1));
                    }
                }

                if (statusLama == statusBaru) {
                    c.commit();
                    return;
                }

                // Jika transaksi yang sebelumnya tidak terkirim dibuka kembali,
                // stok harus dialokasikan lagi sebelum status berubah.
                if (statusLama == StatusKirim.TIDAK_TERKIRIM
                        && statusBaru != StatusKirim.TIDAK_TERKIRIM) {
                    alokasikanKembaliProduk(c, idDistribusi);
                }

                // Jika status diubah menjadi tidak terkirim, barang dianggap sudah
                // diterima kembali oleh gudang dan dilepas dari alokasi distribusi.
                if (statusBaru == StatusKirim.TIDAK_TERKIRIM
                        && statusLama != StatusKirim.TIDAK_TERKIRIM) {
                    lepaskanAlokasiProduk(c, idDistribusi);
                }

                try (PreparedStatement ps = c.prepareStatement(
                        "UPDATE distribusi SET status_kirim = ? WHERE id_distribusi = ?")) {
                    ps.setString(1, statusBaru.getNilaiDatabase());
                    ps.setString(2, idDistribusi);
                    if (ps.executeUpdate() == 0) {
                        throw new IllegalArgumentException("Distribusi tidak ditemukan saat memperbarui status.");
                    }
                }
                c.commit();
            } catch (SQLException | RuntimeException e) {
                c.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Gagal mengubah status distribusi: " + e.getMessage(), e);
        }
    }

    /** Mengalokasikan kembali stok berdasarkan rincian pesanan yang tersimpan. */
    private void alokasikanKembaliProduk(Connection c, String idDistribusi) throws SQLException {
        String sql = "SELECT jenis_produk, jumlah_pesan FROM detail_distribusi WHERE id_distribusi = ?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, idDistribusi);
            try (ResultSet rs = ps.executeQuery()) {
                boolean adaDetail = false;
                while (rs.next()) {
                    adaDetail = true;
                    JenisProduk jenis = JenisProduk.fromDatabase(rs.getString("jenis_produk"));
                    int jumlah = rs.getInt("jumlah_pesan");
                    alokasikanProduk(c, idDistribusi, jenis, jumlah);
                }
                if (!adaDetail) {
                    throw new IllegalArgumentException("Rincian distribusi tidak ditemukan. Stok tidak dapat dialokasikan kembali.");
                }
            }
        }
    }

    /** Melepaskan seluruh stok yang terhubung ke transaksi distribusi. */
    private void lepaskanAlokasiProduk(Connection c, String idDistribusi) throws SQLException {
        String[] tabel = {"produk_botol", "produk_galon"};
        for (String namaTabel : tabel) {
            try (PreparedStatement ps = c.prepareStatement(
                    "UPDATE " + namaTabel + " SET id_distribusi = NULL WHERE id_distribusi = ?")) {
                ps.setString(1, idDistribusi);
                ps.executeUpdate();
            }
        }
    }

    @Override
    public void delete(String id) {
        throw new IllegalArgumentException("Riwayat distribusi tidak dapat dihapus. Data transaksi dan Detail_Distribusi harus tetap tersimpan.");
    }

    @Override
    public String generateIdBaru() {
        return generateIdTerkecil("distribusi", "id_distribusi", "DST");
    }

    @Override
    public List<String> generateIdDetailBaru(int jumlah) {
        if (jumlah <= 0) throw new IllegalArgumentException("Jumlah ID harus lebih besar dari 0.");
        Set<Integer> nomorTerpakai = new HashSet<>();
        try (Connection c = DatabaseConnection.bukaKoneksi();
             PreparedStatement ps = c.prepareStatement("SELECT id_detail_distribusi FROM detail_distribusi");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String id = rs.getString(1);
                if (id != null && id.startsWith("DTL")) {
                    try { nomorTerpakai.add(Integer.parseInt(id.substring(3))); }
                    catch (NumberFormatException e) { /* Abaikan ID lama yang tidak sesuai pola. */ }
                }
            }
            List<String> hasil = new ArrayList<>();
            int nomor = 1;
            while (hasil.size() < jumlah) {
                if (!nomorTerpakai.contains(nomor)) {
                    hasil.add(String.format("DTL%03d", nomor));
                    nomorTerpakai.add(nomor);
                }
                nomor++;
            }
            return hasil;
        } catch (SQLException e) {
            throw new IllegalStateException("Gagal membuat ID Detail_Distribusi: " + e.getMessage(), e);
        }
    }

    private String generateIdTerkecil(String tabel, String kolom, String prefix) {
        Set<Integer> nomorTerpakai = new HashSet<>();
        String sql = "SELECT " + kolom + " FROM " + tabel;
        try (Connection c = DatabaseConnection.bukaKoneksi();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String id = rs.getString(1);
                if (id != null && id.startsWith(prefix)) {
                    try { nomorTerpakai.add(Integer.parseInt(id.substring(prefix.length()))); }
                    catch (NumberFormatException e) { /* Abaikan ID lama yang tidak sesuai pola. */ }
                }
            }
            int nomor = 1;
            while (nomorTerpakai.contains(nomor)) nomor++;
            return String.format(prefix + "%03d", nomor);
        } catch (SQLException e) {
            throw new IllegalStateException("Gagal membuat ID " + prefix + ": " + e.getMessage(), e);
        }
    }

    private Distribusi mapDistribusi(ResultSet rs) throws SQLException {
        return new Distribusi(rs.getString("id_distribusi"),
                rs.getDate("tanggal_kirim").toLocalDate(),
                StatusKirim.fromDatabase(rs.getString("status_kirim")),
                rs.getString("Petugas_Gudang_id_petugas"),
                rs.getString("Petugas_Distribusi_id_petugas"));
    }
}
