/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

/**
 *
 * @author Dovs
 */
public class DashboardDistribusiForm extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger =
            java.util.logging.Logger.getLogger(DashboardDistribusiForm.class.getName());
    private controller.SistemController sistemController;
    private controller.AkunController akunController;

    public DashboardDistribusiForm() {
        initComponents();
    }

    public DashboardDistribusiForm(controller.SistemController sistemController) {
        this();
        setSistemController(sistemController);
    }

    public void setSistemController(controller.SistemController sistemController) {
        if (sistemController == null || sistemController.getAkunController() == null
                || sistemController.getAkunController().getPetugasAktif() == null
                || sistemController.getAkunController().getPetugasAktif().getRole() != model.type.Role.Distribusi) {
            throw new SecurityException("Dashboard ini hanya dapat dibuka oleh Petugas Distribusi.");
        }
        this.sistemController = sistemController;
        this.akunController = sistemController.getAkunController();
        ViewHelper.setHeader(jLabel53, jLabel54, jLabel55, sistemController);
        ViewHelper.pasangSidebarStandar(jPanel1, this, sistemController);
        jLabel19.setText("Selamat datang, " + akunController.getPetugasAktif().getNama()
                + ". Pantau pengiriman sesuai wilayah tugas Anda.");
        jLabel17.setText("Petugas: " + akunController.getPetugasAktif().getNama());

        ViewHelper.pasangTombol(jButton1, e -> ViewHelper.bukaDashboard(this, sistemController));
        ViewHelper.pasangTombol(jButton4, e -> ViewHelper.buka(this, sistemController, "AGEN"));
        ViewHelper.pasangTombol(jButton8, e -> ViewHelper.buka(this, sistemController, "DISTRIBUSI"));
        ViewHelper.pasangTombol(jButton9, e -> ViewHelper.buka(this, sistemController, "LAPORAN"));
        ViewHelper.pasangTombol(jButton10, e -> ViewHelper.buka(this, sistemController, "STOK"));
        ViewHelper.pasangTombol(jButton5, e -> ViewHelper.buka(this, sistemController, "DISTRIBUSI"));
        ViewHelper.pasangTombol(jButton2, e -> ViewHelper.logout(this, sistemController));
        jButton10.setVisible(false);
        muatDashboard();
    }

    private void prosesLogout() { ViewHelper.logout(this, sistemController); }

    private void muatDashboard() {
        try {
            java.time.LocalDate hariIni = java.time.LocalDate.now();
            java.util.List<model.Distribusi> semua = sistemController.getDistribusiController().loadDistribusi();
            javax.swing.table.DefaultTableModel hari = new javax.swing.table.DefaultTableModel(
                    new Object[]{"ID Distribusi", "Agen", "Produk", "Jumlah", "Tanggal", "Status Transaksi"}, 0) {
                @Override public boolean isCellEditable(int r, int c) { return false; }
            };
            javax.swing.table.DefaultTableModel perjalanan = new javax.swing.table.DefaultTableModel(
                    new Object[]{"Distribusi", "Produk", "Agen", "Petugas"}, 0) {
                @Override public boolean isCellEditable(int r, int c) { return false; }
            };
            int jumlahHariIni = 0, jumlahDalamPerjalanan = 0, jumlahTerkirim = 0;
            for (model.Distribusi d : semua) {
                boolean hariIniTransaksi = d.getTanggalKirim().equals(hariIni);
                if (hariIniTransaksi) jumlahHariIni++;
                if (d.getStatusKirim() == model.type.StatusKirim.DALAM_PENGIRIMAN) jumlahDalamPerjalanan++;
                if (d.getStatusKirim() == model.type.StatusKirim.TERKIRIM) jumlahTerkirim++;
                for (model.DetailDistribusi detail : sistemController.getDistribusiController()
                        .loadDetailDistribusi(d.getIdDistribusi())) {
                    model.Agen agen = sistemController.getAgenController().cariAgen(detail.getIdAgen());
                    String namaAgen = agen == null ? detail.getIdAgen() : agen.getNamaAgen();
                    if (hariIniTransaksi) {
                        hari.addRow(new Object[]{d.getIdDistribusi(), namaAgen,
                            detail.getJenisProduk().getNamaProduk(), detail.getJumlahPesan(),
                            d.getTanggalKirim(), d.getStatusKirim().getNilaiDatabase()});
                    }
                    if (d.getStatusKirim() == model.type.StatusKirim.DALAM_PENGIRIMAN) {
                        perjalanan.addRow(new Object[]{d.getIdDistribusi(),
                            detail.getJenisProduk().getNamaProduk(), namaAgen,
                            akunController.getPetugasAktif().getNama()});
                    }
                }
            }
            jTable1.setModel(hari);
            jTable3.setModel(perjalanan);
            javax.swing.table.DefaultTableModel ringkasan = new javax.swing.table.DefaultTableModel(
                    new Object[]{"Pengiriman Hari Ini", "Dalam Perjalanan", "Terkirim", "Agen Wilayah"}, 0) {
                @Override public boolean isCellEditable(int r, int c) { return false; }
            };
            ringkasan.addRow(new Object[]{jumlahHariIni, jumlahDalamPerjalanan, jumlahTerkirim,
                sistemController.getAgenController().loadAgen().size()});
            jTable2.setModel(ringkasan);
            javax.swing.table.DefaultTableModel petugas = new javax.swing.table.DefaultTableModel(
                    new Object[]{"Petugas", "Distribusi Aktif"}, 0) {
                @Override public boolean isCellEditable(int r, int c) { return false; }
            };
            petugas.addRow(new Object[]{akunController.getPetugasAktif().getNama(), jumlahDalamPerjalanan});
            jTable4.setModel(petugas);
            jLabel31.setText("Pengiriman Hari Ini · " + jumlahHariIni);
            jLabel16.setText("Sedang Dalam Perjalanan · " + jumlahDalamPerjalanan);
        } catch (RuntimeException ex) { ViewHelper.error(this, ex); }
    }

