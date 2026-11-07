/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

/**
 *
 * @author Dovs
 */
public class AdminDistribusiForm extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger =
            java.util.logging.Logger.getLogger(AdminDistribusiForm.class.getName());
    private controller.SistemController sistemController;

    public AdminDistribusiForm() {
        initComponents();
    }

    public AdminDistribusiForm(controller.SistemController sistemController) {
        this();
        setSistemController(sistemController);
    }

    public void setSistemController(controller.SistemController sistemController) {
        if (sistemController == null || sistemController.getAkunController() == null
                || sistemController.getAkunController().getPetugasAktif() == null) {
            throw new IllegalStateException("Sesi pengguna belum tersedia.");
        }
        this.sistemController = sistemController;
        ViewHelper.buka(this, sistemController, "DISTRIBUSI");
    }

@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel24 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel25 = new javax.swing.JLabel();
        jButton7 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jLabel26 = new javax.swing.JLabel();
        jButton12 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
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
        jPanel8 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jComboBox6 = new javax.swing.JComboBox<>();
        jLabel18 = new javax.swing.JLabel();
        jComboBox7 = new javax.swing.JComboBox<>();
        jLabel28 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jButton13 = new javax.swing.JButton();
        jLabel29 = new javax.swing.JLabel();
        jButton14 = new javax.swing.JButton();
        jLabel41 = new javax.swing.JLabel();
        jComboBox12 = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        jSeparator10 = new javax.swing.JSeparator();
        jButton2 = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jLabel30 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        jComboBox8 = new javax.swing.JComboBox<>();
        jLabel33 = new javax.swing.JLabel();
        jComboBox9 = new javax.swing.JComboBox<>();
        jLabel34 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jButton15 = new javax.swing.JButton();
        jLabel35 = new javax.swing.JLabel();
        jButton16 = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jLabel36 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();
        jLabel37 = new javax.swing.JLabel();
        jComboBox10 = new javax.swing.JComboBox<>();
        jLabel38 = new javax.swing.JLabel();
        jComboBox11 = new javax.swing.JComboBox<>();
        jLabel39 = new javax.swing.JLabel();
        jTextField10 = new javax.swing.JTextField();
        jButton17 = new javax.swing.JButton();
        jLabel40 = new javax.swing.JLabel();
        jButton18 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(0, 105, 148));
        jPanel1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Spring Logo with Swirl Emblem (2).png"))); // NOI18N
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 30, -1, -1));

        jLabel23.setBackground(new java.awt.Color(255, 255, 255));
        jLabel23.setFont(new java.awt.Font("Arial Black", 1, 13)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setText("Operasional");
        jPanel1.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 70, 90, 20));

        jButton1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(0, 105, 148));
        jButton1.setText("Dashboard");
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, 122, 33));

        jLabel24.setFont(new java.awt.Font("Arial Black", 1, 11)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(255, 255, 255));
        jLabel24.setText("Data");
        jPanel1.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 190, 30, 20));

        jButton3.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jButton3.setForeground(new java.awt.Color(0, 105, 148));
        jButton3.setText("Data Produk");
        jPanel1.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 210, 122, 30));

        jButton4.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jButton4.setForeground(new java.awt.Color(0, 105, 148));
        jButton4.setText("Data Agen");
        jPanel1.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 250, 122, 30));

        jLabel25.setFont(new java.awt.Font("Arial Black", 1, 11)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(255, 255, 255));
        jLabel25.setText("Operasional");
        jPanel1.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 310, 90, 20));

        jButton7.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jButton7.setForeground(new java.awt.Color(0, 105, 148));
        jButton7.setText("Kelola Produksi");
        jPanel1.add(jButton7, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 330, 122, 30));

        jButton10.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jButton10.setForeground(new java.awt.Color(0, 105, 148));
        jButton10.setText("Gudang");
        jPanel1.add(jButton10, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 370, 122, 30));

        jButton11.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jButton11.setForeground(new java.awt.Color(0, 105, 148));
        jButton11.setText("Kembali");
        jPanel1.add(jButton11, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 410, 122, 30));

        jLabel26.setFont(new java.awt.Font("Arial Black", 1, 11)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(255, 255, 255));
        jLabel26.setText("Administrasi");
        jPanel1.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 480, 90, 20));

        jButton12.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jButton12.setForeground(new java.awt.Color(0, 105, 148));
        jButton12.setText("Akun Pengguna");
        jButton12.addActionListener(this::jButton12ActionPerformed);
        jPanel1.add(jButton12, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 500, 122, 30));

        jButton8.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jButton8.setForeground(new java.awt.Color(0, 105, 148));
        jButton8.setText("Laporan");
        jButton8.addActionListener(this::jButton8ActionPerformed);
        jPanel1.add(jButton8, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 580, 122, 37));

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
        jButton5.setText("Refresh");
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
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1010, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton5)))
                .addContainerGap(14, Short.MAX_VALUE))
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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 150, 1040, 290));

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
        jLabel4.setText("Ubah Distribusi");
        jPanel6.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, 20));

        jTextField1.setText("yyyy-mm-dd");
        jTextField1.addActionListener(this::jTextField1ActionPerformed);
        jPanel6.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 150, 30));

        jLabel6.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel6.setText("Agen (wilayah sama)");
        jPanel6.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 60, -1, -1));

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Terkirim", " " }));
        jPanel6.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 80, 130, 30));

        jLabel7.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel7.setText("Jenis Produk");
        jPanel6.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, -1, -1));

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Spring Botol 600ml", "Spring Botol 1500ml", "Spring Galon 19L", "Spring Galon 5L" }));
        jComboBox2.addActionListener(this::jComboBox2ActionPerformed);
        jPanel6.add(jComboBox2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, 180, 30));

        jLabel8.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel8.setText("Jumlah Pesan");
        jPanel6.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 54, -1, 20));

        jTextField2.setText("Jumlah Pesan");
        jTextField2.addActionListener(this::jTextField2ActionPerformed);
        jPanel6.add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 140, 170, 30));

        jButton6.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        jButton6.setText("Batal");
        jButton6.addActionListener(this::jButton6ActionPerformed);
        jPanel6.add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 150, 100, 30));

        jLabel17.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel17.setText("Waktu");
        jPanel6.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 130, -1));

        jButton9.setBackground(new java.awt.Color(0, 105, 148));
        jButton9.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        jButton9.setForeground(new java.awt.Color(255, 255, 255));
        jButton9.setText("Simpan");
        jButton9.addActionListener(this::jButton9ActionPerformed);
        jPanel6.add(jButton9, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 190, 160, 30));

        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setBackground(new java.awt.Color(255, 255, 255));
        jLabel5.setFont(new java.awt.Font("Arial Black", 0, 16)); // NOI18N
        jLabel5.setText("Buat Distribusi");
        jPanel8.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, 20));

        jTextField4.setText("yyyy-mm-dd");
        jTextField4.addActionListener(this::jTextField4ActionPerformed);
        jPanel8.add(jTextField4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 150, 30));

        jLabel12.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel12.setText("Agen * (wilayah sama)");
        jPanel8.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 60, -1, -1));

        jComboBox6.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Toko Segar Jaya", "Depot Air Minum Barokah", "Toko Sembako Berkah ", "Minimarket Sentrosa", "Toko Tirta Abadi ", "Depot Sumber Rejeki", "Toko Maju Bersama ", "Minimarket Melati ", "Toko Kelontong Utama", "Depot Minum Sehat", "Toko Kasih Ibu", "Minimarket Berkat Jaya", "Depot Tirta Bening ", "Toko Makmur Sejahtera", "Toko Baru Palaran" }));
        jPanel8.add(jComboBox6, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 80, 170, 30));

        jLabel18.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel18.setText("Jenis Produk *");
        jPanel8.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, -1, -1));

        jComboBox7.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Spring Botol 600ml", "Spring Botol 1500ml", "Spring Galon 19L", "Spring Galon 5L" }));
        jComboBox7.addActionListener(this::jComboBox7ActionPerformed);
        jPanel8.add(jComboBox7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, 180, 30));

        jLabel28.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel28.setText("Jumlah Pesan *");
        jPanel8.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 120, -1, -1));

        jTextField6.setText("Jumlah Pesan");
        jTextField6.addActionListener(this::jTextField6ActionPerformed);
        jPanel8.add(jTextField6, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 140, 170, 30));

        jButton13.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        jButton13.setText("Batal");
        jButton13.addActionListener(this::jButton13ActionPerformed);
        jPanel8.add(jButton13, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 190, 100, 30));

        jLabel29.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel29.setText("Tanggal Pengiriman *");
        jPanel8.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 130, -1));

        jButton14.setBackground(new java.awt.Color(0, 105, 148));
        jButton14.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        jButton14.setForeground(new java.awt.Color(255, 255, 255));
        jButton14.setText("Simpan Distribusi");
        jButton14.addActionListener(this::jButton14ActionPerformed);
        jPanel8.add(jButton14, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 190, 160, 30));

        jPanel6.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 450, 420, 240));

        jLabel41.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel41.setText("Jumlah Pesan");
        jPanel6.add(jLabel41, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 120, -1, -1));

        jComboBox12.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Toko Segar Jaya", "Depot Air Minum Barokah", "Toko Sembako Berkah ", "Minimarket Sentrosa", "Toko Tirta Abadi ", "Depot Sumber Rejeki", "Toko Maju Bersama ", "Minimarket Melati ", "Toko Kelontong Utama", "Depot Minum Sehat", "Toko Kasih Ibu", "Minimarket Berkat Jaya", "Depot Tirta Bening ", "Toko Makmur Sejahtera", "Toko Baru Palaran" }));
        jPanel6.add(jComboBox12, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 80, 170, 30));

        jPanel2.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 450, 590, 240));

        jLabel3.setBackground(new java.awt.Color(255, 255, 255));
        jLabel3.setFont(new java.awt.Font("Bahnschrift", 1, 36)); // NOI18N
        jLabel3.setText("Distribusi");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 409, 42));

        jLabel27.setBackground(new java.awt.Color(255, 255, 255));
        jLabel27.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        jLabel27.setText("Ganti ganti kata kata ini");
        jPanel2.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 120, 484, -1));

        jLabel53.setText("4 November 2026");
        jPanel2.add(jLabel53, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 30, 110, -1));

        jLabel55.setText("Admin");
        jPanel2.add(jLabel55, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 20, 79, -1));

        jLabel54.setText("Aulia Ashylla");
        jPanel2.add(jLabel54, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 40, 79, 10));

        jSeparator10.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel2.add(jSeparator10, new org.netbeans.lib.awtextra.AbsoluteConstraints(873, 20, -1, 33));

        jButton2.setBackground(new java.awt.Color(255, 0, 51));
        jButton2.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jButton2.setForeground(new java.awt.Color(252, 252, 252));
        jButton2.setText("LogOut");
        jButton2.addActionListener(this::jButton2ActionPerformed);
        jPanel2.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 30, -1, -1));

        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel30.setBackground(new java.awt.Color(255, 255, 255));
        jLabel30.setFont(new java.awt.Font("Arial Black", 0, 16)); // NOI18N
        jLabel30.setText("Buat Distribusi");
        jPanel9.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, 20));

        jTextField7.setText("yyyy-mm-dd");
        jTextField7.addActionListener(this::jTextField7ActionPerformed);
        jPanel9.add(jTextField7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 150, 30));

        jLabel32.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel32.setText("Agen * (wilayah sama)");
        jPanel9.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 60, -1, -1));

        jComboBox8.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Toko Segar Jaya", "Depot Air Minum Barokah", "Toko Sembako Berkah ", "Minimarket Sentrosa", "Toko Tirta Abadi ", "Depot Sumber Rejeki", "Toko Maju Bersama ", "Minimarket Melati ", "Toko Kelontong Utama", "Depot Minum Sehat", "Toko Kasih Ibu", "Minimarket Berkat Jaya", "Depot Tirta Bening ", "Toko Makmur Sejahtera", "Toko Baru Palaran" }));
        jPanel9.add(jComboBox8, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 80, 170, 30));

        jLabel33.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel33.setText("Jenis Produk *");
        jPanel9.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, -1, -1));

        jComboBox9.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Spring Botol 600ml", "Spring Botol 1500ml", "Spring Galon 19L", "Spring Galon 5L" }));
        jComboBox9.addActionListener(this::jComboBox9ActionPerformed);
        jPanel9.add(jComboBox9, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, 180, 30));

        jLabel34.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel34.setText("Jumlah Pesan *");
        jPanel9.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 120, -1, -1));

        jTextField8.setText("Jumlah Pesan");
        jTextField8.addActionListener(this::jTextField8ActionPerformed);
        jPanel9.add(jTextField8, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 140, 170, 30));

        jButton15.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        jButton15.setText("Batal");
        jButton15.addActionListener(this::jButton15ActionPerformed);
        jPanel9.add(jButton15, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 190, 100, 30));

        jLabel35.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel35.setText("Waktu *");
        jPanel9.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 130, -1));

        jButton16.setBackground(new java.awt.Color(0, 105, 148));
        jButton16.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        jButton16.setForeground(new java.awt.Color(255, 255, 255));
        jButton16.setText("Simpan Distribusi");
        jButton16.addActionListener(this::jButton16ActionPerformed);
        jPanel9.add(jButton16, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 190, 160, 30));

        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel36.setBackground(new java.awt.Color(255, 255, 255));
        jLabel36.setFont(new java.awt.Font("Arial Black", 0, 16)); // NOI18N
        jLabel36.setText("Buat Distribusi");
        jPanel10.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, 20));

        jTextField9.setText("yyyy-mm-dd");
        jTextField9.addActionListener(this::jTextField9ActionPerformed);
        jPanel10.add(jTextField9, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 150, 30));

        jLabel37.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel37.setText("Agen * (wilayah sama)");
        jPanel10.add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 60, -1, -1));

        jComboBox10.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Toko Segar Jaya", "Depot Air Minum Barokah", "Toko Sembako Berkah ", "Minimarket Sentrosa", "Toko Tirta Abadi ", "Depot Sumber Rejeki", "Toko Maju Bersama ", "Minimarket Melati ", "Toko Kelontong Utama", "Depot Minum Sehat", "Toko Kasih Ibu", "Minimarket Berkat Jaya", "Depot Tirta Bening ", "Toko Makmur Sejahtera", "Toko Baru Palaran" }));
        jPanel10.add(jComboBox10, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 80, 170, 30));

        jLabel38.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel38.setText("Jenis Produk *");
        jPanel10.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, -1, -1));

        jComboBox11.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Spring Botol 600ml", "Spring Botol 1500ml", "Spring Galon 19L", "Spring Galon 5L" }));
        jComboBox11.addActionListener(this::jComboBox11ActionPerformed);
        jPanel10.add(jComboBox11, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, 180, 30));

        jLabel39.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel39.setText("Jumlah Pesan *");
        jPanel10.add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 120, -1, -1));

        jTextField10.setText("Jumlah Pesan");
        jTextField10.addActionListener(this::jTextField10ActionPerformed);
        jPanel10.add(jTextField10, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 140, 170, 30));

        jButton17.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        jButton17.setText("Batal");
        jButton17.addActionListener(this::jButton17ActionPerformed);
        jPanel10.add(jButton17, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 190, 100, 30));

        jLabel40.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel40.setText("Tanggal Pengiriman *");
        jPanel10.add(jLabel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 130, -1));

        jButton18.setBackground(new java.awt.Color(0, 105, 148));
        jButton18.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        jButton18.setForeground(new java.awt.Color(255, 255, 255));
        jButton18.setText("Simpan Distribusi");
        jButton18.addActionListener(this::jButton18ActionPerformed);
        jPanel10.add(jButton18, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 190, 160, 30));

        jPanel9.add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 450, 420, 240));

        jPanel2.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 450, 430, 240));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 0, 1100, 720));

        pack();
    }

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) { /* Listener diganti oleh penghubung view saat sesi aktif. */ }

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) { /* Listener diganti oleh penghubung view saat sesi aktif. */ }

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) { /* Listener diganti oleh penghubung view saat sesi aktif. */ }

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) { /* Listener diganti oleh penghubung view saat sesi aktif. */ }

    private void jTextField5ActionPerformed(java.awt.event.ActionEvent evt) { /* Listener diganti oleh penghubung view saat sesi aktif. */ }

    private void jComboBox5ActionPerformed(java.awt.event.ActionEvent evt) { /* Listener diganti oleh penghubung view saat sesi aktif. */ }

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) { /* Listener diganti oleh penghubung view saat sesi aktif. */ }

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) { /* Listener diganti oleh penghubung view saat sesi aktif. */ }

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) { /* Listener diganti oleh penghubung view saat sesi aktif. */ }

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) { /* Listener diganti oleh penghubung view saat sesi aktif. */ }

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) { /* Listener diganti oleh penghubung view saat sesi aktif. */ }

    private void jTextField4ActionPerformed(java.awt.event.ActionEvent evt) { /* Listener diganti oleh penghubung view saat sesi aktif. */ }

    private void jComboBox7ActionPerformed(java.awt.event.ActionEvent evt) { /* Listener diganti oleh penghubung view saat sesi aktif. */ }

    private void jTextField6ActionPerformed(java.awt.event.ActionEvent evt) { /* Listener diganti oleh penghubung view saat sesi aktif. */ }

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) { /* Listener diganti oleh penghubung view saat sesi aktif. */ }

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) { /* Listener diganti oleh penghubung view saat sesi aktif. */ }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) { /* Listener diganti oleh penghubung view saat sesi aktif. */ }

    private void jTextField7ActionPerformed(java.awt.event.ActionEvent evt) { /* Listener diganti oleh penghubung view saat sesi aktif. */ }

    private void jComboBox9ActionPerformed(java.awt.event.ActionEvent evt) { /* Listener diganti oleh penghubung view saat sesi aktif. */ }

    private void jTextField8ActionPerformed(java.awt.event.ActionEvent evt) { /* Listener diganti oleh penghubung view saat sesi aktif. */ }

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) { /* Listener diganti oleh penghubung view saat sesi aktif. */ }

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) { /* Listener diganti oleh penghubung view saat sesi aktif. */ }

    private void jTextField9ActionPerformed(java.awt.event.ActionEvent evt) { /* Listener diganti oleh penghubung view saat sesi aktif. */ }

    private void jComboBox11ActionPerformed(java.awt.event.ActionEvent evt) { /* Listener diganti oleh penghubung view saat sesi aktif. */ }

    private void jTextField10ActionPerformed(java.awt.event.ActionEvent evt) { /* Listener diganti oleh penghubung view saat sesi aktif. */ }

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) { /* Listener diganti oleh penghubung view saat sesi aktif. */ }

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) { /* Listener diganti oleh penghubung view saat sesi aktif. */ }

// Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox10;
    private javax.swing.JComboBox<String> jComboBox11;
    private javax.swing.JComboBox<String> jComboBox12;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JComboBox<String> jComboBox5;
    private javax.swing.JComboBox<String> jComboBox6;
    private javax.swing.JComboBox<String> jComboBox7;
    private javax.swing.JComboBox<String> jComboBox8;
    private javax.swing.JComboBox<String> jComboBox9;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
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
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    // End of variables declaration//GEN-END:variables
}
