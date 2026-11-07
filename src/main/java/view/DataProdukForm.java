/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

/**
 *
 * @author Dovs
 */
public class DataProdukForm extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger =
            java.util.logging.Logger.getLogger(DataProdukForm.class.getName());
    private controller.AkunController akunController;
    private controller.SistemController sistemController;

    public DataProdukForm() {
        initComponents();
    }

    public DataProdukForm(controller.SistemController sistemController) {
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
        jTextField3.setText("");
        jTextField2.setEditable(false); // ID produk tidak boleh berubah.
        jTextField5.setEditable(false); // Kemasan mengikuti data produksi.
        jTextField7.setEditable(false); // Nama produk mengikuti jenis produk.
        jTextField5.setToolTipText("Isi kemasan mengikuti spesifikasi produksi dan tidak dapat diubah dari halaman ini.");
        jTextField7.setToolTipText("Nama produk mengikuti jenis produk dan tidak dapat diubah dari halaman ini.");
        jComboBox2.setEnabled(false);
        jComboBox3.setEnabled(false);
        jComboBox4.setEnabled(false);
        jTextField2.setText("");
        jTextField5.setText("");
        jTextField6.setText("");
        jTextField7.setText("");
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(
                new String[]{"Semua Jenis", "Botol", "Galon"}));
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Botol", "Galon"}));
        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"DUS", "GALON"}));
        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Tersedia", "Dialokasikan"}));

        ViewHelper.pasangTombol(jButton1, e -> ViewHelper.bukaDashboard(this, sistemController));
        ViewHelper.pasangTombol(jButton3, e -> ViewHelper.kembali(this, sistemController));
        ViewHelper.pasangTombol(jButton4, e -> ViewHelper.buka(this, sistemController, "AGEN"));
        ViewHelper.pasangTombol(jButton8, e -> ViewHelper.buka(this, sistemController, "LAPORAN"));
        ViewHelper.pasangTombol(jButton2, e -> kosongkanProduk());
        ViewHelper.pasangTombol(jButton10, e -> simpanPerubahanProduk());
        ViewHelper.pasangTombol(jButton11, e -> ViewHelper.logout(this, sistemController));

        boolean bolehUbah = role == model.type.Role.Admin || role == model.type.Role.Gudang;
        jButton4.setVisible(ViewHelper.bolehAkses(role, "AGEN"));
        jTextField6.setEditable(bolehUbah);
        jButton10.setEnabled(bolehUbah);

        jTable1.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && jTable1.getSelectedRow() >= 0) isiFormProduk();
        });
        jTextField3.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { muatProduk(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { muatProduk(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { muatProduk(); }
        });
        jComboBox1.addActionListener(e -> muatProduk());

        javax.swing.JPopupMenu menu = new javax.swing.JPopupMenu();
        javax.swing.JMenuItem hapus = new javax.swing.JMenuItem("Hapus Produk");
        hapus.addActionListener(e -> hapusProdukTerpilih());
        hapus.setEnabled(role == model.type.Role.Admin);
        menu.add(hapus);
        jTable1.setComponentPopupMenu(menu);
        ViewHelper.pilihBarisSaatKlikKanan(jTable1);
        muatProduk();
    }

    private void muatProduk() {
        if (sistemController == null) return;
        try {
            javax.swing.table.DefaultTableModel m = new javax.swing.table.DefaultTableModel(
                    new Object[]{"ID Produk", "Produk", "Jenis", "Satuan", "Kemasan", "Harga Satuan", "Status"}, 0) {
                @Override public boolean isCellEditable(int r, int c) { return false; }
            };
            String cari = jTextField3.getText().trim().toLowerCase(java.util.Locale.ROOT);
            String filter = String.valueOf(jComboBox1.getSelectedItem());
            java.util.List<model.Produk> data = sistemController.getProdukController().loadProduk();
            for (model.Produk p : data) {
                String jenis = p.getJenisProduk().isBotol() ? "Botol" : "Galon";
                boolean cocokCari = cari.isEmpty() || cari.startsWith("cari id")
                        || p.getIdProduk().toLowerCase(java.util.Locale.ROOT).contains(cari)
                        || p.getNamaProduk().toLowerCase(java.util.Locale.ROOT).contains(cari);
                boolean cocokJenis = filter.equals("Semua Jenis") || filter.equals(jenis);
                if (cocokCari && cocokJenis) {
                    m.addRow(new Object[]{p.getIdProduk(), p.getNamaProduk(),
                        p.getJenisProduk().getNamaProduk(), p.getSatuanJual(),
                        p.getDeskripsiKemasan(), p.getHargaSatuan(),
                        p.getIdDistribusi() == null ? "Tersedia" : "Dialokasikan"});
                }
            }
            jTable1.setModel(m);
            jLabel17.setText(m.getRowCount() + " produk ditampilkan dari " + data.size() + " produk.");
        } catch (RuntimeException ex) { ViewHelper.error(this, ex); }
    }

    private model.Produk produkTerpilih() {
        int row = jTable1.getSelectedRow();
        if (row < 0) return null;
        return sistemController.getProdukController().cariProduk(String.valueOf(jTable1.getValueAt(row, 0)));
    }

    private void isiFormProduk() {
        model.Produk p = produkTerpilih();
        if (p == null) return;
        jTextField2.setText(p.getIdProduk());
        jTextField5.setText(p.getDeskripsiKemasan());
        jTextField6.setText(p.getHargaSatuan().toPlainString());
        jTextField7.setText(p.getNamaProduk());
        jComboBox2.setSelectedItem(p.getJenisProduk().isBotol() ? "Botol" : "Galon");
        jComboBox3.setSelectedItem(p.getSatuanJual());
        jComboBox4.setSelectedItem(p.getIdDistribusi() == null ? "Aktif" : "Dialokasikan");
        boolean bolehUbah = akunController.getPetugasAktif().getRole() == model.type.Role.Admin
                || akunController.getPetugasAktif().getRole() == model.type.Role.Gudang;
        jTextField6.setEditable(bolehUbah);
        jButton10.setEnabled(bolehUbah);
    }

    private void simpanPerubahanProduk() {
        model.Produk p = produkTerpilih();
        if (p == null) {
            ViewHelper.info(this, "Pilih produk dari tabel terlebih dahulu.");
            return;
        }
        if (!ViewHelper.konfirmasi(this, "Simpan perubahan harga satuan produk?")) return;
        try {
            sistemController.getProdukController().ubahHargaProduk(p.getIdProduk(), jTextField6.getText());
            muatProduk();
            ViewHelper.info(this, "Harga satuan produk berhasil diperbarui.");
        } catch (RuntimeException ex) { ViewHelper.error(this, ex); }
    }

    private void hapusProdukTerpilih() {
        if (akunController.getPetugasAktif().getRole() != model.type.Role.Admin) {
            ViewHelper.info(this, "Hanya Admin yang dapat menghapus produk.");
            return;
        }
        model.Produk p = produkTerpilih();
        if (p == null) {
            ViewHelper.info(this, "Pilih produk terlebih dahulu.");
            return;
        }
        if (!ViewHelper.konfirmasi(this, "Hapus produk " + p.getIdProduk() + "?")) return;
        try {
            sistemController.getProdukController().hapusProduk(p.getIdProduk());
            kosongkanProduk();
            muatProduk();
            ViewHelper.info(this, "Produk berhasil dihapus.");
        } catch (RuntimeException ex) { ViewHelper.error(this, ex); }
    }

    private void kosongkanProduk() {
        jTextField2.setText("");
        jTextField5.setText("");
        jTextField6.setText("");
        jTextField7.setText("");
        jTable1.clearSelection();
        jTextField6.setEditable(akunController != null
                && (akunController.getPetugasAktif().getRole() == model.type.Role.Admin
                || akunController.getPetugasAktif().getRole() == model.type.Role.Gudang));
        jButton10.setEnabled(jTextField6.isEditable());
    }

    public void refreshData() { muatProduk(); }

    private void prosesLogout() { ViewHelper.logout(this, sistemController); }

@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jTextField3 = new javax.swing.JTextField();
        jPanel12 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jComboBox3 = new javax.swing.JComboBox<>();
        jLabel25 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jTextField7 = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        jComboBox4 = new javax.swing.JComboBox<>();
        jButton2 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jLabel26 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        jSeparator10 = new javax.swing.JSeparator();
        jLabel54 = new javax.swing.JLabel();
        jButton11 = new javax.swing.JButton();
        jLabel36 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(0, 105, 148));
        jPanel1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton3.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jButton3.setForeground(new java.awt.Color(0, 105, 148));
        jButton3.setText("Kembali");
        jPanel1.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 210, 122, 30));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Spring Logo with Swirl Emblem (2).png"))); // NOI18N
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 30, -1, -1));

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setFont(new java.awt.Font("Arial Black", 1, 13)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Operasional");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 70, 90, 20));

        jButton1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(0, 105, 148));
        jButton1.setText("Dashboard");
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, 122, 33));

        jButton4.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jButton4.setForeground(new java.awt.Color(0, 105, 148));
        jButton4.setText("Data Agen");
        jPanel1.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 250, 122, 30));

        jButton8.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jButton8.setForeground(new java.awt.Color(0, 105, 148));
        jButton8.setText("Laporan");
        jButton8.addActionListener(this::jButton8ActionPerformed);
        jPanel1.add(jButton8, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 370, 122, 37));

        jLabel10.setBackground(new java.awt.Color(255, 255, 255));
        jLabel10.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Spring Operasional");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 660, -1, 20));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 180, 720));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel16.setFont(new java.awt.Font("Arial Black", 0, 14)); // NOI18N
        jLabel16.setText("Daftar Produk");

        jLabel17.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(128, 128, 128));
        jLabel17.setText("4 produk aktif · 3 jenis kemasan");

        jComboBox1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Semua Jenis", "Galon", "Botol" }));
        jComboBox1.addActionListener(this::jComboBox1ActionPerformed);

        jTable1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Id Produk", "Produk", "Jenis", "Satuan", "Harga Satuan", "Status"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jTextField3.setText("Cari Id Produk atau Produk");
        jTextField3.addActionListener(this::jTextField3ActionPerformed);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 513, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 643, Short.MAX_VALUE))
                        .addGap(40, 40, 40))))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel17)
                .addGap(4, 4, 4)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(32, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 144, 710, 350));

        jPanel12.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel19.setBackground(new java.awt.Color(255, 255, 255));
        jLabel19.setFont(new java.awt.Font("Arial Black", 0, 14)); // NOI18N
        jLabel19.setText("Ubah Produk");
        jPanel12.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 16, 206, -1));

        jLabel20.setBackground(new java.awt.Color(255, 255, 255));
        jLabel20.setFont(new java.awt.Font("Arial", 0, 8)); // NOI18N
        jLabel20.setText("Kode Unik Tidak Dapat Diubah");
        jPanel12.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, 206, 20));

        jTextField2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jTextField2.setText("Id Produk");
        jTextField2.addActionListener(this::jTextField2ActionPerformed);
        jPanel12.add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 260, 31));

        jLabel21.setBackground(new java.awt.Color(255, 255, 255));
        jLabel21.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel21.setText("Produk");
        jPanel12.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 90, -1));

        jLabel22.setBackground(new java.awt.Color(255, 255, 255));
        jLabel22.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel22.setText("Id Produk");
        jPanel12.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 90, -1));

        jLabel23.setBackground(new java.awt.Color(255, 255, 255));
        jLabel23.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel23.setText("Jenis Produk *");
        jPanel12.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 200, 116, -1));

        jTextField5.setText("24 Botol");
        jTextField5.addActionListener(this::jTextField5ActionPerformed);
        jPanel12.add(jTextField5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 280, 110, 34));

        jLabel24.setBackground(new java.awt.Color(255, 255, 255));
        jLabel24.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel24.setText("Satuan Jual *");
        jPanel12.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 200, 90, -1));

        jComboBox2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Galon", "Botol" }));
        jComboBox2.addActionListener(this::jComboBox2ActionPerformed);
        jPanel12.add(jComboBox2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 220, 116, 32));

        jComboBox3.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Dus ", "Pack", "Unit", " ", " ", " " }));
        jComboBox3.addActionListener(this::jComboBox3ActionPerformed);
        jPanel12.add(jComboBox3, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 220, 130, 32));

        jLabel25.setBackground(new java.awt.Color(255, 255, 255));
        jLabel25.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel25.setText("Harga Satuan");
        jPanel12.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 260, 110, 20));

        jTextField6.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jTextField6.setText("Masukkan harga satuan");
        jTextField6.addActionListener(this::jTextField6ActionPerformed);
        jPanel12.add(jTextField6, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 280, 140, 34));

        jTextField7.setText("Isi Nama Produk");
        jTextField7.addActionListener(this::jTextField7ActionPerformed);
        jPanel12.add(jTextField7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 150, 260, 40));

        jLabel27.setBackground(new java.awt.Color(255, 255, 255));
        jLabel27.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel27.setText("Status Produk");
        jPanel12.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 320, 100, 20));

        jComboBox4.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Aktif", "Tidak Aktif", " " }));
        jComboBox4.addActionListener(this::jComboBox4ActionPerformed);
        jPanel12.add(jComboBox4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 350, 254, 32));

        jButton2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton2.setText("Batal");
        jButton2.addActionListener(this::jButton2ActionPerformed);
        jPanel12.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 400, 110, -1));

        jButton10.setBackground(new java.awt.Color(0, 105, 148));
        jButton10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton10.setForeground(new java.awt.Color(255, 255, 255));
        jButton10.setText("Simpan");
        jButton10.addActionListener(this::jButton10ActionPerformed);
        jPanel12.add(jButton10, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 400, 136, -1));

        jLabel26.setBackground(new java.awt.Color(255, 255, 255));
        jLabel26.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel26.setText("Isi per Kemasan");
        jPanel12.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 260, 110, 20));

        jPanel2.add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(754, 144, 300, 440));

        jLabel3.setBackground(new java.awt.Color(255, 255, 255));
        jLabel3.setFont(new java.awt.Font("Bahnschrift", 1, 36)); // NOI18N
        jLabel3.setText("Data Produk");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 409, 42));

        jLabel53.setText("4 November 2026");
        jPanel2.add(jLabel53, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 30, 110, -1));

        jLabel55.setText("Admin");
        jPanel2.add(jLabel55, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 20, 79, -1));

        jSeparator10.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel2.add(jSeparator10, new org.netbeans.lib.awtextra.AbsoluteConstraints(873, 20, -1, 33));

        jLabel54.setText("Aulia Ashylla");
        jPanel2.add(jLabel54, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 40, 79, 10));

        jButton11.setBackground(new java.awt.Color(255, 0, 51));
        jButton11.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jButton11.setForeground(new java.awt.Color(252, 252, 252));
        jButton11.setText("LogOut");
        jButton11.addActionListener(this::jButton11ActionPerformed);
        jPanel2.add(jButton11, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 30, -1, -1));

        jLabel36.setBackground(new java.awt.Color(255, 255, 255));
        jLabel36.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        jLabel36.setText("Selamat siang, Budi. Data Produk");
        jPanel2.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 120, 300, -1));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 0, 1100, 720));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jTextField5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField5ActionPerformed

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void jComboBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox3ActionPerformed

    private void jTextField6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField6ActionPerformed

    private void jTextField7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField7ActionPerformed

    private void jComboBox4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox4ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        prosesLogout();
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {
        ViewHelper.info(this, "Produk dibuat otomatis setelah produksi berstatus Selesai. Produk tidak ditambahkan manual.");
    }

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton8;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    // End of variables declaration//GEN-END:variables
}
