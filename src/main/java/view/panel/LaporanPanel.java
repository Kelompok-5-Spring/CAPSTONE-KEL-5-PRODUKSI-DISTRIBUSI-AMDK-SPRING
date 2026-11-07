package view.panel;

import java.awt.BorderLayout;
import java.time.LocalDate;
import javax.swing.*;
import controller.LaporanController;

/** Panel laporan yang memanggil method LaporanController yang tersedia di backend. */
public class LaporanPanel extends JPanel {
    private final LaporanController controller;
    private final JTextField tanggalAcuan = new JTextField(LocalDate.now().toString(), 10);
    private final JComboBox<String> periode = new JComboBox<>(new String[]{"Minggu", "Bulan", "Tahun"});
    private final JTextField awal = new JTextField(LocalDate.now().withDayOfMonth(1).toString(), 10);
    private final JTextField akhir = new JTextField(LocalDate.now().toString(), 10);
    private final JTextArea hasil = new JTextArea();

    public LaporanPanel(LaporanController controller) {
        super(new BorderLayout(8, 8));
        this.controller = controller;

        JPanel filter = new JPanel();
        filter.setBorder(BorderFactory.createTitledBorder("Filter Laporan"));
        JButton tampilPeriode = new JButton("Tampilkan Periode");
        JButton tampilRentang = new JButton("Filter Rentang Tanggal");
        JButton semua = new JButton("Semua Periode");
        filter.add(new JLabel("Tanggal acuan")); filter.add(tanggalAcuan);
        filter.add(periode); filter.add(tampilPeriode);
        filter.add(new JLabel("Dari")); filter.add(awal);
        filter.add(new JLabel("Sampai")); filter.add(akhir);
        filter.add(tampilRentang); filter.add(semua);
        add(filter, BorderLayout.NORTH);

        hasil.setEditable(false);
        hasil.setLineWrap(true);
        hasil.setWrapStyleWord(true);
        hasil.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        add(new JScrollPane(hasil), BorderLayout.CENTER);

        semua.addActionListener(e -> tampilkanSemua());
        tampilRentang.addActionListener(e -> tampilkanRentang());
        tampilPeriode.addActionListener(e -> tampilkanPeriode());
        tampilkanSemua();
    }

    private void tampilkanSemua() {
        try {
            hasil.setText(controller.buatLaporanProduksi()
                    + "\n\n" + controller.buatLaporanDistribusi());
        } catch (RuntimeException ex) {
            tampilkanError(ex);
        }
    }

    private void tampilkanRentang() {
        try {
            LocalDate dari = LocalDate.parse(awal.getText().trim());
            LocalDate sampai = LocalDate.parse(akhir.getText().trim());
            hasil.setText(controller.buatLaporanProduksi(dari, sampai)
                    + "\n\n" + controller.buatLaporanDistribusi(dari, sampai));
        } catch (RuntimeException ex) {
            tampilkanError(ex);
        }
    }

    private void tampilkanPeriode() {
        try {
            hasil.setText(controller.buatLaporanPeriode(
                    LocalDate.parse(tanggalAcuan.getText().trim()),
                    String.valueOf(periode.getSelectedItem())));
        } catch (RuntimeException ex) {
            tampilkanError(ex);
        }
    }

    private void tampilkanError(RuntimeException ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(),
                "Kesalahan laporan", JOptionPane.ERROR_MESSAGE);
    }
}
