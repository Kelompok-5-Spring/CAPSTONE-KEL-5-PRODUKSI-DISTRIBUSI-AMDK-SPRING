/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

/**
 *
 * @author Dovs
 */
public class DashboardProduksiForm extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger =
            java.util.logging.Logger.getLogger(DashboardProduksiForm.class.getName());
    private controller.SistemController sistemController;
    private controller.AkunController akunController;

    public DashboardProduksiForm() {
        initComponents();
    }

    public DashboardProduksiForm(controller.SistemController sistemController) {
        this();
        setSistemController(sistemController);
    }

    public void setSistemController(controller.SistemController sistemController) {
        if (sistemController == null || sistemController.getAkunController() == null
                || sistemController.getAkunController().getPetugasAktif() == null
                || sistemController.getAkunController().getPetugasAktif().getRole() != model.type.Role.Produksi) {
            throw new SecurityException("Dashboard ini hanya dapat dibuka oleh Petugas Produksi.");
        }
        this.sistemController = sistemController;
        this.akunController = sistemController.getAkunController();
        ViewHelper.setHeader(jLabel53, jLabel54, jLabel55, sistemController);
        ViewHelper.pasangSidebarStandar(jPanel1, this, sistemController);
        java.time.LocalDate hariIni = java.time.LocalDate.now();
        jLabel29.setText("Produksi: " + hariIni);
        jLabel27.setText("Selamat datang, " + akunController.getPetugasAktif().getNama()
                + ". Berikut catatan produksi yang menjadi tanggung jawab Anda.");

        ViewHelper.pasangTombol(jButton1, e -> ViewHelper.bukaDashboard(this, sistemController));
        ViewHelper.pasangTombol(jButton5, e -> ViewHelper.buka(this, sistemController, "PRODUKSI"));
        ViewHelper.pasangTombol(jButton8, e -> ViewHelper.buka(this, sistemController, "LAPORAN"));
        ViewHelper.pasangTombol(jButton2, e -> ViewHelper.logout(this, sistemController));
        muatDashboard();
    }

    private void prosesLogout() { ViewHelper.logout(this, sistemController); }

    private void muatDashboard() {
        try {
            java.time.LocalDate hariIni = java.time.LocalDate.now();
            java.util.List<model.Produksi> semua = sistemController.getProduksiController().loadProduksi();
            javax.swing.table.DefaultTableModel harian = new javax.swing.table.DefaultTableModel(
                    new Object[]{"Produk", "Hasil", "Tanggal", "Status"}, 0) {
                @Override public boolean isCellEditable(int r, int c) { return false; }
            };
            java.util.Map<model.type.JenisProduk, Integer> jumlahSelesai =
                    new java.util.EnumMap<>(model.type.JenisProduk.class);
            for (model.type.JenisProduk jenis : model.type.JenisProduk.values()) jumlahSelesai.put(jenis, 0);
            int totalHariIni = 0;
            for (model.Produksi p : semua) {
                if (p.getStatusProduksi() == model.type.StatusProduksi.Selesai) {
                    model.type.JenisProduk jenis = jenisDariNama(p.getJenisProduksi());
                    jumlahSelesai.put(jenis, jumlahSelesai.get(jenis) + p.getJumlahProduksi());
                }
                if (p.getTanggalProduksi().equals(hariIni)) {
                    harian.addRow(new Object[]{p.getJenisProduksi(), p.getJumlahProduksi(),
                        p.getTanggalProduksi(), p.getStatusProduksi().getNilaiDatabase()});
                    totalHariIni += p.getJumlahProduksi();
                }
            }
            jTable2.setModel(harian);

            javax.swing.table.DefaultTableModel ringkasan = new javax.swing.table.DefaultTableModel(
                    new Object[]{"Botol 600 ml", "Botol 1500 ml", "Galon 19 L", "Galon 5 L", "Total Produksi Selesai"}, 0) {
                @Override public boolean isCellEditable(int r, int c) { return false; }
            };
            int total = 0;
            for (int n : jumlahSelesai.values()) total += n;
            ringkasan.addRow(new Object[]{
                jumlahSelesai.get(model.type.JenisProduk.BOTOL_600ML),
                jumlahSelesai.get(model.type.JenisProduk.BOTOL_1500ML),
                jumlahSelesai.get(model.type.JenisProduk.GALON_19L),
                jumlahSelesai.get(model.type.JenisProduk.GALON_5L), total});
            jTable3.setModel(ringkasan);

            int penyebut = Math.max(1, total);
            jLabel33.setText(Math.round(100.0 * jumlahSelesai.get(model.type.JenisProduk.GALON_19L) / penyebut) + "%");
            jLabel34.setText(Math.round(100.0 * jumlahSelesai.get(model.type.JenisProduk.GALON_5L) / penyebut) + "%");
            jLabel35.setText(Math.round(100.0 * jumlahSelesai.get(model.type.JenisProduk.BOTOL_1500ML) / penyebut) + "%");
            jLabel36.setText(Math.round(100.0 * jumlahSelesai.get(model.type.JenisProduk.BOTOL_600ML) / penyebut) + "%");
            jProgressBar5.setValue((int) Math.round(100.0 * jumlahSelesai.get(model.type.JenisProduk.BOTOL_600ML) / penyebut));
            jProgressBar6.setValue((int) Math.round(100.0 * jumlahSelesai.get(model.type.JenisProduk.BOTOL_1500ML) / penyebut));
            jProgressBar7.setValue((int) Math.round(100.0 * jumlahSelesai.get(model.type.JenisProduk.GALON_5L) / penyebut));
            jProgressBar8.setValue((int) Math.round(100.0 * jumlahSelesai.get(model.type.JenisProduk.GALON_19L) / penyebut));
            jProgressBar5.setStringPainted(true);
            jProgressBar6.setStringPainted(true);
            jProgressBar7.setStringPainted(true);
            jProgressBar8.setStringPainted(true);
            jLabel28.setText("Proporsi produksi selesai: " + total + " kemasan");
            jLabel16.setText("Pencatatan Produksi Hari Ini · " + harian.getRowCount() + " transaksi");
        } catch (RuntimeException ex) { ViewHelper.error(this, ex); }
    }

    private model.type.JenisProduk jenisDariNama(String nama) {
        for (model.type.JenisProduk jenis : model.type.JenisProduk.values()) {
            if (jenis.getNamaProduk().equalsIgnoreCase(nama)) return jenis;
        }
        throw new IllegalArgumentException("Jenis produk produksi tidak dikenali: " + nama);
    }

