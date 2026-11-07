package view.panel;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.time.LocalDate;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import controller.ProduksiController;
import controller.ProdukController;
import model.Produksi;
import model.type.StatusProduksi;

/** Panel pencatatan produksi dengan ID otomatis dan method sesuai ProduksiController. */
public class ProduksiPanel extends JPanel {
    private final ProduksiController controller;
    private final boolean bolehKelola;

    private final JTextField id = new JTextField();
    private final JComboBox<String> jenis = new JComboBox<>(new String[]{
        "Spring Botol 600ml", "Spring Botol 1500ml",
        "Spring Galon 5L", "Spring Galon 19L"});
    private final JTextField tanggal = new JTextField(LocalDate.now().toString());
    private final JTextField jumlah = new JTextField();
    private final JTextField idPetugas = new JTextField();
    private final JComboBox<StatusProduksi> status = new JComboBox<>(StatusProduksi.values());

    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"ID Produksi", "Jenis Produksi", "Tanggal", "Jumlah", "Status", "ID Petugas"}, 0) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };
    private final JTable tabel = new JTable(model);

    /** produkController dipertahankan agar pemanggil View lama tetap kompatibel. */
    public ProduksiPanel(ProduksiController controller, ProdukController produkController,
            boolean bolehKelola) {
        super(new BorderLayout(8, 8));
        this.controller = controller;
        this.bolehKelola = bolehKelola;

        JPanel form = new JPanel(new GridLayout(0, 2, 6, 6));
        id.setEditable(false);
        id.setToolTipText("ID produksi dibuat otomatis oleh sistem.");
        status.setEnabled(false);
        form.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        form.add(new JLabel("ID produksi (otomatis)")); form.add(id);
        form.add(new JLabel("Jenis produk")); form.add(jenis);
        form.add(new JLabel("Tanggal (yyyy-MM-dd)")); form.add(tanggal);
        form.add(new JLabel("Jumlah produksi")); form.add(jumlah);
        form.add(new JLabel("ID petugas produksi")); form.add(idPetugas);
        form.add(new JLabel("Status produksi")); form.add(status);
        add(form, BorderLayout.NORTH);

        tabel.setRowHeight(26);
        add(new JScrollPane(tabel), BorderLayout.CENTER);

        JPanel tombol = new JPanel();
        JButton tambah = new JButton("Tambah Produksi");
        JButton ubah = new JButton("Ubah Data");
        JButton ubahStatus = new JButton("Ubah Status");
        JButton hapus = new JButton("Hapus");
        for (JButton b : new JButton[]{tambah, ubah, ubahStatus, hapus}) b.setEnabled(bolehKelola);
        tombol.add(tambah);
        tombol.add(ubah);
        tombol.add(ubahStatus);
        tombol.add(hapus);
        add(tombol, BorderLayout.SOUTH);

        tambah.addActionListener(e -> {
            if (!konfirmasi("Simpan pencatatan produksi baru?")) return;
            jalankan(() -> {
                Produksi baru = controller.simpanProduksi((String) jenis.getSelectedItem(),
                        LocalDate.parse(tanggal.getText().trim()), jumlah.getText().trim(),
                        idPetugas.getText().trim());
                id.setText(baru.getIdProduksi());
            });
        });

        ubah.addActionListener(e -> {
            if (id.getText().isBlank()) {
                pesan("Pilih data produksi yang akan diubah.", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!konfirmasi("Simpan perubahan data produksi?")) return;
            jalankan(() -> controller.ubahProduksi(id.getText().trim(),
                    (String) jenis.getSelectedItem(), LocalDate.parse(tanggal.getText().trim()),
                    jumlah.getText().trim(), idPetugas.getText().trim()));
        });

        ubahStatus.addActionListener(e -> {
            if (id.getText().isBlank()) {
                pesan("Pilih data produksi terlebih dahulu.", JOptionPane.WARNING_MESSAGE);
                return;
            }
            StatusProduksi pilihan = (StatusProduksi) JOptionPane.showInputDialog(this,
                    "Pilih status produksi baru:", "Ubah Status Produksi",
                    JOptionPane.QUESTION_MESSAGE, null, StatusProduksi.values(),
                    status.getSelectedItem());
            if (pilihan == null) return;
            if (pilihan == StatusProduksi.Selesai) {
                if (!konfirmasi("Produksi akan diselesaikan dan sistem otomatis membuat record produk. Lanjutkan?")) return;
            } else if (!konfirmasi("Ubah status produksi menjadi " + pilihan + "?")) {
                return;
            }
            jalankan(() -> controller.ubahStatusProduksi(id.getText().trim(), pilihan));
        });

        hapus.addActionListener(e -> {
            if (id.getText().isBlank()) {
                pesan("Pilih data produksi yang akan dihapus.", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!konfirmasi("Data produksi terpilih akan dihapus. Lanjutkan?")) return;
            jalankan(() -> {
                controller.hapusProduksi(id.getText().trim());
                kosongkanForm();
            });
        });

        tabel.getSelectionModel().addListSelectionListener(e -> {
            int row = tabel.getSelectedRow();
            if (!e.getValueIsAdjusting() && row >= 0) pilihProduksi(String.valueOf(model.getValueAt(row, 0)));
        });
        loadData();
    }

    private void pilihProduksi(String idProduksi) {
        Produksi p = controller.cariProduksi(idProduksi);
        if (p == null) return;
        id.setText(p.getIdProduksi());
        jenis.setSelectedItem(p.getJenisProduksi());
        tanggal.setText(p.getTanggalProduksi().toString());
        jumlah.setText(String.valueOf(p.getJumlahProduksi()));
        idPetugas.setText(p.getIdPetugasProduksi());
        status.setSelectedItem(p.getStatusProduksi());
    }

    private boolean konfirmasi(String isi) {
        return JOptionPane.showConfirmDialog(this, isi, "Konfirmasi",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;
    }

    private void jalankan(Runnable aksi) {
        try {
            aksi.run();
            loadData();
            pesan("Data produksi berhasil diproses.", JOptionPane.INFORMATION_MESSAGE);
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
        jumlah.setText("");
        idPetugas.setText("");
        status.setSelectedItem(StatusProduksi.Direncanakan);
        tabel.clearSelection();
    }

    private void loadData() {
        model.setRowCount(0);
        for (Produksi p : controller.loadProduksi()) {
            model.addRow(new Object[]{p.getIdProduksi(), p.getJenisProduksi(),
                    p.getTanggalProduksi(), p.getJumlahProduksi(),
                    p.getStatusProduksi(), p.getIdPetugasProduksi()});
        }
    }
}
