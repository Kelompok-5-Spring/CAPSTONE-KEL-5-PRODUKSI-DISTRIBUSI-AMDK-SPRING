package view.panel;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import controller.DistribusiController;
import controller.ProdukController;
import model.DetailDistribusi;
import model.Distribusi;
import model.type.JenisProduk;
import model.type.StatusKirim;

/** Panel transaksi distribusi. ID dan petugas ditentukan otomatis oleh backend. */
public class DistribusiPanel extends JPanel {
    private final DistribusiController controller;
    private final boolean bolehKelola;
    private final boolean hanyaStatus;

    private final JTextField id = new JTextField();
    private final JTextField tanggal = new JTextField(LocalDate.now().toString());
    private final JComboBox<StatusKirim> status = new JComboBox<>(StatusKirim.values());
    private final JTextField idAgen = new JTextField();
    private final JComboBox<JenisProduk> jenisProduk = new JComboBox<>(JenisProduk.values());
    private final JTextField jumlahPesan = new JTextField();
    private final JTextField tanggalPesan = new JTextField(LocalDate.now().toString());

    private final DefaultTableModel detailModel = new DefaultTableModel(
            new String[]{"ID Detail", "ID Agen", "Jenis Produk", "Jumlah", "Tanggal Pesan"}, 0) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };
    private final JTable tabelDetail = new JTable(detailModel);
    private final List<DetailDistribusi> daftarDetail = new ArrayList<>();

    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"ID Distribusi", "Tanggal Kirim", "Status", "Petugas Gudang", "Petugas Distribusi"}, 0) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };
    private final JTable tabel = new JTable(model);

    /**
     * Parameter produkController dipertahankan agar pemanggil View lama tidak rusak.
     * Backend menetapkan ID dan petugas distribusi secara otomatis.
     */
    public DistribusiPanel(DistribusiController controller, ProdukController produkController,
            boolean bolehKelola, boolean hanyaStatus) {
        super(new BorderLayout(8, 8));
        this.controller = controller;
        this.bolehKelola = bolehKelola;
        this.hanyaStatus = hanyaStatus;

        JPanel header = new JPanel(new GridLayout(0, 2, 6, 6));
        header.setBorder(BorderFactory.createTitledBorder("Informasi Distribusi"));
        id.setEditable(false);
        id.setToolTipText("ID distribusi dibuat otomatis oleh sistem.");
        header.add(new JLabel("ID distribusi (otomatis)")); header.add(id);
        header.add(new JLabel("Tanggal kirim (yyyy-MM-dd)")); header.add(tanggal);
        header.add(new JLabel("Status kirim")); header.add(status);

        JPanel detailInput = new JPanel(new GridLayout(0, 2, 6, 6));
        detailInput.setBorder(BorderFactory.createTitledBorder("Detail Pesanan Agen"));
        detailInput.add(new JLabel("ID agen")); detailInput.add(idAgen);
        detailInput.add(new JLabel("Jenis produk")); detailInput.add(jenisProduk);
        detailInput.add(new JLabel("Jumlah pesan")); detailInput.add(jumlahPesan);
        detailInput.add(new JLabel("Tanggal pesan (yyyy-MM-dd)")); detailInput.add(tanggalPesan);

        JButton tambahDetail = new JButton("Tambah Detail");
        JButton hapusDetail = new JButton("Hapus Detail Terpilih");
        JPanel detailActions = new JPanel();
        detailActions.add(tambahDetail);
        detailActions.add(hapusDetail);

        JPanel detailPanel = new JPanel(new BorderLayout(4, 4));
        detailPanel.add(detailInput, BorderLayout.NORTH);
        tabelDetail.setRowHeight(24);
        detailPanel.add(new JScrollPane(tabelDetail), BorderLayout.CENTER);
        detailPanel.add(detailActions, BorderLayout.SOUTH);

        JPanel atas = new JPanel(new BorderLayout(6, 6));
        atas.add(header, BorderLayout.NORTH);
        atas.add(detailPanel, BorderLayout.CENTER);
        add(atas, BorderLayout.NORTH);

        tabel.setRowHeight(25);
        add(new JScrollPane(tabel), BorderLayout.CENTER);

        JButton simpan = new JButton("Simpan Distribusi");
        JButton ubahTanggal = new JButton("Ubah Tanggal");
        JButton ubahStatus = new JButton("Ubah Status");
        JButton hapus = new JButton("Hapus Distribusi");

        simpan.setEnabled(bolehKelola);
        ubahTanggal.setEnabled(bolehKelola);
        hapus.setEnabled(bolehKelola);
        tambahDetail.setEnabled(bolehKelola);
        hapusDetail.setEnabled(bolehKelola);
        ubahStatus.setEnabled(bolehKelola || hanyaStatus);

        JPanel tombol = new JPanel();
        tombol.add(simpan);
        tombol.add(ubahTanggal);
        tombol.add(ubahStatus);
        tombol.add(hapus);
        add(tombol, BorderLayout.SOUTH);

        tambahDetail.addActionListener(e -> tambahDetailKeDaftar());
        hapusDetail.addActionListener(e -> hapusDetailTerpilih());

        simpan.addActionListener(e -> {
            if (daftarDetail.isEmpty()) {
                pesan("Tambahkan minimal satu detail pesanan.", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!konfirmasi("Simpan transaksi distribusi beserta seluruh detail pesanan?")) return;
            jalankan(() -> {
                Distribusi hasil = controller.simpanDistribusi(
                        LocalDate.parse(tanggal.getText().trim()), new ArrayList<>(daftarDetail));
                id.setText(hasil.getIdDistribusi());
                daftarDetail.clear();
                detailModel.setRowCount(0);
            });
        });

        ubahTanggal.addActionListener(e -> {
            if (id.getText().isBlank()) {
                pesan("Pilih transaksi distribusi terlebih dahulu.", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!konfirmasi("Simpan perubahan tanggal pengiriman?")) return;
            jalankan(() -> controller.ubahTanggalKirim(
                    id.getText().trim(), LocalDate.parse(tanggal.getText().trim())));
        });

        ubahStatus.addActionListener(e -> ubahStatusTerpilih());

        hapus.addActionListener(e -> {
            if (id.getText().isBlank()) {
                pesan("Pilih transaksi distribusi yang akan dihapus.", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!konfirmasi("Transaksi distribusi terpilih akan dihapus. Lanjutkan?")) return;
            jalankan(() -> {
                controller.hapusDistribusi(id.getText().trim());
                kosongkanForm();
            });
        });

        tabel.getSelectionModel().addListSelectionListener(e -> {
            int row = tabel.getSelectedRow();
            if (!e.getValueIsAdjusting() && row >= 0) pilihDistribusi(row);
        });

        if (!bolehKelola) {
            tanggal.setEditable(false);
            idAgen.setEditable(false);
            jumlahPesan.setEditable(false);
            tanggalPesan.setEditable(false);
            jenisProduk.setEnabled(false);
        }
        loadData();
    }

    private void tambahDetailKeDaftar() {
        try {
            String agen = idAgen.getText().trim();
            JenisProduk jenis = (JenisProduk) jenisProduk.getSelectedItem();
            int jumlah = Integer.parseInt(jumlahPesan.getText().trim());
            LocalDate tanggalDetail = LocalDate.parse(tanggalPesan.getText().trim());

            for (DetailDistribusi item : daftarDetail) {
                if (item.getIdAgen().equals(agen) && item.getJenisProduk() == jenis) {
                    throw new IllegalArgumentException(
                            "Agen dan jenis produk yang sama sudah ada. Gabungkan jumlah pesan.");
                }
            }

            DetailDistribusi detail = new DetailDistribusi(agen, jenis, jumlah, tanggalDetail);
            daftarDetail.add(detail);
            detailModel.addRow(new Object[]{(daftarDetail.size()), detail.getIdAgen(),
                    detail.getJenisProduk().getNamaProduk(), detail.getJumlahPesan(),
                    detail.getTanggalPesan()});
            idAgen.setText("");
            jumlahPesan.setText("");
        } catch (RuntimeException ex) {
            pesan(ex.getMessage(), JOptionPane.ERROR_MESSAGE);
        }
    }

    private void hapusDetailTerpilih() {
        int row = tabelDetail.getSelectedRow();
        if (row < 0) {
            pesan("Pilih detail pesanan yang akan dihapus.", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!konfirmasi("Hapus detail pesanan terpilih dari daftar?")) return;
        daftarDetail.remove(row);
        detailModel.removeRow(row);
        detailModel.setRowCount(0);
        for (int i = 0; i < daftarDetail.size(); i++) {
            DetailDistribusi d = daftarDetail.get(i);
            detailModel.addRow(new Object[]{i + 1, d.getIdAgen(),
                    d.getJenisProduk().getNamaProduk(), d.getJumlahPesan(), d.getTanggalPesan()});
        }
    }

    private void pilihDistribusi(int row) {
        String idDipilih = String.valueOf(model.getValueAt(row, 0));
        Distribusi dipilih = controller.cariDistribusi(idDipilih);
        if (dipilih == null) return;

        id.setText(dipilih.getIdDistribusi());
        tanggal.setText(dipilih.getTanggalKirim().toString());
        status.setSelectedItem(dipilih.getStatusKirim());

        detailModel.setRowCount(0);
        daftarDetail.clear();
        for (DetailDistribusi detail : controller.loadDetailDistribusi(idDipilih)) {
            daftarDetail.add(detail);
            detailModel.addRow(new Object[]{detail.getIdDetailDistribusi(), detail.getIdAgen(),
                    detail.getJenisProduk().getNamaProduk(), detail.getJumlahPesan(),
                    detail.getTanggalPesan()});
        }
    }

    private void ubahStatusTerpilih() {
        if (id.getText().isBlank()) {
            pesan("Pilih transaksi distribusi terlebih dahulu.", JOptionPane.WARNING_MESSAGE);
            return;
        }
        StatusKirim pilihan = (StatusKirim) status.getSelectedItem();
        if (pilihan == null || pilihan == StatusKirim.DALAM_PENGIRIMAN) {
            pesan("Pilih status akhir: Terkirim atau Tidak Terkirim.", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean barangKembali = false;
        if (pilihan == StatusKirim.TIDAK_TERKIRIM) {
            int konfirmasiKembali = JOptionPane.showConfirmDialog(this,
                    "Apakah seluruh produk sudah kembali ke gudang?",
                    "Konfirmasi pengembalian barang", JOptionPane.YES_NO_OPTION);
            if (konfirmasiKembali != JOptionPane.YES_OPTION) return;
            barangKembali = true;
        }
        if (!konfirmasi("Ubah status transaksi distribusi menjadi " + pilihan + "?")) return;
        final boolean sudahKembali = barangKembali;
        jalankan(() -> controller.ubahStatusDistribusi(
                id.getText().trim(), pilihan, sudahKembali));
    }

    private boolean konfirmasi(String isi) {
        return JOptionPane.showConfirmDialog(this, isi, "Konfirmasi",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;
    }

    private void jalankan(Runnable aksi) {
        try {
            aksi.run();
            loadData();
            pesan("Transaksi distribusi berhasil diproses.", JOptionPane.INFORMATION_MESSAGE);
        } catch (RuntimeException ex) {
            pesan(ex.getMessage(), JOptionPane.ERROR_MESSAGE);
        }
    }

    private void pesan(String isi, int tipe) {
        JOptionPane.showMessageDialog(this, isi, "Informasi", tipe);
    }

    private void kosongkanForm() {
        id.setText("");
        tanggal.setText(LocalDate.now().toString());
        status.setSelectedItem(StatusKirim.DALAM_PENGIRIMAN);
        detailModel.setRowCount(0);
        daftarDetail.clear();
        tabel.clearSelection();
    }

    private void loadData() {
        model.setRowCount(0);
        for (Distribusi d : controller.loadDistribusi()) {
            model.addRow(new Object[]{d.getIdDistribusi(), d.getTanggalKirim(),
                    d.getStatusKirim().getNilaiDatabase(),
                    d.getIdPetugasGudang(), d.getIdPetugasDistribusi()});
        }
    }
}