@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jButton10 = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton5 = new javax.swing.JButton();
        jSeparator10 = new javax.swing.JSeparator();
        jLabel55 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jLabel53 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(0, 105, 148));
        jPanel1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Spring Logo with Swirl Emblem (2).png"))); // NOI18N
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 30, -1, -1));

        jButton1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(0, 105, 148));
        jButton1.setText("Dashboard");
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, 122, 33));

        jButton4.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jButton4.setForeground(new java.awt.Color(0, 105, 148));
        jButton4.setText("Data Agen");
        jPanel1.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 210, 122, 30));

        jButton8.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jButton8.setForeground(new java.awt.Color(0, 105, 148));
        jButton8.setText("Distribusi");
        jButton8.addActionListener(this::jButton8ActionPerformed);
        jPanel1.add(jButton8, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 370, 122, 30));

        jButton9.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jButton9.setForeground(new java.awt.Color(0, 105, 148));
        jButton9.setText("Laporan");
        jButton9.addActionListener(this::jButton9ActionPerformed);
        jPanel1.add(jButton9, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 580, 122, 37));

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setFont(new java.awt.Font("Arial Black", 1, 13)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Operasional");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 70, 90, 20));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Spring Logo with Swirl Emblem (2).png"))); // NOI18N
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 30, -1, -1));

        jLabel8.setFont(new java.awt.Font("Arial Black", 1, 11)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Data");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 190, 30, 20));

        jButton10.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jButton10.setForeground(new java.awt.Color(0, 105, 148));
        jButton10.setText("Gudang");
        jPanel1.add(jButton10, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 330, 122, 30));

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

        jLabel16.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel16.setText("Sedang Dalam Perjalanan");

        jTable3.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Distribusi", "Produk", "Agen", "Petugas"
            }
        ));
        jScrollPane3.setViewportView(jTable3);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 578, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)
                .addGap(12, 12, 12))
        );

        jPanel2.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 276, 610, 130));

        jPanel4.setBackground(new java.awt.Color(252, 252, 252));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel2.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel31.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel31.setText("Pengiriman Hari Ini");
        jPanel3.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 180, -1));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Id Distribusi", "Agen", "Produk", "Jumlah", "Waktu", "Status Transaksi"
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

        jPanel3.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 1009, 200));

        jButton5.setBackground(new java.awt.Color(0, 105, 148));
        jButton5.setForeground(new java.awt.Color(255, 255, 255));
        jButton5.setText("Semua Riwayat ↗");
        jButton5.addActionListener(this::jButton5ActionPerformed);
        jPanel3.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 20, 155, 26));

        jPanel2.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 420, 1040, 270));

        jSeparator10.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel2.add(jSeparator10, new org.netbeans.lib.awtextra.AbsoluteConstraints(873, 20, -1, 33));

        jLabel55.setText("Admin");
        jPanel2.add(jLabel55, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 20, 79, -1));

        jLabel54.setText("Aulia Ashylla");
        jPanel2.add(jLabel54, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 40, 79, 10));

        jButton2.setBackground(new java.awt.Color(255, 0, 51));
        jButton2.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jButton2.setForeground(new java.awt.Color(252, 252, 252));
        jButton2.setText("LogOut");
        jButton2.addActionListener(this::jButton2ActionPerformed);
        jPanel2.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 30, -1, -1));

        jLabel3.setBackground(new java.awt.Color(255, 255, 255));
        jLabel3.setFont(new java.awt.Font("Bahnschrift", 1, 36)); // NOI18N
        jLabel3.setText("Dashboard Distribusi");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 409, 42));

        jLabel19.setBackground(new java.awt.Color(255, 255, 255));
        jLabel19.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        jLabel19.setText("Selamat siang, Dimas. Pantau pengiriman dan pastikan barang sampai ke distributor.");
        jPanel2.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 120, 580, -1));

        jTable2.setFont(new java.awt.Font("Arial Black", 0, 14)); // NOI18N
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Pengiriman Hari Ini", "Dalam Perjalanan", "Terkirim", "Total Agen"
            }
        ));
        jScrollPane2.setViewportView(jTable2);

        jPanel2.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 150, 1040, 110));

        jLabel53.setText("4 November 2026");
        jPanel2.add(jLabel53, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 30, 110, -1));

        jLabel17.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel17.setText("Petugas Distribusi");

        jTable4.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Petugas", "Distribusi"
            }
        ));
        jScrollPane4.setViewportView(jTable4);

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 382, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(14, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)
                .addGap(12, 12, 12))
        );

        jPanel2.add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(642, 280, 410, -1));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 0, 1100, 720));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        prosesLogout();
    }//GEN-LAST:event_jButton2ActionPerformed

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
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    // End of variables declaration//GEN-END:variables
}
