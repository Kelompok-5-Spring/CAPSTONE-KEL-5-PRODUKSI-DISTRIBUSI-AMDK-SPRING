package view.panel;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import controller.ProdukController;
import model.Produk;
import model.ProdukBotol;
import model.ProdukGalon;
import model.type.JenisProduk;

/** Panel data produk. Produk baru berasal otomatis dari produksi berstatus Selesai. */
public class ProdukPanel extends JPanel {
    private final ProdukController controller;
    private final boolean bolehHapus;
    private final boolean bolehUbah;

    private final JTextField id = new JTextField();
    private final JTextField nama = new JTextField();
    private final JTextField harga = new JTextField();
    private final JTextField volume = new JTextField();
    private final JTextField detail = new JTextField();
    private final JTextField idProduksi = new JTextField();
    private final JTextField idDistribusi = new JTextField();
    private final JComboBox<JenisProduk> jenis = new JComboBox<>(JenisProduk.values());

    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"ID Produk", "Nama", "Harga", "Jenis", "Kemasan", "ID Produksi", "ID Distribusi"}, 0) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };
    private final JTable tabel = new JTable(model);

    public ProdukPanel(ProdukController controller, boolean bolehHapus, boolean bolehUbah) {
        super(new BorderLayout(8, 8));
        this.controller = controller;
        this.bolehHapus = bolehHapus;
        this.bolehUbah = bolehUbah;

        JPanel form = new JPanel(new GridLayout(0, 2, 6, 6));
        id.setEditable(false);
        idProduksi.setEditable(false);
        idDistribusi.setEditable(false);
        form.add(new JLabel("ID produk")); form.add(id);
        form.add(new JLabel("Nama produk")); form.add(nama);
        form.add(new JLabel("Harga satuan")); form.add(harga);
        form.add(new JLabel("Jenis produk")); form.add(jenis);
        form.add(new JLabel("Volume (ml / liter)")); form.add(volume);
        form.add(new JLabel("Pcs per dus / jenis galon")); form.add(detail);
        form.add(new JLabel("ID produksi asal")); form.add(idProduksi);
        form.add(new JLabel("ID distribusi")); form.add(idDistribusi);
        form.add(new JLabel("Catatan")); form.add(new JLabel("Produk baru dibuat otomatis saat produksi selesai."));
        add(form, BorderLayout.NORTH);

        tabel.setRowHeight(26);
        add(new JScrollPane(tabel), BorderLayout.CENTER);

        JPanel tombol = new JPanel();
        JButton ubah = new JButton("Ubah Produk");
        JButton hapus = new JButton("Hapus Produk");
        ubah.setEnabled(bolehUbah);
        hapus.setEnabled(bolehHapus);
        tombol.add(ubah);
        tombol.add(hapus);
        add(tombol, BorderLayout.SOUTH);

        ubah.addActionListener(e -> {
            if (id.getText().isBlank()) {
                pesan("Pilih produk yang akan diubah terlebih dahulu.", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!konfirmasi("Simpan perubahan data produk?")) return;
            jalankan(() -> controller.ubahProduk(id.getText().trim(), nama.getText(),
                    harga.getText(), (JenisProduk) jenis.getSelectedItem(),
                    volume.getText(), detail.getText()));
        });

        hapus.addActionListener(e -> {
            if (id.getText().isBlank()) {
                pesan("Pilih produk yang akan dihapus terlebih dahulu.", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!konfirmasi("Produk terpilih akan dihapus. Lanjutkan?")) return;
            jalankan(() -> {
                controller.hapusProduk(id.getText().trim());
                kosongkanForm();
            });
        });

        tabel.getSelectionModel().addListSelectionListener(e -> {
            int row = tabel.getSelectedRow();
            if (!e.getValueIsAdjusting() && row >= 0) pilihProduk(String.valueOf(model.getValueAt(row, 0)));
        });
        loadData();
    }

    private void pilihProduk(String idProduk) {
        Produk p = controller.cariProduk(idProduk);
        if (p == null) return;

        id.setText(p.getIdProduk());
        nama.setText(p.getNamaProduk());
        harga.setText(p.getHargaSatuan().toPlainString());
        jenis.setSelectedItem(p.getJenisProduk());
        idProduksi.setText(p.getIdProduksi());
        idDistribusi.setText(p.getIdDistribusi() == null ? "" : p.getIdDistribusi());

        if (p instanceof ProdukBotol) {
            ProdukBotol botol = (ProdukBotol) p;
            volume.setText(botol.getVolumeMl().stripTrailingZeros().toPlainString());
            detail.setText(String.valueOf(botol.getPcsPerDus()));
        } else if (p instanceof ProdukGalon) {
            ProdukGalon galon = (ProdukGalon) p;
            volume.setText(galon.getVolumeLiter().stripTrailingZeros().toPlainString());
            detail.setText(galon.getJenisGalon());
        }
    }

    private boolean konfirmasi(String isi) {
        return JOptionPane.showConfirmDialog(this, isi, "Konfirmasi",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;
    }

    private void jalankan(Runnable aksi) {
        try {
            aksi.run();
            loadData();
            pesan("Data produk berhasil diproses.", JOptionPane.INFORMATION_MESSAGE);
        } catch (RuntimeException ex) {
            pesan(ex.getMessage(), JOptionPane.ERROR_MESSAGE);
        }
    }

    private void pesan(String isi, int tipe) {
        JOptionPane.showMessageDialog(this, isi, "Informasi", tipe);
    }

    private void kosongkanForm() {
        id.setText("");
        nama.setText("");
        harga.setText("");
        volume.setText("");
        detail.setText("");
        idProduksi.setText("");
        idDistribusi.setText("");
        tabel.clearSelection();
    }

    private void loadData() {
        model.setRowCount(0);
        for (Produk p : controller.loadProduk()) {
            model.addRow(new Object[]{p.getIdProduk(), p.getNamaProduk(), p.getHargaSatuan(),
                    p.getJenisProduk().getNamaProduk(), p.getDeskripsiKemasan(),
                    p.getIdProduksi(), p.getIdDistribusi()});
        }
    }
}
