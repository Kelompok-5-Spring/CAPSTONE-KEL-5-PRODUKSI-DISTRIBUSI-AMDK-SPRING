package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.Image;
import javax.swing.ImageIcon;
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

        JPanel sidebar = new JPanel(new org.netbeans.lib.awtextra.AbsoluteLayout());
        sidebar.setPreferredSize(new Dimension(180, 700));
        sidebar.setBackground(new Color(0, 105, 148));

        JLabel logo = new JLabel();
        java.net.URL logoUrl = getClass().getResource("/images/Spring Logo with Swirl Emblem (2).png");
        if (logoUrl != null) {
            ImageIcon icon = new ImageIcon(logoUrl);
            Image gambar = icon.getImage().getScaledInstance(128, 48, Image.SCALE_SMOOTH);
            logo.setIcon(new ImageIcon(gambar));
        }
        sidebar.add(logo, new org.netbeans.lib.awtextra.AbsoluteConstraints(24, 20, 132, 48));

        JLabel brand = new JLabel("Operasional");
        brand.setFont(new Font("Arial", Font.BOLD, 13));
        brand.setForeground(Color.WHITE);
        sidebar.add(brand, new org.netbeans.lib.awtextra.AbsoluteConstraints(35, 70, 120, 20));

        JLabel footer = new JLabel("Spring Operasional");
        footer.setFont(new Font("Arial", Font.BOLD, 11));
        footer.setForeground(Color.WHITE);
        sidebar.add(footer, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 660, 145, 20));

        add(sidebar, BorderLayout.WEST);
        ViewHelper.pasangSidebarStandar(sidebar, this, sistem);
    }
}
