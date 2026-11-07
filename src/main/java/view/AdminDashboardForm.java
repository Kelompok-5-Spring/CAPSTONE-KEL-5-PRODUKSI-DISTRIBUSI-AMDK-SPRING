/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

/**
 *
 * @author Dovs
 */
public class AdminDashboardForm extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger =
            java.util.logging.Logger.getLogger(AdminDashboardForm.class.getName());
    private controller.SistemController sistemController;
    private controller.AkunController akunController;

    public AdminDashboardForm() {
        initComponents();
    }

    public AdminDashboardForm(controller.SistemController sistemController) {
        this();
        setSistemController(sistemController);
    }

    public void setSistemController(controller.SistemController sistemController) {
        if (sistemController == null || sistemController.getAkunController() == null
                || sistemController.getAkunController().getPetugasAktif() == null
                || sistemController.getAkunController().getPetugasAktif().getRole() != model.type.Role.Admin) {
            throw new SecurityException("Dashboard ini hanya dapat dibuka oleh Admin.");
        }
        this.sistemController = sistemController;
        this.akunController = sistemController.getAkunController();
        ViewHelper.setHeader(jLabel53, jLabel54, jLabel55, sistemController);
        ViewHelper.pasangSidebarStandar(jPanel1, this, sistemController);
        jLabel6.setText("Selamat datang, " + akunController.getPetugasAktif().getNama()
                + ". Pantau produksi, stok, dan distribusi perusahaan.");
        jLabel17.setText("Ringkasan operasional tujuh hari terakhir.");

        ViewHelper.pasangTombol(jButton1, e -> ViewHelper.bukaDashboard(this, sistemController));
        ViewHelper.pasangTombol(jButton3, e -> ViewHelper.buka(this, sistemController, "PRODUK"));
        ViewHelper.pasangTombol(jButton4, e -> ViewHelper.buka(this, sistemController, "AGEN"));
        ViewHelper.pasangTombol(jButton5, e -> ViewHelper.buka(this, sistemController, "PRODUKSI"));
        ViewHelper.pasangTombol(jButton6, e -> ViewHelper.buka(this, sistemController, "STOK"));
        ViewHelper.pasangTombol(jButton7, e -> ViewHelper.buka(this, sistemController, "DISTRIBUSI"));
        ViewHelper.pasangTombol(jButton8, e -> ViewHelper.buka(this, sistemController, "LAPORAN"));
        ViewHelper.pasangTombol(jButton9, e -> ViewHelper.buka(this, sistemController, "AKUN"));
        ViewHelper.pasangTombol(jButton2, e -> ViewHelper.logout(this, sistemController));
        muatDashboard();
    }

    private void prosesLogout() { ViewHelper.logout(this, sistemController); }

    private void muatDashboard() {
        try {
            java.time.LocalDate hariIni = java.time.LocalDate.now();
            java.time.LocalDate awal = hariIni.minusDays(6);
            java.util.List<model.Produksi> produksi = sistemController.getProduksiController().loadProduksi();
            java.util.List<model.Distribusi> distribusi = sistemController.getDistribusiController().loadDistribusi();
            java.util.List<model.Produk> produk = sistemController.getProdukController().loadProduk();
            java.util.List<model.Agen> agen = sistemController.getAgenController().loadAgen();
            java.util.List<model.Petugas> akun = akunController.loadAkun();

            int produksiHariIni = 0;
            int kirimHariIni = 0;
            for (model.Produksi p : produksi) if (p.getTanggalProduksi().equals(hariIni)) produksiHariIni++;
            for (model.Distribusi d : distribusi) if (d.getTanggalKirim().equals(hariIni)) kirimHariIni++;

            javax.swing.table.DefaultTableModel metrik = new javax.swing.table.DefaultTableModel(
                    new Object[]{"Pencatatan Produksi", "Pengiriman Hari Ini", "Produk Kosong", "Akun Pegawai Aktif"}, 0) {
                @Override public boolean isCellEditable(int r, int c) { return false; }
            };
            int kosong = 0;
            for (model.type.JenisProduk jenis : model.type.JenisProduk.values()) {
                if (sistemController.getProdukController().hitungStokTersedia(jenis) == 0) kosong++;
            }
            metrik.addRow(new Object[]{produksiHariIni, kirimHariIni, kosong, akun.size()});
            jTable2.setModel(metrik);

            javax.swing.table.DefaultTableModel stok = new javax.swing.table.DefaultTableModel(
                    new Object[]{"Produk", "Stok Awal", "Produksi Hari Ini", "Barang Keluar",
                        "Stok Akhir", "Status Akhir"}, 0) {
                @Override public boolean isCellEditable(int r, int c) { return false; }
            };
            StringBuilder grafik = new StringBuilder("<html><div style='font-family:Arial;font-size:12px;width:360px;'><b>Perbandingan produksi dan distribusi</b><br>");
            for (model.type.JenisProduk jenis : model.type.JenisProduk.values()) {
                int masuk = 0, keluar = 0;
                for (model.Produksi p : produksi) {
                    if (p.getTanggalProduksi().equals(hariIni)
                            && p.getStatusProduksi() == model.type.StatusProduksi.Selesai
                            && p.getJenisProduksi().equalsIgnoreCase(jenis.getNamaProduk())) masuk += p.getJumlahProduksi();
                }
                for (model.Distribusi d : distribusi) {
                    if (!d.getTanggalKirim().equals(hariIni)
                            || d.getStatusKirim() != model.type.StatusKirim.TERKIRIM) continue;
                    for (model.DetailDistribusi detail : sistemController.getDistribusiController()
                            .loadDetailDistribusi(d.getIdDistribusi())) {
                        if (detail.getJenisProduk() == jenis) keluar += detail.getJumlahPesan();
                    }
                }
                int tersedia = sistemController.getProdukController().hitungStokTersedia(jenis);
                stok.addRow(new Object[]{jenis.getNamaProduk(), "—", masuk, keluar, tersedia,
                    tersedia > 0 ? "Tersedia" : "Kosong"});
                int skala = Math.max(1, Math.max(masuk, Math.max(keluar, tersedia)));
                int lebarMasuk = Math.max(2, (int) Math.round(115.0 * masuk / skala));
                int lebarKeluar = Math.max(2, (int) Math.round(115.0 * keluar / skala));
                int lebarStok = Math.max(2, (int) Math.round(115.0 * tersedia / skala));
                grafik.append("<div style='margin-top:3px'><b>").append(jenis.getNamaProduk()).append("</b> &nbsp;")
                        .append("<span style='color:#1686b5'>P</span><span style='display:inline-block;background:#1686b5;width:").append(lebarMasuk).append("px;height:7px'></span> ").append(masuk).append(" &nbsp;")
                        .append("<span style='color:#e49b35'>K</span><span style='display:inline-block;background:#e49b35;width:").append(lebarKeluar).append("px;height:7px'></span> ").append(keluar).append(" &nbsp;")
                        .append("<span style='color:#3b9b72'>S</span><span style='display:inline-block;background:#3b9b72;width:").append(lebarStok).append("px;height:7px'></span> ").append(tersedia).append("</div>");
            }
            grafik.append("</div></html>");
            jTable1.setModel(stok);
            jLabel1.setText(grafik.toString());

            java.util.List<String> aktivitas = new java.util.ArrayList<>();
            for (model.Produksi p : produksi) {
                if (!p.getTanggalProduksi().isBefore(awal) && !p.getTanggalProduksi().isAfter(hariIni)) {
                    aktivitas.add(p.getTanggalProduksi() + " · Produksi " + p.getJenisProduksi()
                            + " · " + p.getStatusProduksi());
                }
            }
            for (model.Distribusi d : distribusi) {
                if (!d.getTanggalKirim().isBefore(awal) && !d.getTanggalKirim().isAfter(hariIni)) {
                    aktivitas.add(d.getTanggalKirim() + " · Distribusi " + d.getIdDistribusi()
                            + " · " + d.getStatusKirim());
                }
            }
            aktivitas.sort(java.util.Comparator.reverseOrder());
            StringBuilder ringkasAktivitas = new StringBuilder("<html><div style='font-family:Arial;font-size:12px;'>");
            for (int i = 0; i < Math.min(6, aktivitas.size()); i++) {
                ringkasAktivitas.append("<p>").append(aktivitas.get(i)).append("</p>");
            }
            if (aktivitas.isEmpty()) ringkasAktivitas.append("<p>Belum ada aktivitas pada tujuh hari terakhir.</p>");
            ringkasAktivitas.append("</div></html>");
            
            jLabel18.setText("7 Hari Terakhir · " + awal + " s.d. " + hariIni);
        } catch (RuntimeException ex) { ViewHelper.error(this, ex); }
    }

