/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

/**
 *
 * @author Dovs
 */
public class AdminTambahAgenForm extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger =
            java.util.logging.Logger.getLogger(AdminTambahAgenForm.class.getName());
    private controller.SistemController sistemController;

    public AdminTambahAgenForm() {
        initComponents();
    }

    public AdminTambahAgenForm(controller.SistemController sistemController) {
        this();
        setSistemController(sistemController);
    }

    public void setSistemController(controller.SistemController sistemController) {
        if (sistemController == null || sistemController.getAkunController() == null
                || sistemController.getAkunController().getPetugasAktif() == null) {
            throw new IllegalStateException("Sesi pengguna belum tersedia.");
        }
        this.sistemController = sistemController;
        ViewHelper.buka(this, sistemController, "TAMBAH_AGEN");
    }

@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jButton9 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jTextField13 = new javax.swing.JTextField();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jLabel33 = new javax.swing.JLabel();
        jTextField15 = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jTextField16 = new javax.swing.JTextField();
        jTextField14 = new javax.swing.JTextField();
        jTextField17 = new javax.swing.JTextField();
        jTextField18 = new javax.swing.JTextField();
        jLabel53 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        jButton13 = new javax.swing.JButton();
        jSeparator10 = new javax.swing.JSeparator();
        jLabel6 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jTextField3 = new javax.swing.JTextField();
        jButton14 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(0, 105, 148));
        jPanel1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Spring Logo with Swirl Emblem (2).png"))); // NOI18N
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 30, -1, -1));

        jLabel4.setBackground(new java.awt.Color(255, 255, 255));
        jLabel4.setFont(new java.awt.Font("Arial Black", 1, 13)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Operasional");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 70, 90, 20));

        jButton2.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jButton2.setForeground(new java.awt.Color(0, 105, 148));
        jButton2.setText("Dashboard");
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, 122, 33));

        jLabel8.setFont(new java.awt.Font("Arial Black", 1, 11)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Data");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 190, 30, 20));

        jButton3.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jButton3.setForeground(new java.awt.Color(0, 105, 148));
        jButton3.setText("Data Produk");
        jPanel1.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 210, 122, 30));

        jButton4.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jButton4.setForeground(new java.awt.Color(0, 105, 148));
        jButton4.setText("Data Agen");
        jPanel1.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 250, 122, 30));

        jLabel9.setFont(new java.awt.Font("Arial Black", 1, 11)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Operasional");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 310, 90, 20));

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

        jLabel7.setFont(new java.awt.Font("Arial Black", 1, 11)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Administrasi");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 480, 90, 20));

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

        jLabel11.setBackground(new java.awt.Color(255, 255, 255));
        jLabel11.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Spring Operasional");
        jPanel1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 660, -1, 20));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 180, 720));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel11.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel16.setFont(new java.awt.Font("Arial Black", 0, 18)); // NOI18N
        jLabel16.setText("Informasi Agen");
        jPanel11.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(27, 18, 200, 20));

        jLabel17.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(128, 128, 128));
        jLabel17.setText("Lengkapi kolom bertanda * sebelum menyimpan.");
        jPanel11.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(27, 44, 272, -1));

        jLabel29.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel29.setText("Nama Agen *");
        jPanel11.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(27, 88, -1, 14));

        jLabel31.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel31.setText("No Telepon *");
        jPanel11.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(308, 88, 143, 14));

        jLabel32.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel32.setText("Alamat Lengkap *");
        jPanel11.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(586, 88, 152, 14));

        jTextField13.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jTextField13.setText("Masukkan alamat lengkap");
        jTextField13.addActionListener(this::jTextField13ActionPerformed);
        jPanel11.add(jTextField13, new org.netbeans.lib.awtextra.AbsoluteConstraints(586, 108, 297, 36));

        jButton11.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jButton11.setText("Batal");
        jButton11.addActionListener(this::jButton11ActionPerformed);
        jPanel11.add(jButton11, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 150, 105, -1));

        jButton12.setBackground(new java.awt.Color(0, 105, 148));
        jButton12.setFont(new java.awt.Font("Arial Black", 1, 14)); // NOI18N
        jButton12.setForeground(new java.awt.Color(255, 255, 255));
        jButton12.setText("Simpan");
        jButton12.addActionListener(this::jButton12ActionPerformed);
        jPanel11.add(jButton12, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 180, 146, -1));

        jLabel33.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel33.setText("Kecamatan Terdeteksi");
        jPanel11.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(27, 156, -1, 14));

        jTextField15.setEditable(false);
        jTextField15.setText("Validasi otomatis");
        jTextField15.addActionListener(this::jTextField15ActionPerformed);
        jPanel11.add(jTextField15, new org.netbeans.lib.awtextra.AbsoluteConstraints(586, 176, 149, 36));

        jLabel34.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel34.setText("Status Wilayah");
        jPanel11.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(586, 156, 152, 14));

        jLabel42.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel42.setText("Petugas Distribusi");
        jPanel11.add(jLabel42, new org.netbeans.lib.awtextra.AbsoluteConstraints(308, 156, 177, 14));

        jTextField16.setEditable(false);
        jTextField16.setText("Ditentukan sistem");
        jTextField16.addActionListener(this::jTextField16ActionPerformed);
        jPanel11.add(jTextField16, new org.netbeans.lib.awtextra.AbsoluteConstraints(308, 176, 229, 36));

        jTextField14.setEditable(false);
        jTextField14.setText("Dari alamat agen");
        jTextField14.addActionListener(this::jTextField14ActionPerformed);
        jPanel11.add(jTextField14, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 600, 306, 36));

        jTextField17.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jTextField17.setText("No Telp");
        jTextField17.addActionListener(this::jTextField17ActionPerformed);
        jPanel11.add(jTextField17, new org.netbeans.lib.awtextra.AbsoluteConstraints(308, 108, 229, 36));

        jTextField18.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jTextField18.setText("Nama Agen");
        jTextField18.addActionListener(this::jTextField18ActionPerformed);
        jPanel11.add(jTextField18, new org.netbeans.lib.awtextra.AbsoluteConstraints(27, 108, 229, 36));

        jPanel2.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 154, 1040, 240));

        jLabel53.setText("4 November 2026");
        jPanel2.add(jLabel53, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 30, 110, -1));

        jLabel55.setText("Admin");
        jPanel2.add(jLabel55, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 20, 79, -1));

        jLabel54.setText("Aulia Ashylla");
        jPanel2.add(jLabel54, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 40, 79, 10));

        jButton13.setBackground(new java.awt.Color(255, 0, 51));
        jButton13.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jButton13.setForeground(new java.awt.Color(252, 252, 252));
        jButton13.setText("LogOut");
        jButton13.addActionListener(this::jButton13ActionPerformed);
        jPanel2.add(jButton13, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 30, -1, -1));

        jSeparator10.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel2.add(jSeparator10, new org.netbeans.lib.awtextra.AbsoluteConstraints(873, 20, -1, 33));

        jLabel6.setBackground(new java.awt.Color(255, 255, 255));
        jLabel6.setFont(new java.awt.Font("Bahnschrift", 1, 36)); // NOI18N
        jLabel6.setText("Tambah Agen");
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 280, 42));

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 80, 603, -1));

        jLabel10.setBackground(new java.awt.Color(255, 255, 255));
        jLabel10.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        jLabel10.setText("Catat identitas agen dan alamat lengkap untuk pencocokan wilayah distribusi.");
        jPanel2.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 120, 484, -1));

        jLabel18.setFont(new java.awt.Font("Arial Black", 0, 16)); // NOI18N
        jLabel18.setText("Daftar Agen");

        jComboBox1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Semua Wilayah", "Palaran", "Samarinda Ilir", "Samarinda Kota", "Sungai Kunjang", " " }));
        jComboBox1.addActionListener(this::jComboBox1ActionPerformed);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
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
                "Id Agen", "Nama Agen", "No Telpon", "Alamat Lengkap"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jTextField3.setText("Cara Id Agen atau Nama Agen");
        jTextField3.addActionListener(this::jTextField3ActionPerformed);

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 861, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel12Layout.createSequentialGroup()
                                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 334, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(40, 40, 40))))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 410, 1040, 270));

        jButton14.setBackground(new java.awt.Color(0, 105, 148));
        jButton14.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jButton14.setForeground(new java.awt.Color(255, 255, 255));
        jButton14.setText("Kembali");
        jButton14.addActionListener(this::jButton14ActionPerformed);
        jPanel2.add(jButton14, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 80, 150, 40));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 0, 1100, 720));

        pack();
    }

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) { /* Listener diganti oleh penghubung view saat sesi aktif. */ }

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) { /* Listener diganti oleh penghubung view saat sesi aktif. */ }

    private void jTextField13ActionPerformed(java.awt.event.ActionEvent evt) { /* Listener diganti oleh penghubung view saat sesi aktif. */ }

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) { /* Listener diganti oleh penghubung view saat sesi aktif. */ }

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) { /* Listener diganti oleh penghubung view saat sesi aktif. */ }

    private void jTextField15ActionPerformed(java.awt.event.ActionEvent evt) { /* Listener diganti oleh penghubung view saat sesi aktif. */ }

    private void jTextField16ActionPerformed(java.awt.event.ActionEvent evt) { /* Listener diganti oleh penghubung view saat sesi aktif. */ }

    private void jTextField14ActionPerformed(java.awt.event.ActionEvent evt) { /* Listener diganti oleh penghubung view saat sesi aktif. */ }

    private void jTextField17ActionPerformed(java.awt.event.ActionEvent evt) { /* Listener diganti oleh penghubung view saat sesi aktif. */ }

    private void jTextField18ActionPerformed(java.awt.event.ActionEvent evt) { /* Listener diganti oleh penghubung view saat sesi aktif. */ }

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) { /* Listener diganti oleh penghubung view saat sesi aktif. */ }

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) { /* Listener diganti oleh penghubung view saat sesi aktif. */ }

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) { /* Listener diganti oleh penghubung view saat sesi aktif. */ }

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) { /* Listener diganti oleh penghubung view saat sesi aktif. */ }

// Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel42;
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
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField13;
    private javax.swing.JTextField jTextField14;
    private javax.swing.JTextField jTextField15;
    private javax.swing.JTextField jTextField16;
    private javax.swing.JTextField jTextField17;
    private javax.swing.JTextField jTextField18;
    private javax.swing.JTextField jTextField3;
    // End of variables declaration//GEN-END:variables
}
