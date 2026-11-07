package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Locale;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.BorderFactory;

/** Pemilih tanggal kalender sederhana untuk field tanggal pada form Swing. */
public final class KalenderTanggal {
    private KalenderTanggal() { }

    public static void pasang(JTextField field) {
        if (field == null) return;
        field.setToolTipText("Klik dua kali atau tekan F4 untuk memilih tanggal dari kalender.");
        field.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() >= 2 && SwingUtilities.isLeftMouseButton(e)) {
                    tampilkan(field);
                }
            }
        });
        field.getInputMap(JComponent.WHEN_FOCUSED)
                .put(KeyStroke.getKeyStroke("F4"), "pilihTanggal");
        field.getActionMap().put("pilihTanggal", new AbstractAction() {
            @Override public void actionPerformed(java.awt.event.ActionEvent e) {
                tampilkan(field);
            }
        });
    }

    private static void tampilkan(JTextField field) {
        LocalDate tanggalAwal;
        try {
            tanggalAwal = ViewHelper.tanggal(field.getText());
        } catch (RuntimeException ex) {
            tanggalAwal = LocalDate.now();
        }

        Window pemilik = SwingUtilities.getWindowAncestor(field);
        JDialog dialog = new JDialog(pemilik, "Pilih Tanggal", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setLayout(new BorderLayout(8, 8));
        dialog.getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        YearMonth[] bulan = {YearMonth.from(tanggalAwal)};
        JLabel judulBulan = new JLabel("", SwingConstants.CENTER);
        judulBulan.setFont(new Font("Arial", Font.BOLD, 14));
        JPanel kalender = new JPanel(new GridLayout(0, 7, 4, 4));

        JButton sebelumnya = new JButton("‹");
        JButton berikutnya = new JButton("›");
        JPanel navigasi = new JPanel(new BorderLayout(8, 4));
        navigasi.add(sebelumnya, BorderLayout.WEST);
        navigasi.add(judulBulan, BorderLayout.CENTER);
        navigasi.add(berikutnya, BorderLayout.EAST);
        dialog.add(navigasi, BorderLayout.NORTH);
        dialog.add(kalender, BorderLayout.CENTER);

        Runnable render = () -> {
            judulBulan.setText(bulan[0].getMonth().getDisplayName(TextStyle.FULL, Locale.forLanguageTag("id-ID"))
                    + " " + bulan[0].getYear());
            kalender.removeAll();
            for (DayOfWeek hari : new DayOfWeek[]{
                    DayOfWeek.SUNDAY, DayOfWeek.MONDAY, DayOfWeek.TUESDAY,
                    DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY,
                    DayOfWeek.SATURDAY}) {
                JLabel label = new JLabel(hari.getDisplayName(TextStyle.SHORT, Locale.forLanguageTag("id-ID")),
                        SwingConstants.CENTER);
                label.setFont(new Font("Arial", Font.BOLD, 11));
                kalender.add(label);
            }
            int offset = bulan[0].atDay(1).getDayOfWeek().getValue() % 7;
            for (int i = 0; i < offset; i++) kalender.add(new JLabel(""));
            for (int hari = 1; hari <= bulan[0].lengthOfMonth(); hari++) {
                final int nomorHari = hari;
                JButton tombolHari = new JButton(String.valueOf(hari));
                tombolHari.setFocusPainted(false);
                tombolHari.addActionListener(e -> {
                    field.setText(bulan[0].atDay(nomorHari).toString());
                    dialog.dispose();
                });
                kalender.add(tombolHari);
            }
            kalender.revalidate();
            kalender.repaint();
        };

        sebelumnya.addActionListener(e -> { bulan[0] = bulan[0].minusMonths(1); render.run(); });
        berikutnya.addActionListener(e -> { bulan[0] = bulan[0].plusMonths(1); render.run(); });
        render.run();
        dialog.setSize(320, 300);
        dialog.setLocationRelativeTo((Component) field);
        dialog.setVisible(true);
    }
}