@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jLabel29 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jLabel54 = new javax.swing.JLabel();
        jSeparator10 = new javax.swing.JSeparator();
        jLabel27 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jPanel12 = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        jProgressBar5 = new javax.swing.JProgressBar();
        jProgressBar6 = new javax.swing.JProgressBar();
        jProgressBar7 = new javax.swing.JProgressBar();
        jProgressBar8 = new javax.swing.JProgressBar();
        jLabel17 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(0, 105, 148));
        jPanel1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(0, 105, 148));
        jButton1.setText("Dashboard");
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, 122, 33));

        jButton5.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jButton5.setForeground(new java.awt.Color(0, 105, 148));
        jButton5.setText("Kelola Produksi");
        jPanel1.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 330, 122, 30));

        jButton8.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jButton8.setForeground(new java.awt.Color(0, 105, 148));
        jButton8.setText("Laporan");
        jButton8.addActionListener(this::jButton8ActionPerformed);
        jPanel1.add(jButton8, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 370, 122, 37));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Spring Logo with Swirl Emblem (2).png"))); // NOI18N
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 30, -1, -1));

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setFont(new java.awt.Font("Arial Black", 1, 13)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Operasional");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 70, 90, 20));

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

        jPanel11.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel16.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel16.setText("Pencatatan Produksi Hari Ini");
        jPanel11.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 6, 223, -1));

        jTable2.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Produk", "Hasil", "Dicatat Pukul", "Status"
            }
        ));
        jTable2.setAutoscrolls(false);
        jTable2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jScrollPane2.setViewportView(jTable2);

        jPanel11.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 49, 742, 205));

        jLabel29.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(128, 128, 128));
        jLabel29.setText("Produksi: 4 November 2026");
        jPanel11.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 29, 272, -1));

        jPanel2.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 280, 770, 270));

        jLabel3.setBackground(new java.awt.Color(255, 255, 255));
        jLabel3.setFont(new java.awt.Font("Bahnschrift", 1, 36)); // NOI18N
        jLabel3.setText("Dashboard Produksi");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 409, 42));

        jLabel53.setText("4 November 2026");
        jPanel2.add(jLabel53, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 30, 110, -1));

        jLabel55.setText("Admin");
        jPanel2.add(jLabel55, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 20, 79, -1));

        jButton2.setBackground(new java.awt.Color(255, 0, 51));
        jButton2.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jButton2.setForeground(new java.awt.Color(252, 252, 252));
        jButton2.setText("LogOut");
        jButton2.addActionListener(this::jButton2ActionPerformed);
        jPanel2.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 30, -1, -1));

        jLabel54.setText("Aulia Ashylla");
        jPanel2.add(jLabel54, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 40, 79, 10));

        jSeparator10.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel2.add(jSeparator10, new org.netbeans.lib.awtextra.AbsoluteConstraints(873, 20, -1, 33));

        jLabel27.setBackground(new java.awt.Color(255, 255, 255));
        jLabel27.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        jLabel27.setText("Selamat siang, Budi. Berikut hasil produksi yang Anda catat hari ini.");
        jPanel2.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 120, 580, -1));

        jTable3.setFont(new java.awt.Font("Arial Black", 0, 14)); // NOI18N
        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Spring Botol 600 ml", "Spring Borol 1500 ml", "Galon 19 L", "Galon 5 L", "Total Produksi"
            }
        ));
        jScrollPane3.setViewportView(jTable3);

        jPanel2.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 150, 1040, 110));

        jLabel28.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel28.setText("Proporsi Produksi Selesai");

        jLabel17.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(128, 128, 128));
        jLabel17.setText("Galon 19 L");

        jLabel30.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(128, 128, 128));
        jLabel30.setText("Galon 5 L");

        jLabel31.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(128, 128, 128));
        jLabel31.setText("Botol 1500 Ml");

        jLabel32.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(128, 128, 128));
        jLabel32.setText("Botol 600 Ml");

        jLabel33.setFont(new java.awt.Font("Arial", 1, 10)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(128, 128, 128));
        jLabel33.setText("80%");

        jLabel34.setFont(new java.awt.Font("Arial", 1, 10)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(128, 128, 128));
        jLabel34.setText("80%");

        jLabel35.setFont(new java.awt.Font("Arial", 1, 10)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(128, 128, 128));
        jLabel35.setText("80%");

        jLabel36.setFont(new java.awt.Font("Arial", 1, 10)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(128, 128, 128));
        jLabel36.setText("80%");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel28, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jProgressBar5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jProgressBar6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jProgressBar7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jProgressBar8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel12Layout.createSequentialGroup()
                                .addComponent(jLabel17)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel36)
                                .addGap(9, 9, 9))
                            .addGroup(jPanel12Layout.createSequentialGroup()
                                .addComponent(jLabel32)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel33)
                                .addGap(9, 9, 9))
                            .addGroup(jPanel12Layout.createSequentialGroup()
                                .addComponent(jLabel31)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel34)
                                .addGap(10, 10, 10))
                            .addGroup(jPanel12Layout.createSequentialGroup()
                                .addComponent(jLabel30)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel35)
                                .addGap(10, 10, 10)))))
                .addContainerGap(37, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(jLabel28)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32)
                    .addComponent(jLabel33))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jProgressBar5, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel31)
                    .addComponent(jLabel34))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jProgressBar6, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(jLabel35))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jProgressBar7, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(jLabel36))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jProgressBar8, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(35, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(802, 280, 260, 270));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 0, 1100, 720));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        prosesLogout();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton8ActionPerformed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton8;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JProgressBar jProgressBar5;
    private javax.swing.JProgressBar jProgressBar6;
    private javax.swing.JProgressBar jProgressBar7;
    private javax.swing.JProgressBar jProgressBar8;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    // End of variables declaration//GEN-END:variables
}
