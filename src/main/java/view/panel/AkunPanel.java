package view.panel;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import controller.AkunController;
import model.Petugas;
import model.PetugasAdmin;
import model.PetugasDistribusi;
import model.PetugasGudang;
import model.PetugasProduksi;
import model.type.Role;

/** Panel pengelolaan akun yang memakai API AkunController. */
public class AkunPanel extends JPanel {
    private final AkunController controller;
    private final JTextField id = new JTextField();
    private final JTextField username = new JTextField();
    private final JTextField nama = new JTextField();
    private final JPasswordField password = new JPasswordField();
    private final JTextField detail = new JTextField();
    private final JComboBox<Role> role = new JComboBox<>(Role.values());

    private final DefaultTableModel tabelModel = new DefaultTableModel(
            new String[]{"ID", "Username", "Nama", "Role", "Spesialisasi / Wilayah"}, 0) {
        @Override public boolean isCellEditable(int row, int col) { return false; }
    };
    private final JTable tabel = new JTable(tabelModel);

    public AkunPanel(AkunController controller) {
        super(new BorderLayout(6, 6));
        this.controller = controller;

        JPanel form = new JPanel(new GridLayout(0, 2, 5, 5));
        id.setEditable(false);
        id.setToolTipText("ID petugas dibuat otomatis oleh sistem.");
        form.add(new JLabel("ID petugas (otomatis)")); form.add(id);
        form.add(new JLabel("Username")); form.add(username);
        form.add(new JLabel("Nama petugas")); form.add(nama);
        form.add(new JLabel("Password (wajib saat tambah)")); form.add(password);
        form.add(new JLabel("Role")); form.add(role);
        form.add(new JLabel("Spesialisasi / Wilayah")); form.add(detail);
        add(form, BorderLayout.NORTH);
        add(new JScrollPane(tabel), BorderLayout.CENTER);

        JPanel tombol = new JPanel();
        JButton tambah = new JButton("Tambah");
        JButton ubah = new JButton("Ubah");
        JButton hapus = new JButton("Hapus");
        tombol.add(tambah); tombol.add(ubah); tombol.add(hapus);
        add(tombol, BorderLayout.SOUTH);

        tambah.addActionListener(e -> {
            if (!konfirmasi("Simpan akun petugas baru?")) return;
            jalankan(() -> {
                Petugas baru = controller.simpanAkun(username.getText(), nama.getText(),
                        new String(password.getPassword()), (Role) role.getSelectedItem(), detail.getText());
                id.setText(baru.getIdPetugas());
            });
        });

        ubah.addActionListener(e -> {
            if (id.getText().isBlank()) {
                pesan("Pilih akun yang akan diubah terlebih dahulu.", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!konfirmasi("Simpan perubahan akun ini?")) return;
            jalankan(() -> controller.ubahAkun(id.getText().trim(), username.getText(),
                    nama.getText(), new String(password.getPassword()),
                    (Role) role.getSelectedItem(), detail.getText()));
        });

        hapus.addActionListener(e -> {
            if (id.getText().isBlank()) {
                pesan("Pilih akun yang akan dihapus terlebih dahulu.", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!konfirmasi("Akun terpilih akan dihapus. Lanjutkan?")) return;
            jalankan(() -> {
                controller.hapusAkun(id.getText().trim());
                kosongkanForm();
            });
        });

        tabel.getSelectionModel().addListSelectionListener(e -> {
            int row = tabel.getSelectedRow();
            if (!e.getValueIsAdjusting() && row >= 0) {
                String idPilih = String.valueOf(tabelModel.getValueAt(row, 0));
                Petugas petugas = controller.cariAkun(idPilih);
                if (petugas == null) return;
                id.setText(petugas.getIdPetugas());
                username.setText(petugas.getUsername());
                nama.setText(petugas.getNama());
                role.setSelectedItem(petugas.getRole());
                detail.setText(ambilDetail(petugas));
                password.setText("");
            }
        });
        loadData();
    }

    private String ambilDetail(Petugas petugas) {
        if (petugas instanceof PetugasAdmin) return ((PetugasAdmin) petugas).getSpesialisasiAdmin();
        if (petugas instanceof PetugasProduksi) return ((PetugasProduksi) petugas).getSpesialisasiProses();
        if (petugas instanceof PetugasGudang) return ((PetugasGudang) petugas).getSpesialisasiTugas();
        if (petugas instanceof PetugasDistribusi) return ((PetugasDistribusi) petugas).getWilayahTugas();
        return "";
    }

    private boolean konfirmasi(String isi) {
        return JOptionPane.showConfirmDialog(this, isi, "Konfirmasi",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;
    }

    private void jalankan(Runnable operasi) {
        try {
            operasi.run();
            loadData();
            pesan("Operasi akun berhasil.", JOptionPane.INFORMATION_MESSAGE);
        } catch (RuntimeException ex) {
            pesan(ex.getMessage(), JOptionPane.ERROR_MESSAGE);
        }
    }

    private void pesan(String isi, int tipe) {
        JOptionPane.showMessageDialog(this, isi, "Informasi", tipe);
    }

    private void kosongkanForm() {
        id.setText("");
        username.setText("");
        nama.setText("");
        password.setText("");
        detail.setText("");
        tabel.clearSelection();
    }

    private void loadData() {
        tabelModel.setRowCount(0);
        for (Petugas p : controller.loadAkun()) {
            tabelModel.addRow(new Object[]{p.getIdPetugas(), p.getUsername(),
                    p.getNama(), p.getRole(), ambilDetail(p)});
        }
    }
}
