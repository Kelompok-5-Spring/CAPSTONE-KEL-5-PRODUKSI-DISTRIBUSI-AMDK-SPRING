package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import controller.SistemController;
import model.type.Role;
import view.panel.AgenPanel;

/** Halaman aktif data agen untuk Admin, Gudang, dan Distribusi. */
public class DataAgenForm extends JFrame {
    public DataAgenForm(SistemController sistem) { this(sistem, false); }

    public DataAgenForm(SistemController sistem, boolean langsungTambah) {
        if (sistem == null || sistem.getAkunController() == null
                || sistem.getAkunController().getPetugasAktif() == null) {
            throw new IllegalStateException("Sesi pengguna belum tersedia. Silakan login kembali.");
        }
        Role role = sistem.getAkunController().getPetugasAktif().getRole();
        if (!ViewHelper.bolehAkses(role, "AGEN")) {
            throw new SecurityException("Role Anda tidak memiliki akses ke data agen.");
        }
        setTitle("SPRING | Data Agen");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1080, 700);
        setMinimumSize(new java.awt.Dimension(900, 600));
        setLayout(new BorderLayout(12, 12));

        JPanel header = new JPanel(new BorderLayout(8, 8));
        header.setBackground(new Color(0, 105, 148));
        header.setBorder(javax.swing.BorderFactory.createEmptyBorder(14, 18, 14, 18));
        JLabel judul = new JLabel("DATA AGEN");
        judul.setForeground(Color.WHITE);
        judul.setFont(new Font("Arial", Font.BOLD, 22));
        JLabel pengguna = new JLabel(sistem.getAkunController().getPetugasAktif().getNama()
                + "  |  " + role.getNilaiDatabase());
        pengguna.setForeground(Color.WHITE);
        pengguna.setHorizontalAlignment(SwingConstants.RIGHT);
        header.add(judul, BorderLayout.WEST);
        header.add(pengguna, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);
        AgenPanel panelAgen = new AgenPanel(sistem.getAgenController(), role == Role.Admin);
        add(panelAgen, BorderLayout.CENTER);
        if (langsungTambah && role == Role.Admin) {
            javax.swing.SwingUtilities.invokeLater(panelAgen::fokusTambahAgen);
        }

        JPanel bawah = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton kembali = new JButton("Kembali");
        JButton dashboard = new JButton("Dashboard");
        JButton produk = new JButton("Data Produk");
        JButton laporan = new JButton("Laporan");
        JButton keluar = new JButton("Logout");
        kembali.addActionListener(e -> ViewHelper.kembali(this, sistem));
        dashboard.addActionListener(e -> ViewHelper.bukaDashboard(this, sistem));
        produk.setVisible(ViewHelper.bolehAkses(role, "PRODUK"));
        produk.addActionListener(e -> ViewHelper.buka(this, sistem, "PRODUK"));
        laporan.addActionListener(e -> ViewHelper.buka(this, sistem, "LAPORAN"));
        keluar.addActionListener(e -> ViewHelper.logout(this, sistem));
        bawah.add(kembali); bawah.add(dashboard); bawah.add(produk); bawah.add(laporan); bawah.add(keluar);
        add(bawah, BorderLayout.SOUTH);
    }
}
