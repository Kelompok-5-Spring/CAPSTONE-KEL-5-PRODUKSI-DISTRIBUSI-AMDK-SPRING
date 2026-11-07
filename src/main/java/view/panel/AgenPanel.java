package view.panel;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import controller.AgenController;
import model.Agen;

/** Panel CRUD agen yang menggunakan method AgenController. */
public class AgenPanel extends JPanel {
    private final AgenController controller;
    private final boolean bolehKelola;
    private final JTextField id = new JTextField();
    private final JTextField nama = new JTextField();
    private final JTextField telepon = new JTextField();
    private final JTextField alamat = new JTextField();
    private final JTextField pencarian = new JTextField(18);
    private final javax.swing.JComboBox<String> kecamatan = new javax.swing.JComboBox<>(new String[]{
        "Semua Kecamatan", "Palaran", "Samarinda Ilir", "Samarinda Kota",
        "Samarinda Utara", "Samarinda Ulu", "Sungai Kunjang"});
    private final javax.swing.JComboBox<String> kecamatanInput = new javax.swing.JComboBox<>(new String[]{
        "Palaran", "Samarinda Ilir", "Samarinda Kota",
        "Samarinda Utara", "Samarinda Ulu", "Sungai Kunjang"});

    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"ID Agen", "Nama Agen", "No. Telepon", "Alamat Lengkap"}, 0) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };
    private final JTable tabel = new JTable(model);

    public AgenPanel(AgenController controller, boolean bolehKelola) {
        super(new BorderLayout(8, 8));
        this.controller = controller;
        this.bolehKelola = bolehKelola;

        JPanel form = new JPanel(new GridLayout(0, 2, 6, 6));
        form.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        id.setEditable(false);
        id.setToolTipText("ID agen dibuat otomatis oleh sistem.");
        form.add(new JLabel("ID agen (otomatis)")); form.add(id);
        form.add(new JLabel("Nama agen")); form.add(nama);
        form.add(new JLabel("Nomor telepon")); form.add(telepon);
        form.add(new JLabel("Alamat jalan")); form.add(alamat);
        form.add(new JLabel("Kecamatan")); form.add(kecamatanInput);
        JPanel areaAtas = new JPanel(new BorderLayout(6, 6));
        JPanel filter = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        filter.add(new JLabel("Cari agen:"));
        filter.add(pencarian);
        filter.add(new JLabel("Kecamatan:"));
        filter.add(kecamatan);
        areaAtas.add(filter, BorderLayout.NORTH);
        areaAtas.add(form, BorderLayout.CENTER);
        add(areaAtas, BorderLayout.NORTH);

        pencarian.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { loadData(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { loadData(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { loadData(); }
        });
        kecamatan.addActionListener(e -> loadData());

        tabel.setRowHeight(26);
        add(new JScrollPane(tabel), BorderLayout.CENTER);

        JPanel tombol = new JPanel();
        JButton tambah = new JButton("Tambah");
        JButton ubah = new JButton("Ubah");
        JButton hapus = new JButton("Hapus");
        tambah.setEnabled(bolehKelola);
        ubah.setEnabled(bolehKelola);
        hapus.setEnabled(bolehKelola);
        tombol.add(tambah);
        tombol.add(ubah);
        tombol.add(hapus);
        add(tombol, BorderLayout.SOUTH);

        tambah.addActionListener(e -> {
            if (!konfirmasi("Simpan data agen baru?")) return;
            jalankan(() -> {
                Agen baru = controller.simpanAgen(
                        nama.getText(), telepon.getText(), alamatLengkapDenganKecamatan());
                id.setText(baru.getIdAgen());
            });
        });

        ubah.addActionListener(e -> {
            if (id.getText().isBlank()) {
                pesan("Pilih data agen yang akan diubah terlebih dahulu.", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!konfirmasi("Simpan perubahan data agen ini?")) return;
            jalankan(() -> controller.ubahAgen(
                    id.getText().trim(), nama.getText(), telepon.getText(),
                    alamatLengkapDenganKecamatan()));
        });

        hapus.addActionListener(e -> {
            if (id.getText().isBlank()) {
                pesan("Pilih data agen yang akan dihapus terlebih dahulu.", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!konfirmasi("Data agen terpilih akan dihapus. Lanjutkan?")) return;
            jalankan(() -> {
                controller.hapusAgen(id.getText().trim());
                kosongkanForm();
            });
        });

        tabel.getSelectionModel().addListSelectionListener(e -> {
            int row = tabel.getSelectedRow();
            if (!e.getValueIsAdjusting() && row >= 0) {
                id.setText(String.valueOf(model.getValueAt(row, 0)));
                nama.setText(String.valueOf(model.getValueAt(row, 1)));
                telepon.setText(String.valueOf(model.getValueAt(row, 2)));
                alamat.setText(String.valueOf(model.getValueAt(row, 3)));
                Agen agenTerpilih = controller.cariAgen(String.valueOf(model.getValueAt(row, 0)));
                if (agenTerpilih != null && agenTerpilih.ambilKecamatan() != null) {
                    kecamatanInput.setSelectedItem(agenTerpilih.ambilKecamatan());
                }
            }
        });
        loadData();
    }

    public void fokusTambahAgen() {
        kosongkanForm();
        nama.requestFocusInWindow();
    }

    private boolean konfirmasi(String pesan) {
        return JOptionPane.showConfirmDialog(this, pesan, "Konfirmasi",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;
    }

    private void jalankan(Runnable aksi) {
        try {
            aksi.run();
            loadData();
            pesan("Data agen berhasil diproses.", JOptionPane.INFORMATION_MESSAGE);
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
        telepon.setText("");
        alamat.setText("");
        kecamatanInput.setSelectedIndex(0);
        tabel.clearSelection();
    }

    private String alamatLengkapDenganKecamatan() {
        String alamatDasar = alamat.getText().trim();
        if (alamatDasar.isBlank()) {
            throw new IllegalArgumentException("Alamat jalan wajib diisi.");
        }
        // Hilangkan kecamatan lama ketika data agen diedit, lalu gunakan pilihan terbaru.
        alamatDasar = alamatDasar.replaceAll("(?i),?\\s*Kec\\.\\s*[^,]+", "");
        alamatDasar = alamatDasar.replaceAll("(?i),?\\s*Samarinda\\s*$", "").trim();
        String wilayah = String.valueOf(kecamatanInput.getSelectedItem());
        return alamatDasar + ", Kec. " + wilayah + ", Samarinda";
    }

    private void loadData() {
        try {
            model.setRowCount(0);
            String cari = pencarian.getText().trim();
            String wilayah = String.valueOf(kecamatan.getSelectedItem());
            List<Agen> data = (!cari.isBlank() && !"Semua Kecamatan".equals(wilayah))
                    ? controller.cariAgen(cari, wilayah) : controller.loadAgen();
            for (Agen agen : data) {
                boolean cocokNama = cari.isBlank() || agen.getNamaAgen().toLowerCase(java.util.Locale.ROOT)
                        .contains(cari.toLowerCase(java.util.Locale.ROOT));
                boolean cocokWilayah = "Semua Kecamatan".equals(wilayah)
                        || (agen.ambilKecamatan() != null && agen.ambilKecamatan().equalsIgnoreCase(wilayah));
                if (cocokNama && cocokWilayah) {
                    model.addRow(new Object[]{agen.getIdAgen(), agen.getNamaAgen(),
                            agen.getNoTelp(), agen.getAlamatLengkap()});
                }
            }
        } catch (RuntimeException ex) {
            pesan(ex.getMessage(), JOptionPane.ERROR_MESSAGE);
        }
    }
}
