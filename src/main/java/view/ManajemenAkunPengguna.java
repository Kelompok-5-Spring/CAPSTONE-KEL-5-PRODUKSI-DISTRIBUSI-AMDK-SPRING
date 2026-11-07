/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

/**
 *
 * @author Dovs
 */
public class ManajemenAkunPengguna extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger =
            java.util.logging.Logger.getLogger(ManajemenAkunPengguna.class.getName());
    private controller.AkunController akunController;
    private controller.SistemController sistemController;

    public ManajemenAkunPengguna() {
        initComponents();
    }

    public ManajemenAkunPengguna(controller.SistemController sistemController) {
        this();
        setSistemController(sistemController);
    }

    public void setSistemController(controller.SistemController sistemController) {
        if (sistemController == null || sistemController.getAkunController() == null
                || sistemController.getAkunController().getPetugasAktif() == null
                || sistemController.getAkunController().getPetugasAktif().getRole() != model.type.Role.Admin) {
            throw new SecurityException("Halaman akun hanya dapat dibuka oleh Admin.");
        }
        this.sistemController = sistemController;
        this.akunController = sistemController.getAkunController();
        ViewHelper.setHeader(jLabel53, jLabel54, jLabel55, sistemController);

        jTextField1.setText("");
        jTextField16.setText("");
        jTextField17.setText("");
        jTextField18.setText("");
        jTextField19.setText("");
        jTextField5.setText("");
        jTextField18.setToolTipText("Password minimal 8 karakter dan mengandung huruf serta angka.");
        jTextField5.setToolTipText("Kosongkan jika password tidak ingin diubah.");
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(
                new String[]{"Admin", "Produksi", "Gudang", "Distribusi"}));
        jComboBox6.setModel(new javax.swing.DefaultComboBoxModel<>(
                new String[]{"Semua Hak Akses", "Admin", "Produksi", "Gudang", "Distribusi"}));

        ViewHelper.pasangTombol(jButton13, e -> ViewHelper.bukaDashboard(this, sistemController));
        ViewHelper.pasangTombol(jButton3, e -> ViewHelper.buka(this, sistemController, "PRODUK"));
        ViewHelper.pasangTombol(jButton4, e -> ViewHelper.buka(this, sistemController, "AGEN"));
        ViewHelper.pasangTombol(jButton5, e -> ViewHelper.buka(this, sistemController, "PRODUKSI"));
        ViewHelper.pasangTombol(jButton6, e -> ViewHelper.buka(this, sistemController, "STOK"));
        ViewHelper.pasangTombol(jButton7, e -> ViewHelper.buka(this, sistemController, "DISTRIBUSI"));
        ViewHelper.pasangTombol(jButton8, e -> ViewHelper.buka(this, sistemController, "LAPORAN"));
        ViewHelper.pasangTombol(jButton9, e -> muatAkun());
        ViewHelper.pasangTombol(jButton10, e -> ViewHelper.logout(this, sistemController));
        ViewHelper.pasangTombol(jButton12, e -> tambahAkun());
        ViewHelper.pasangTombol(jButton2, e -> ubahAkunTerpilih());
        ViewHelper.tambahTombolKembali(jPanel1, this, sistemController, 540);

        jTable1.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && jTable1.getSelectedRow() >= 0) isiFormAkun();
        });
        jTextField1.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { muatAkun(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { muatAkun(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { muatAkun(); }
        });
        jComboBox6.addActionListener(e -> muatAkun());

        javax.swing.JPopupMenu menu = new javax.swing.JPopupMenu();
        javax.swing.JMenuItem hapus = new javax.swing.JMenuItem("Hapus Akun Terpilih");
        hapus.addActionListener(e -> hapusAkunTerpilih());
        menu.add(hapus);
        jTable1.setComponentPopupMenu(menu);
        ViewHelper.pilihBarisSaatKlikKanan(jTable1);
        muatAkun();
    }

    private void muatAkun() {
        if (akunController == null) return;
        try {
            javax.swing.table.DefaultTableModel m = new javax.swing.table.DefaultTableModel(
                    new Object[]{"ID Petugas", "Nama", "Username", "Hak Akses", "Spesialisasi / Wilayah"}, 0) {
                @Override public boolean isCellEditable(int r, int c) { return false; }
            };
            String cari = jTextField1.getText().trim().toLowerCase(java.util.Locale.ROOT);
            String filter = String.valueOf(jComboBox6.getSelectedItem());
            for (model.Petugas p : akunController.loadAkun()) {
                boolean cocokCari = cari.isEmpty() || cari.startsWith("cari nama")
                        || p.getNama().toLowerCase(java.util.Locale.ROOT).contains(cari)
                        || p.getUsername().toLowerCase(java.util.Locale.ROOT).contains(cari)
                        || p.getIdPetugas().toLowerCase(java.util.Locale.ROOT).contains(cari);
                boolean cocokRole = "Semua Hak Akses".equals(filter)
                        || filter.equalsIgnoreCase(p.getRole().getNilaiDatabase());
                if (cocokCari && cocokRole) {
                    m.addRow(new Object[]{p.getIdPetugas(), p.getNama(), p.getUsername(),
                        p.getRole().getNilaiDatabase(), ViewHelper.spesialisasi(p)});
                }
            }
            jTable1.setModel(m);
        } catch (RuntimeException ex) { ViewHelper.error(this, ex); }
    }

    private model.Petugas petugasTerpilih() {
        int row = jTable1.getSelectedRow();
        if (row < 0) return null;
        return akunController.cariAkun(String.valueOf(jTable1.getValueAt(row, 0)));
    }

    private void isiFormAkun() {
        model.Petugas p = petugasTerpilih();
        if (p == null) return;
        jTextField19.setText(p.getUsername());
        jTextField5.setText("");
        jLabel10.setText("Perbarui akun: " + p.getNama() + " (" + p.getRole().getNilaiDatabase() + ")");
    }

    private void tambahAkun() {
        model.type.Role role;
        try {
            role = model.type.Role.valueOf(String.valueOf(jComboBox1.getSelectedItem()));
        } catch (RuntimeException ex) {
            ViewHelper.info(this, "Pilih hak akses yang valid.");
            return;
        }
        String spesialisasi = ViewHelper.mintaSpesialisasi(this, role);
        if (spesialisasi == null) return;
        if (!ViewHelper.konfirmasi(this, "Daftarkan akun pegawai baru?")) return;
        try {
            model.Petugas baru = akunController.simpanAkun(
                    jTextField17.getText(), jTextField16.getText(), jTextField18.getText(),
                    role, spesialisasi);
            jTextField16.setText("");
            jTextField17.setText("");
            jTextField18.setText("");
            muatAkun();
            ViewHelper.info(this, "Akun berhasil dibuat. ID Petugas: " + baru.getIdPetugas());
        } catch (RuntimeException ex) { ViewHelper.error(this, ex); }
    }

    private void ubahAkunTerpilih() {
        model.Petugas lama = petugasTerpilih();
        if (lama == null) {
            ViewHelper.info(this, "Pilih akun dari tabel terlebih dahulu.");
            return;
        }
        if (!ViewHelper.konfirmasi(this, "Simpan perubahan username atau password akun ini?")) return;
        try {
            akunController.ubahAkun(lama.getIdPetugas(), jTextField19.getText(),
                    lama.getNama(), jTextField5.getText(), lama.getRole(),
                    ViewHelper.spesialisasi(lama));
            jTextField5.setText("");
            muatAkun();
            ViewHelper.info(this, "Akun berhasil diperbarui.");
        } catch (RuntimeException ex) { ViewHelper.error(this, ex); }
    }

    private void hapusAkunTerpilih() {
        model.Petugas p = petugasTerpilih();
        if (p == null) {
            ViewHelper.info(this, "Pilih akun yang akan dihapus.");
            return;
        }
        if (!ViewHelper.konfirmasi(this, "Hapus akun " + p.getUsername() + "?")) return;
        try {
            akunController.hapusAkun(p.getIdPetugas());
            jTextField19.setText("");
            jTextField5.setText("");
            muatAkun();
            ViewHelper.info(this, "Akun berhasil dihapus.");
        } catch (RuntimeException ex) { ViewHelper.error(this, ex); }
    }

    public void refreshData() { muatAkun(); }

    private void prosesLogout() { ViewHelper.logout(this, sistemController); }

@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jButton13 = new javax.swing.JButton();
        jLabel44 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel45 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jLabel46 = new javax.swing.JLabel();
        jButton9 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jLabel47 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jTextField1 = new javax.swing.JTextField();
        jComboBox6 = new javax.swing.JComboBox<>();
        jPanel5 = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jLabel42 = new javax.swing.JLabel();
        jTextField19 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        jButton10 = new javax.swing.JButton();
        jSeparator10 = new javax.swing.JSeparator();
        jLabel27 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel38 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jTextField16 = new javax.swing.JTextField();
        jLabel39 = new javax.swing.JLabel();
        jTextField17 = new javax.swing.JTextField();
        jTextField18 = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jButton12 = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(0, 105, 148));
        jPanel1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Spring Logo with Swirl Emblem (2).png"))); // NOI18N
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 30, -1, -1));

        jLabel43.setBackground(new java.awt.Color(255, 255, 255));
        jLabel43.setFont(new java.awt.Font("Arial Black", 1, 13)); // NOI18N
        jLabel43.setForeground(new java.awt.Color(255, 255, 255));
        jLabel43.setText("Operasional");
        jPanel1.add(jLabel43, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 70, 90, 20));

        jButton13.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jButton13.setForeground(new java.awt.Color(0, 105, 148));
        jButton13.setText("Dashboard");
        jPanel1.add(jButton13, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, 122, 33));

        jLabel44.setFont(new java.awt.Font("Arial Black", 1, 11)); // NOI18N
        jLabel44.setForeground(new java.awt.Color(255, 255, 255));
        jLabel44.setText("Data");
        jPanel1.add(jLabel44, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 190, 30, 20));

        jButton3.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jButton3.setForeground(new java.awt.Color(0, 105, 148));
        jButton3.setText("Data Produk");
        jPanel1.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 210, 122, 30));

        jButton4.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jButton4.setForeground(new java.awt.Color(0, 105, 148));
        jButton4.setText("Data Agen");
        jPanel1.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 250, 122, 30));

        jLabel45.setFont(new java.awt.Font("Arial Black", 1, 11)); // NOI18N
        jLabel45.setForeground(new java.awt.Color(255, 255, 255));
        jLabel45.setText("Operasional");
        jPanel1.add(jLabel45, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 310, 90, 20));

        jButton5.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jButton5.setForeground(new java.awt.Color(0, 105, 148));
        jButton5.setText("Kelola Produksi");
        jPanel1.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 330, 122, 30));

        jButton6.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jButton6.setForeground(new java.awt.Color(0, 105, 148));
        jButton6.setText("Gudang");
        jPanel1.add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 370, 122, 30));

        jButton7.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jButton7.setForeground(new java.awt.Color(0, 105, 148));
        jButton7.setText("Distribusi");
        jPanel1.add(jButton7, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 410, 122, 30));

        jLabel46.setFont(new java.awt.Font("Arial Black", 1, 11)); // NOI18N
        jLabel46.setForeground(new java.awt.Color(255, 255, 255));
        jLabel46.setText("Administrasi");
        jPanel1.add(jLabel46, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 480, 90, 20));

        jButton9.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jButton9.setForeground(new java.awt.Color(0, 105, 148));
        jButton9.setText("Akun Pengguna");
        jButton9.addActionListener(this::jButton9ActionPerformed);
        jPanel1.add(jButton9, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 500, 122, 30));

        jButton8.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jButton8.setForeground(new java.awt.Color(0, 105, 148));
        jButton8.setText("Laporan");
        jButton8.addActionListener(this::jButton8ActionPerformed);
        jPanel1.add(jButton8, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 580, 122, 37));

        jLabel47.setBackground(new java.awt.Color(255, 255, 255));
        jLabel47.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        jLabel47.setForeground(new java.awt.Color(255, 255, 255));
        jLabel47.setText("Spring Operasional");
        jPanel1.add(jLabel47, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 660, -1, 20));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 180, 720));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 150, 590, -1));

        jPanel13.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel28.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        jLabel28.setText("Akun Pegawai");
        jPanel13.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(21, 12, 198, -1));

        jTable1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Username", "Password", "Hak Akses", "Kelola"
            }
        ));
        jTable1.setAutoscrolls(false);
        jTable1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jScrollPane1.setViewportView(jTable1);

        jPanel13.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 630, 230));

        jTextField1.setText("Cari nama atau username...");
        jTextField1.addActionListener(this::jTextField1ActionPerformed);
        jPanel13.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 370, -1));

        jComboBox6.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Semua Hak Akses", "Admin", "Produksi", "Gudang", "Distribusi" }));
        jComboBox6.addActionListener(this::jComboBox6ActionPerformed);
        jPanel13.add(jComboBox6, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 40, 200, -1));

        jPanel2.add(jPanel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 150, 670, 310));

        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel31.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        jLabel31.setText("Perbarui password");
        jPanel5.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 12, 189, -1));

        jLabel8.setFont(new java.awt.Font("Arial Black", 0, 14)); // NOI18N
        jLabel8.setText("Password Baru:");
        jPanel5.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, -1, -1));

        jTextField5.setText("Username");
        jTextField5.addActionListener(this::jTextField5ActionPerformed);
        jPanel5.add(jTextField5, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 100, 391, 35));

        jLabel10.setText("Minimal 8 karakter, gunakan kombinasi huruf dan angka.");
        jPanel5.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 140, -1, -1));

        jButton2.setBackground(new java.awt.Color(0, 105, 148));
        jButton2.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Perbarui password");
        jButton2.addActionListener(this::jButton2ActionPerformed);
        jPanel5.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 10, 200, -1));

        jLabel42.setFont(new java.awt.Font("Arial Black", 0, 14)); // NOI18N
        jLabel42.setText("Username:");
        jPanel5.add(jLabel42, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, -1, -1));

        jTextField19.setText("Username");
        jTextField19.addActionListener(this::jTextField19ActionPerformed);
        jPanel5.add(jTextField19, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 60, 391, 35));

        jPanel2.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 470, 670, 170));

        jLabel3.setBackground(new java.awt.Color(255, 255, 255));
        jLabel3.setFont(new java.awt.Font("Bahnschrift", 1, 36)); // NOI18N
        jLabel3.setText("Akun Pengguna");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 409, 42));

        jLabel53.setText("4 November 2026");
        jPanel2.add(jLabel53, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 30, 110, -1));

        jLabel55.setText("Admin");
        jPanel2.add(jLabel55, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 20, 79, -1));

        jLabel54.setText("Aulia Ashylla");
        jPanel2.add(jLabel54, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 40, 79, 10));

        jButton10.setBackground(new java.awt.Color(255, 0, 51));
        jButton10.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jButton10.setForeground(new java.awt.Color(252, 252, 252));
        jButton10.setText("LogOut");
        jButton10.addActionListener(this::jButton10ActionPerformed);
        jPanel2.add(jButton10, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 30, -1, -1));

        jSeparator10.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel2.add(jSeparator10, new org.netbeans.lib.awtextra.AbsoluteConstraints(873, 20, -1, 33));

        jLabel27.setBackground(new java.awt.Color(255, 255, 255));
        jLabel27.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        jLabel27.setText("Daftarkan pegawai, atur hak akses, dan kelola keamanan akun perusahaan.");
        jPanel2.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 120, 580, -1));

        jPanel9.setBackground(new java.awt.Color(0, 105, 148));

        jLabel38.setBackground(new java.awt.Color(0, 105, 148));
        jLabel38.setFont(new java.awt.Font("Arial Black", 1, 18)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(255, 255, 255));
        jLabel38.setText("Daftarkan akun pegawai");

        jLabel26.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(255, 255, 255));
        jLabel26.setText("Nama Pegawai");

        jTextField16.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jTextField16.setText("Nama Pegawai");
        jTextField16.addActionListener(this::jTextField2ActionPerformed);

        jLabel39.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel39.setForeground(new java.awt.Color(255, 255, 255));
        jLabel39.setText("Username");

        jTextField17.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jTextField17.setText("Username");
        jTextField17.addActionListener(this::jTextField17jTextField2ActionPerformed);

        jTextField18.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jTextField18.setText("Password");
        jTextField18.addActionListener(this::jTextField18jTextField2ActionPerformed);

        jLabel40.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel40.setForeground(new java.awt.Color(255, 255, 255));
        jLabel40.setText("Password");

        jLabel41.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel41.setForeground(new java.awt.Color(255, 255, 255));
        jLabel41.setText("Hak Akses");

        jComboBox1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Admin", "Produksi", "Gudang", "Distribusi" }));
        jComboBox1.addActionListener(this::jComboBox1ActionPerformed);

        jButton12.setForeground(new java.awt.Color(0, 105, 148));
        jButton12.setText("Simpan");

        jSeparator3.setBackground(new java.awt.Color(255, 255, 255));
        jSeparator3.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(110, 110, 110))
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextField17, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                    .addComponent(jTextField16)
                    .addComponent(jTextField18, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                    .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel41)
                    .addComponent(jLabel40)
                    .addComponent(jLabel39)
                    .addComponent(jLabel26)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jLabel38)))
                .addContainerGap(27, Short.MAX_VALUE))
            .addComponent(jSeparator3, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel38)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel26)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField16, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel39)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField17, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField18, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(13, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 150, 350, 370));
        jPanel2.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 600, -1, -1));
        jPanel2.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 610, -1, -1));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 0, 1100, 720));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTextField5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField5ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        prosesLogout();
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jTextField17jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField17jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField17jTextField2ActionPerformed

    private void jTextField18jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField18jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField18jTextField2ActionPerformed

    private void jComboBox6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox6ActionPerformed

    private void jTextField19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField19ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField19ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton8ActionPerformed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox6;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField16;
    private javax.swing.JTextField jTextField17;
    private javax.swing.JTextField jTextField18;
    private javax.swing.JTextField jTextField19;
    private javax.swing.JTextField jTextField5;
    // End of variables declaration//GEN-END:variables
}