@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jSeparator10 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(0, 105, 148));
        jPanel1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(0, 105, 148));
        jButton1.setText("Dashboard");
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, 122, 33));

        jButton3.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jButton3.setForeground(new java.awt.Color(0, 105, 148));
        jButton3.setText("Data Produk");
        jPanel1.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 210, 122, 30));

        jButton4.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jButton4.setForeground(new java.awt.Color(0, 105, 148));
        jButton4.setText("Data Agen");
        jPanel1.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 250, 122, 30));

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

        jButton8.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jButton8.setForeground(new java.awt.Color(0, 105, 148));
        jButton8.setText("Laporan");
        jButton8.addActionListener(this::jButton8ActionPerformed);
        jPanel1.add(jButton8, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 580, 122, 37));

        jButton9.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jButton9.setForeground(new java.awt.Color(0, 105, 148));
        jButton9.setText("Akun Pengguna");
        jButton9.addActionListener(this::jButton9ActionPerformed);
        jPanel1.add(jButton9, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 500, 122, 30));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Spring Logo with Swirl Emblem (2).png"))); // NOI18N
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 30, -1, -1));

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setFont(new java.awt.Font("Arial Black", 1, 13)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Operasional");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 70, 90, 20));

        jLabel7.setFont(new java.awt.Font("Arial Black", 1, 11)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Administrasi");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 480, 90, 20));

        jLabel8.setFont(new java.awt.Font("Arial Black", 1, 11)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Data");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 190, 30, 20));

        jLabel9.setFont(new java.awt.Font("Arial Black", 1, 11)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Operasional");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 310, 90, 20));

        jLabel10.setBackground(new java.awt.Color(255, 255, 255));
        jLabel10.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Spring Operasional");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 660, -1, 20));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 180, 720));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel11.setBackground(new java.awt.Color(248, 248, 248));

        jLabel16.setFont(new java.awt.Font("Arial Black", 0, 14)); // NOI18N
        jLabel16.setText("Produksi & Barang Keluar");

        jLabel17.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(128, 128, 128));
        jLabel17.setText("Botol 600 ml · dus isi 24 · 21–27 Oktober 2026");

        jLabel18.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(128, 128, 128));
        jLabel18.setText("7 Hari Terakhir");

        jLabel1.setText("jLabel1");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 263, Short.MAX_VALUE)
                        .addComponent(jLabel18)))
                .addGap(20, 20, 20))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel17))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(jLabel18)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 278, -1, 200));

        jLabel3.setBackground(new java.awt.Color(255, 255, 255));
        jLabel3.setFont(new java.awt.Font("Bahnschrift", 1, 36)); // NOI18N
        jLabel3.setText("Dashboard Admin");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 409, 42));

        jPanel13.setBackground(new java.awt.Color(248, 248, 248));

        jLabel28.setFont(new java.awt.Font("Arial Black", 0, 14)); // NOI18N
        jLabel28.setText("Posisi Stok Produk Jadi");

        jTable1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Produk", "Stok Awal", "Produksi Hari Ini", "Barang Keluar", "Stok Akhir", "Status Akhir"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Float.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jTable1.setAutoscrolls(false);
        jTable1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1006, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel28)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 488, -1, 200));

        jTable2.setFont(new java.awt.Font("Arial Black", 0, 24)); // NOI18N
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null}
            },
            new String [] {
                "Pencatatan produksi ", "Pengiriman hari ini ", "Produk stok menipis ", "Akun pegawai aktif "
            }
        ));
        jScrollPane2.setViewportView(jTable2);

        jPanel2.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 150, 1040, 110));

        jLabel6.setBackground(new java.awt.Color(255, 255, 255));
        jLabel6.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        jLabel6.setText("Selamat siang admin Pantau aliran produksi, stok, dan distribusi hari ini.");
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 120, 484, -1));

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

        jSeparator10.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel2.add(jSeparator10, new org.netbeans.lib.awtextra.AbsoluteConstraints(873, 20, -1, 33));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 0, 1100, 730));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        prosesLogout();
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    // End of variables declaration//GEN-END:variables
}
