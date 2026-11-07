/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

/**
 *
 * @author Dovs
 */
public class ManajemenDistribusiForm extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger =
            java.util.logging.Logger.getLogger(ManajemenDistribusiForm.class.getName());
    private controller.AkunController akunController;
    private controller.SistemController sistemController;
    private String idDistribusiTerpilih;

    public ManajemenDistribusiForm() {
        initComponents();
    }

    public ManajemenDistribusiForm(controller.SistemController sistemController) {
        this();
        setSistemController(sistemController);
    }

    public void setSistemController(controller.SistemController sistemController) {
        if (sistemController == null || sistemController.getAkunController() == null
                || sistemController.getAkunController().getPetugasAktif() == null) {
            throw new IllegalStateException("Sesi pengguna belum tersedia.");
        }
        this.sistemController = sistemController;
        this.akunController = sistemController.getAkunController();
        model.type.Role role = akunController.getPetugasAktif().getRole();

        ViewHelper.setHeader(jLabel53, jLabel54, jLabel55, sistemController);
        ViewHelper.pasangSidebarStandar(jPanel1, this, sistemController);
        jLabel36.setText("Kelola pengiriman sesuai wilayah dan hak akses akun.");
        jTextField1.setText(java.time.LocalDate.now().toString());
        jTextField2.setText("");
        jTextField3.setText("");
        jTextField5.setText("");
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{
            "Dalam Pengiriman", "Terkirim", "Tidak Terkirim"}));
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{
            "Spring Botol 600ml", "Spring Botol 1500ml", "Spring Galon 5L", "Spring Galon 19L"}));
        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{
            "Semua Produk", "Spring Botol 600ml", "Spring Botol 1500ml",
            "Spring Galon 5L", "Spring Galon 19L"}));
        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{
            "Semua", "Dalam Pengiriman", "Terkirim", "Tidak Terkirim"}));
        muatPilihanAgen();

        ViewHelper.pasangTombol(jButton1, e -> ViewHelper.bukaDashboard(this, sistemController));
        ViewHelper.pasangTombol(jButton4, e -> ViewHelper.buka(this, sistemController, "AGEN"));
        ViewHelper.pasangTombol(jButton10, e -> ViewHelper.buka(this, sistemController, "STOK"));
        ViewHelper.pasangTombol(jButton8, e -> muatDistribusi());
        ViewHelper.pasangTombol(jButton11, e -> ViewHelper.buka(this, sistemController, "LAPORAN"));
        ViewHelper.pasangTombol(jButton5, e -> resetFilterDistribusi());
        ViewHelper.pasangTombol(jButton6, e -> kosongkanFormDistribusi());
        ViewHelper.pasangTombol(jButton9, e -> simpanAtauPerbaruiDistribusi());
        ViewHelper.pasangTombol(jButton2, e -> ViewHelper.logout(this, sistemController));
        boolean admin = role == model.type.Role.Admin;
        boolean distribusi = role == model.type.Role.Distribusi;
        jButton10.setVisible(false);
        jButton9.setEnabled(admin);
        jComboBox6.setEnabled(admin);
        jComboBox2.setEnabled(admin);
        jTextField1.setEditable(admin);
        jTextField2.setEditable(admin);
        jComboBox1.setEnabled(false);
        jButton6.setEnabled(admin);

        jTable1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTable1.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && jTable1.getSelectedRow() >= 0) isiFormDistribusi();
        });
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2 && jTable1.getSelectedRow() >= 0) {
                    isiFormDistribusi();
                }
            }
        });
        jTextField3.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { muatDistribusi(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { muatDistribusi(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { muatDistribusi(); }
        });
        jTextField5.addActionListener(e -> muatDistribusi());
        jComboBox3.addActionListener(e -> muatDistribusi());
        jComboBox4.addActionListener(e -> muatDistribusi());
        jComboBox5.addActionListener(e -> muatDistribusi());

        javax.swing.JPopupMenu menu = new javax.swing.JPopupMenu();
        javax.swing.JMenuItem status = new javax.swing.JMenuItem("Ubah Status Pengiriman");
        status.addActionListener(e -> ubahStatusDistribusiTerpilih());
        status.setEnabled(admin || distribusi);
        javax.swing.JMenuItem tanggal = new javax.swing.JMenuItem("Ubah Tanggal Pengiriman");
        tanggal.addActionListener(e -> ubahTanggalDistribusiTerpilih());
        tanggal.setEnabled(admin);
        javax.swing.JMenuItem hapus = new javax.swing.JMenuItem("Hapus Transaksi");
        hapus.addActionListener(e -> hapusDistribusiTerpilih());
        hapus.setEnabled(admin);
        menu.add(status);
        menu.add(tanggal);
        menu.add(hapus);
        jTable1.setComponentPopupMenu(menu);
        ViewHelper.pilihBarisSaatKlikKanan(jTable1);
        muatDistribusi();
    }

    private void muatPilihanAgen() {
        try {
            javax.swing.DefaultComboBoxModel<String> modelAgen = new javax.swing.DefaultComboBoxModel<>();
            javax.swing.DefaultComboBoxModel<String> modelFilter = new javax.swing.DefaultComboBoxModel<>();
            modelFilter.addElement("Semua Agen");
            for (model.Agen agen : sistemController.getAgenController().loadAgen()) {
                modelAgen.addElement(agen.getNamaAgen());
                modelFilter.addElement(agen.getNamaAgen());
            }
            jComboBox6.setModel(modelAgen);
            jComboBox4.setModel(modelFilter);
        } catch (RuntimeException ex) { ViewHelper.error(this, ex); }
    }

    private model.type.JenisProduk jenisProdukDipilih() {
        String nama = String.valueOf(jComboBox2.getSelectedItem());
        for (model.type.JenisProduk jenis : model.type.JenisProduk.values()) {
            if (jenis.getNamaProduk().equalsIgnoreCase(nama.trim())) return jenis;
        }
        throw new IllegalArgumentException("Pilih jenis produk yang valid.");
    }

    private model.Agen agenDipilih() {
        String nama = String.valueOf(jComboBox6.getSelectedItem());
        for (model.Agen agen : sistemController.getAgenController().loadAgen()) {
            if (agen.getNamaAgen().equals(nama)) return agen;
        }
        throw new IllegalArgumentException("Pilih agen yang terdaftar dan berada dalam wilayah tugas.");
    }

    private void simpanAtauPerbaruiDistribusi() {
        if (idDistribusiTerpilih != null) {
            ubahStatusDistribusiTerpilih();
            return;
        }
        if (akunController.getPetugasAktif().getRole() != model.type.Role.Admin) {
            ViewHelper.info(this, "Petugas Distribusi hanya dapat memperbarui status pengiriman yang ditugaskan.");
            return;
        }
        if (!ViewHelper.konfirmasi(this, "Simpan transaksi distribusi baru?")) return;
        try {
            java.time.LocalDate tanggal = ViewHelper.tanggal(jTextField1.getText());
            model.Agen agen = agenDipilih();
            model.type.JenisProduk jenis = jenisProdukDipilih();
            int jumlah;
            try { jumlah = Integer.parseInt(jTextField2.getText().trim()); }
            catch (NumberFormatException ex) { throw new IllegalArgumentException("Jumlah pesan harus berupa angka bulat."); }
            model.DetailDistribusi detail = new model.DetailDistribusi(
                    agen.getIdAgen(), jenis, jumlah, java.time.LocalDate.now());
            model.Distribusi baru = sistemController.getDistribusiController()
                    .simpanDistribusi(tanggal, java.util.Collections.singletonList(detail));
            kosongkanFormDistribusi();
            muatDistribusi();
            ViewHelper.info(this, "Distribusi berhasil dicatat. ID: " + baru.getIdDistribusi());
        } catch (RuntimeException ex) { ViewHelper.error(this, ex); }
    }

    private void muatDistribusi() {
        if (sistemController == null) return;
        try {
            idDistribusiTerpilih = null;
            jComboBox1.setEnabled(false);
            jButton9.setEnabled(akunController.getPetugasAktif().getRole() == model.type.Role.Admin);
            jButton9.setText("Simpan Distribusi");
            javax.swing.table.DefaultTableModel m = new javax.swing.table.DefaultTableModel(
                    new Object[]{"ID Distribusi", "Produk", "Agen", "Jumlah", "Tanggal", "Status"}, 0) {
                @Override public boolean isCellEditable(int r, int c) { return false; }
            };
            String cari = jTextField3.getText().trim().toLowerCase(java.util.Locale.ROOT);
            String tanggalFilter = jTextField5.getText().trim();
            String produkFilter = String.valueOf(jComboBox3.getSelectedItem());
            String agenFilter = String.valueOf(jComboBox4.getSelectedItem());
            String statusFilter = String.valueOf(jComboBox5.getSelectedItem());
            java.time.LocalDate tanggalCari = null;
            if (!tanggalFilter.isEmpty() && !tanggalFilter.equalsIgnoreCase("yyyy-mm-dd")) {
                tanggalCari = ViewHelper.tanggal(tanggalFilter);
            }

            int totalDetail = 0;
            for (model.Distribusi distribusi : sistemController.getDistribusiController().loadDistribusi()) {
                if (!cari.isEmpty() && !distribusi.getIdDistribusi().toLowerCase(java.util.Locale.ROOT).contains(cari)) continue;
                if (tanggalCari != null && !distribusi.getTanggalKirim().equals(tanggalCari)) continue;
                if (!"Semua".equals(statusFilter)
                        && !distribusi.getStatusKirim().getNilaiDatabase().equalsIgnoreCase(statusFilter)) continue;
                for (model.DetailDistribusi detail : sistemController.getDistribusiController()
                        .loadDetailDistribusi(distribusi.getIdDistribusi())) {
                    model.Agen agen = sistemController.getAgenController().cariAgen(detail.getIdAgen());
                    String namaAgen = agen == null ? detail.getIdAgen() : agen.getNamaAgen();
                    String namaProduk = detail.getJenisProduk().getNamaProduk();
                    if (!"Semua Produk".equals(produkFilter) && !namaProduk.equalsIgnoreCase(produkFilter)) continue;
                    if (!"Semua Agen".equals(agenFilter) && !namaAgen.equalsIgnoreCase(agenFilter)) continue;
                    m.addRow(new Object[]{distribusi.getIdDistribusi(), namaProduk, namaAgen,
                        detail.getJumlahPesan(), distribusi.getTanggalKirim(),
                        distribusi.getStatusKirim().getNilaiDatabase()});
                    totalDetail++;
                }
            }
            jTable1.setModel(m);
            jLabel36.setText(totalDetail == 0
                    ? "Tidak ada baris data yang sesuai dengan filter. Silakan ubah pencarian atau pilihan filter."
                    : totalDetail + " rincian pengiriman ditampilkan sesuai filter.");
        } catch (RuntimeException ex) { ViewHelper.error(this, ex); }
    }

    private void isiFormDistribusi() {
        int row = jTable1.getSelectedRow();
        if (row < 0) return;
        idDistribusiTerpilih = String.valueOf(jTable1.getValueAt(row, 0));
        try {
            model.Distribusi distribusi = sistemController.getDistribusiController().cariDistribusi(idDistribusiTerpilih);
            if (distribusi == null) return;
            jTextField1.setText(distribusi.getTanggalKirim().toString());
            jComboBox1.setSelectedItem(distribusi.getStatusKirim().getNilaiDatabase());
            java.util.List<model.DetailDistribusi> detail =
                    sistemController.getDistribusiController().loadDetailDistribusi(idDistribusiTerpilih);
            String produkTerpilih = String.valueOf(jTable1.getValueAt(row, 1));
            String agenTerpilih = String.valueOf(jTable1.getValueAt(row, 2));
            for (model.DetailDistribusi rincian : detail) {
                model.Agen agen = sistemController.getAgenController().cariAgen(rincian.getIdAgen());
                String namaAgen = agen == null ? rincian.getIdAgen() : agen.getNamaAgen();
                if (rincian.getJenisProduk().getNamaProduk().equalsIgnoreCase(produkTerpilih)
                        && namaAgen.equalsIgnoreCase(agenTerpilih)) {
                    jComboBox6.setSelectedItem(namaAgen);
                    jComboBox2.setSelectedItem(rincian.getJenisProduk().getNamaProduk());
                    jTextField2.setText(String.valueOf(rincian.getJumlahPesan()));
                    break;
                }
            }
            boolean bolehUbahStatus = akunController.getPetugasAktif().getRole() == model.type.Role.Admin
                    || akunController.getPetugasAktif().getRole() == model.type.Role.Distribusi;
            jComboBox1.setEnabled(bolehUbahStatus);
            jButton9.setEnabled(bolehUbahStatus);
            jButton9.setText("Simpan Status");
        } catch (RuntimeException ex) { ViewHelper.error(this, ex); }
    }

    private void ubahStatusDistribusiTerpilih() {
        if (idDistribusiTerpilih == null) {
            ViewHelper.info(this, "Pilih transaksi distribusi terlebih dahulu.");
            return;
        }
        try {
            model.type.StatusKirim status = model.type.StatusKirim.fromDatabase(
                    String.valueOf(jComboBox1.getSelectedItem()).equals("Dalam Pengiriman")
                            ? "Dalam Pengiriman" : String.valueOf(jComboBox1.getSelectedItem()));
            boolean kembali = true;
            if (status == model.type.StatusKirim.TIDAK_TERKIRIM) {
                kembali = ViewHelper.konfirmasi(this, "Apakah seluruh barang sudah kembali ke gudang? Produk hanya akan masuk kembali ke stok setelah dikonfirmasi.");
                if (!kembali) return;
            }
            if (!ViewHelper.konfirmasi(this, "Ubah status pengiriman menjadi " + status + "?")) return;
            sistemController.getDistribusiController().ubahStatusDistribusi(idDistribusiTerpilih, status, kembali);
            kosongkanFormDistribusi();
            muatDistribusi();
            ViewHelper.info(this, "Status pengiriman berhasil diperbarui.");
        } catch (RuntimeException ex) { ViewHelper.error(this, ex); }
    }

    private void ubahTanggalDistribusiTerpilih() {
        if (akunController.getPetugasAktif().getRole() != model.type.Role.Admin) return;
        if (idDistribusiTerpilih == null) {
            ViewHelper.info(this, "Pilih transaksi distribusi terlebih dahulu.");
            return;
        }
        try {
            String nilai = javax.swing.JOptionPane.showInputDialog(this,
                    "Masukkan tanggal baru (yyyy-MM-dd):", jTextField1.getText());
            if (nilai == null) return;
            sistemController.getDistribusiController().ubahTanggalKirim(
                    idDistribusiTerpilih, ViewHelper.tanggal(nilai));
            muatDistribusi();
            ViewHelper.info(this, "Tanggal pengiriman berhasil diperbarui.");
        } catch (RuntimeException ex) { ViewHelper.error(this, ex); }
    }

    private void hapusDistribusiTerpilih() {
        if (akunController.getPetugasAktif().getRole() != model.type.Role.Admin) return;
        if (idDistribusiTerpilih == null) {
            ViewHelper.info(this, "Pilih transaksi distribusi terlebih dahulu.");
            return;
        }
        if (!ViewHelper.konfirmasi(this, "Hapus transaksi " + idDistribusiTerpilih + "?")) return;
        try {
            sistemController.getDistribusiController().hapusDistribusi(idDistribusiTerpilih);
            kosongkanFormDistribusi();
            muatDistribusi();
            ViewHelper.info(this, "Transaksi distribusi berhasil dihapus.");
        } catch (RuntimeException ex) { ViewHelper.error(this, ex); }
    }

    private void resetFilterDistribusi() {
        jTextField3.setText("");
        jTextField5.setText("");
        jComboBox3.setSelectedIndex(0);
        jComboBox4.setSelectedIndex(0);
        jComboBox5.setSelectedIndex(0);
        muatDistribusi();
    }

    private void kosongkanFormDistribusi() {
        idDistribusiTerpilih = null;
        jTextField1.setText(java.time.LocalDate.now().toString());
        jTextField2.setText("");
        jComboBox1.setSelectedItem("Dalam Pengiriman");
        jButton9.setText("Simpan Distribusi");
        jComboBox1.setEnabled(false);
        jButton9.setEnabled(akunController.getPetugasAktif().getRole() == model.type.Role.Admin);
        jTable1.clearSelection();
    }

    public void refreshData() { muatPilihanAgen(); muatDistribusi(); }

    private void prosesLogout() { ViewHelper.logout(this, sistemController); }

@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jLabel23 = new javax.swing.JLabel();
        jButton10 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton5 = new javax.swing.JButton();
        jTextField3 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jComboBox3 = new javax.swing.JComboBox<>();
        jComboBox4 = new javax.swing.JComboBox<>();
        jComboBox5 = new javax.swing.JComboBox<>();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jButton6 = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        jButton9 = new javax.swing.JButton();
        jComboBox6 = new javax.swing.JComboBox<>();
        jLabel24 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(0, 105, 148));
        jPanel1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Spring Logo with Swirl Emblem (2).png"))); // NOI18N
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 30, -1, -1));

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setFont(new java.awt.Font("Arial Black", 1, 13)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Operasional");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 70, 90, 20));

        jButton1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(0, 105, 148));
        jButton1.setText("Dashboard");
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, 122, 33));

        jLabel18.setFont(new java.awt.Font("Arial Black", 1, 11)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("Data");
        jPanel1.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 190, 30, 20));

        jButton4.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jButton4.setForeground(new java.awt.Color(0, 105, 148));
        jButton4.setText("Data Agen");
        jPanel1.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 210, 122, 30));

        jLabel23.setFont(new java.awt.Font("Arial Black", 1, 11)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setText("Operasional");
        jPanel1.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 310, 90, 20));

        jButton10.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jButton10.setForeground(new java.awt.Color(0, 105, 148));
        jButton10.setText("Gudang");
        jPanel1.add(jButton10, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 330, 122, 30));

        jButton8.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jButton8.setForeground(new java.awt.Color(0, 105, 148));
        jButton8.setText("Distribusi");
        jButton8.addActionListener(this::jButton8ActionPerformed);
        jPanel1.add(jButton8, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 370, 122, 30));

        jButton11.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jButton11.setForeground(new java.awt.Color(0, 105, 148));
        jButton11.setText("Laporan");
        jButton11.addActionListener(this::jButton11ActionPerformed);
        jPanel1.add(jButton11, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 580, 122, 37));

        jLabel10.setBackground(new java.awt.Color(255, 255, 255));
        jLabel10.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Spring Operasional");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 660, -1, 20));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 180, 720));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel31.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel31.setText("Data Distribusi");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "No. Pengiriman", "Produk", "Agen", "Jumlah", "Waktu", "Status Transaksi"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Float.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jButton5.setBackground(new java.awt.Color(0, 105, 148));
        jButton5.setForeground(new java.awt.Color(255, 255, 255));
        jButton5.setText("Semua Riwayat ↗");
        jButton5.addActionListener(this::jButton5ActionPerformed);

        jTextField3.setText("Cari No. Pengiriman...");
        jTextField3.addActionListener(this::jTextField3ActionPerformed);

        jTextField5.setText("yyyy-mm-dd");
        jTextField5.addActionListener(this::jTextField5ActionPerformed);

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Semua Produk", "pring Botol 600ml", "Spring Botol 1500ml", "Spring Galon 19L", "Spring Galon 5L" }));

        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Semua Agen", "Toko Segar Jaya", "Depot Air Minum Barokah", "Toko Sembako Berkah ", "Minimarket Sentrosa", "Toko Tirta Abadi ", "Depot Sumber Rejeki", "Toko Maju Bersama ", "Minimarket Melati ", "Toko Kelontong Utama", "Depot Minum Sehat", "Toko Kasih Ibu", "Minimarket Berkat Jaya", "Depot Tirta Bening ", "Toko Makmur Sejahtera", "Toko Baru Palaran" }));

        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Semua", "Terkirim", "Tidak Terkirim", "Dalam Pengiriman" }));
        jComboBox5.addActionListener(this::jComboBox5ActionPerformed);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel31)
                .addGap(9, 9, 9)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 360, -1, 290));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 361, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 122, Short.MAX_VALUE)
        );

        jPanel2.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(1637, 286, -1, -1));

        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setBackground(new java.awt.Color(255, 255, 255));
        jLabel4.setFont(new java.awt.Font("Arial Black", 0, 16)); // NOI18N
        jLabel4.setText("Update Distribusi");
        jPanel6.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 200, 20));

        jTextField1.setText("yyyy-mm-dd");
        jTextField1.addActionListener(this::jTextField1ActionPerformed);
        jPanel6.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 150, 30));

        jLabel6.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel6.setText("Status Pengiriman");
        jPanel6.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 40, -1, -1));

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Terkirim", "Dalam Pengiriman", " " }));
        jPanel6.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 60, 170, 30));

        jLabel7.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel7.setText("Jenis Produk *");
        jPanel6.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, -1, -1));

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Spring Botol 600ml", "Spring Botol 1500ml", "Spring Galon 19L", "Spring Galon 5L" }));
        jComboBox2.addActionListener(this::jComboBox2ActionPerformed);
        jPanel6.add(jComboBox2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, 150, 30));

        jLabel8.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel8.setText("Jumlah Pesan *");
        jPanel6.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 100, -1, -1));

        jTextField2.setText("Jumlah Pesan");
        jTextField2.addActionListener(this::jTextField2ActionPerformed);
        jPanel6.add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 120, 170, 30));

        jButton6.setFont(new java.awt.Font("Bahnschrift", 1, 12)); // NOI18N
        jButton6.setText("Batal");
        jButton6.addActionListener(this::jButton6ActionPerformed);
        jPanel6.add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 120, 150, 30));

        jLabel17.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel17.setText("Tanggal Pengiriman *");
        jPanel6.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 130, -1));

        jButton9.setBackground(new java.awt.Color(0, 105, 148));
        jButton9.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        jButton9.setForeground(new java.awt.Color(255, 255, 255));
        jButton9.setText("Simpan Distribusi");
        jButton9.addActionListener(this::jButton9ActionPerformed);
        jPanel6.add(jButton9, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 160, 150, 30));

        jComboBox6.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Toko Segar Jaya", "Depot Air Minum Barokah", "Toko Sembako Berkah ", "Minimarket Sentrosa", "Toko Tirta Abadi ", "Depot Sumber Rejeki", "Toko Maju Bersama ", "Minimarket Melati ", "Toko Kelontong Utama", "Depot Minum Sehat", "Toko Kasih Ibu", "Minimarket Berkat Jaya", "Depot Tirta Bening ", "Toko Makmur Sejahtera", "Toko Baru Palaran" }));
        jPanel6.add(jComboBox6, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 60, 170, 30));

        jLabel24.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel24.setText("Agen * (wilayah sama)");
        jPanel6.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 40, -1, -1));

        jPanel2.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, 580, 210));

        jLabel53.setText("4 November 2026");
        jPanel2.add(jLabel53, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 30, 110, -1));

        jLabel54.setText("Aulia Ashylla");
        jPanel2.add(jLabel54, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 40, 79, 10));

        jLabel55.setText("Admin");
        jPanel2.add(jLabel55, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 20, 79, -1));

        jButton2.setBackground(new java.awt.Color(255, 0, 51));
        jButton2.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jButton2.setForeground(new java.awt.Color(252, 252, 252));
        jButton2.setText("LogOut");
        jButton2.addActionListener(this::jButton2ActionPerformed);
        jPanel2.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 30, -1, -1));

        jLabel3.setBackground(new java.awt.Color(255, 255, 255));
        jLabel3.setFont(new java.awt.Font("Bahnschrift", 1, 36)); // NOI18N
        jLabel3.setText("Kelola Distribusi");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 409, 42));

        jLabel36.setBackground(new java.awt.Color(255, 255, 255));
        jLabel36.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        jLabel36.setText("Selamat siang, Budi. Berikut hasil produksi yang Anda catat hari ini.");
        jPanel2.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 120, 580, -1));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 0, 1100, 720));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3ActionPerformed

    private void jTextField5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField5ActionPerformed

    private void jComboBox5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox5ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        prosesLogout();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton11ActionPerformed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JComboBox<String> jComboBox5;
    private javax.swing.JComboBox<String> jComboBox6;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField5;
    // End of variables declaration//GEN-END:variables
}
