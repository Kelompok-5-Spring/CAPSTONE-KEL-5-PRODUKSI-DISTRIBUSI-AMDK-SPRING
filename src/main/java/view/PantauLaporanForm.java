/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

/**
 *
 * @author Dovs
 */
public class PantauLaporanForm extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger =
            java.util.logging.Logger.getLogger(PantauLaporanForm.class.getName());
    private controller.SistemController sistemController;

    public PantauLaporanForm() {
        initComponents();
    }

    public PantauLaporanForm(controller.SistemController sistemController) {
        this();
        setSistemController(sistemController);
    }

    public void setSistemController(controller.SistemController sistemController) {
        if (sistemController == null || sistemController.getAkunController() == null
                || sistemController.getAkunController().getPetugasAktif() == null) {
            throw new IllegalStateException("Sesi pengguna belum tersedia.");
        }
        this.sistemController = sistemController;
        ViewHelper.setHeader(jLabel53, jLabel54, jLabel55, sistemController);
        ViewHelper.pasangSidebarStandar(jPanel1, this, sistemController);
        jTextField1.setText(java.time.LocalDate.now().toString());
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(
                new String[]{"Minggu", "Bulan", "Tahun"}));
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(
                new String[]{"Gabungan", "Produksi", "Distribusi"}));
        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{
            "Semua Produk", "Spring Botol 600ml", "Spring Botol 1500ml",
            "Spring Galon 5L", "Spring Galon 19L"}));
        jComboBox3.setEnabled(true);
        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{
            "Semua Status", "Dalam Pengiriman", "Terkirim", "Tidak Terkirim"}));
        siapkanFilterLaporan();

        ViewHelper.pasangTombol(jButton1, e -> ViewHelper.bukaDashboard(this, sistemController));
        ViewHelper.pasangTombol(jButton8, e -> tampilkanTabelLaporan());
        ViewHelper.pasangTombol(jButton10, e -> ViewHelper.logout(this, sistemController));
        ViewHelper.pasangTombol(jButton2, e -> tampilkanLaporan());
        jTextField1.addActionListener(e -> tampilkanTabelLaporan());
        jComboBox1.addActionListener(e -> tampilkanTabelLaporan());
        jComboBox2.addActionListener(e -> tampilkanTabelLaporan());
        jComboBox3.addActionListener(e -> tampilkanTabelLaporan());
        jComboBox4.addActionListener(e -> tampilkanTabelLaporan());
        tampilkanTabelLaporan();
    }

    private void prosesLogout() {
        ViewHelper.logout(this, sistemController);
    }

    private java.time.LocalDate[] rentangLaporan() {
        java.time.LocalDate acuan = ViewHelper.tanggal(jTextField1.getText());
        String periode = String.valueOf(jComboBox1.getSelectedItem()).toLowerCase(java.util.Locale.ROOT);
        java.time.LocalDate awal;
        java.time.LocalDate akhir;
        if (periode.equals("minggu")) {
            awal = acuan.minusDays(acuan.getDayOfWeek().getValue() - 1);
            akhir = awal.plusDays(6);
        } else if (periode.equals("bulan")) {
            awal = acuan.withDayOfMonth(1);
            akhir = acuan.withDayOfMonth(acuan.lengthOfMonth());
        } else {
            awal = acuan.withDayOfYear(1);
            akhir = acuan.withDayOfYear(acuan.lengthOfYear());
        }
        return new java.time.LocalDate[]{awal, akhir};
    }

    private void tampilkanLaporan() {
        // Hasil laporan ditampilkan langsung pada tabel, tanpa pop-up yang menutupi data.
        tampilkanTabelLaporan();
    }

    private void tampilkanTabelLaporan() {
        if (sistemController == null) return;
        try {
            java.time.LocalDate[] range = rentangLaporan();
            model.type.Role role = sistemController.getAkunController().getPetugasAktif().getRole();
            String jenisLaporan = String.valueOf(jComboBox2.getSelectedItem());
            String produkFilter = String.valueOf(jComboBox3.getSelectedItem());
            String statusFilter = String.valueOf(jComboBox4.getSelectedItem());

            java.util.List<model.Produksi> produksi = new java.util.ArrayList<>();
            if (role != model.type.Role.Distribusi && !"Distribusi".equalsIgnoreCase(jenisLaporan)) {
                produksi = sistemController.getProduksiController().getProduksiByTanggal(range[0], range[1]);
            }

            java.util.Map<model.type.JenisProduk, Integer> hasilProduksi =
                    new java.util.EnumMap<>(model.type.JenisProduk.class);
            java.util.Map<model.type.JenisProduk, Integer> barangKeluar =
                    new java.util.EnumMap<>(model.type.JenisProduk.class);
            for (model.type.JenisProduk jenis : model.type.JenisProduk.values()) {
                hasilProduksi.put(jenis, 0);
                barangKeluar.put(jenis, 0);
            }

            int jumlahProduksiCocok = 0;
            for (model.Produksi p : produksi) {
                if (p.getStatusProduksi() != model.type.StatusProduksi.Selesai) continue;
                model.type.JenisProduk jenis = jenisDariNama(p.getJenisProduksi());
                if (!"Semua Produk".equals(produkFilter)
                        && !jenis.getNamaProduk().equalsIgnoreCase(produkFilter)) continue;
                hasilProduksi.put(jenis, hasilProduksi.get(jenis) + p.getJumlahProduksi());
                jumlahProduksiCocok++;
            }

            javax.swing.table.DefaultTableModel m1 = new javax.swing.table.DefaultTableModel(
                    new Object[]{"Produk", "Stok Awal", "Produksi Selesai", "Barang Keluar",
                        "Stok Tersedia", "Status"}, 0) {
                @Override public boolean isCellEditable(int r, int c) { return false; }
            };
            javax.swing.table.DefaultTableModel m2 = new javax.swing.table.DefaultTableModel(
                    new Object[]{"Agen", "Produk", "Jumlah", "Status"}, 0) {
                @Override public boolean isCellEditable(int r, int c) { return false; }
            };

            int jumlahRincianDistribusi = 0;
            if (role != model.type.Role.Produksi && !"Produksi".equalsIgnoreCase(jenisLaporan)) {
                for (model.Distribusi d : sistemController.getDistribusiController().loadDistribusi()) {
                    if (d.getTanggalKirim().isBefore(range[0]) || d.getTanggalKirim().isAfter(range[1])) continue;
                    String status = d.getStatusKirim().getNilaiDatabase();
                    if (!"Semua Status".equals(statusFilter)
                            && !status.equalsIgnoreCase(statusFilter)) continue;

                    for (model.DetailDistribusi detail : sistemController.getDistribusiController()
                            .loadDetailDistribusi(d.getIdDistribusi())) {
                        String namaProduk = detail.getJenisProduk().getNamaProduk();
                        if (!"Semua Produk".equals(produkFilter)
                                && !namaProduk.equalsIgnoreCase(produkFilter)) continue;

                        model.Agen agen = sistemController.getAgenController().cariAgen(detail.getIdAgen());
                        m2.addRow(new Object[]{
                            agen == null ? detail.getIdAgen() : agen.getNamaAgen(),
                            namaProduk, detail.getJumlahPesan(), status});
                        jumlahRincianDistribusi++;

                        if (d.getStatusKirim() == model.type.StatusKirim.TERKIRIM) {
                            barangKeluar.put(detail.getJenisProduk(),
                                    barangKeluar.get(detail.getJenisProduk()) + detail.getJumlahPesan());
                        }
                    }
                }
            }

            for (model.type.JenisProduk jenis : model.type.JenisProduk.values()) {
                if (!"Semua Produk".equals(produkFilter)
                        && !jenis.getNamaProduk().equalsIgnoreCase(produkFilter)) continue;
                int tersedia = sistemController.getProdukController().hitungStokTersedia(jenis);
                Object produksiCell = role == model.type.Role.Distribusi
                        || "Distribusi".equalsIgnoreCase(jenisLaporan) ? "—" : hasilProduksi.get(jenis);
                Object keluarCell = role == model.type.Role.Produksi
                        || "Produksi".equalsIgnoreCase(jenisLaporan) ? "—" : barangKeluar.get(jenis);
                m1.addRow(new Object[]{jenis.getNamaProduk(), "—", produksiCell,
                    keluarCell, tersedia, tersedia == 0 ? "Kosong" : "Tersedia"});
            }

            jTable1.setModel(m1);
            jTable2.setModel(m2);
            boolean tampilDistribusi = role != model.type.Role.Produksi
                    && !"Produksi".equalsIgnoreCase(jenisLaporan);
            jPanel14.setVisible(tampilDistribusi);
            jPanel13.setVisible(true);

            jLabel34.setText("Keterangan Stok");
            jLabel35.setText("Batas minimum stok");
            jLabel36.setText("—");
            jLabel37.setText("Tidak dicatat");
            jLabel38.setText("Belum dihitung");
            jLabel20.setText("<html><div style='width:220px'>Database belum menyimpan batas minimum stok. Status stok menipis belum dapat dihitung.</div></html>");

            boolean tidakAdaData = "Produksi".equalsIgnoreCase(jenisLaporan)
                    ? jumlahProduksiCocok == 0
                    : "Distribusi".equalsIgnoreCase(jenisLaporan)
                        ? jumlahRincianDistribusi == 0
                        : jumlahProduksiCocok == 0 && jumlahRincianDistribusi == 0;
            if (tidakAdaData) {
                jLabel17.setText("Tidak ada baris data yang sesuai dengan periode dan filter yang dipilih.");
            } else {
                jLabel17.setText("Periode " + range[0] + " sampai " + range[1]
                        + " | " + jumlahProduksiCocok + " produksi selesai"
                        + " | " + jumlahRincianDistribusi + " rincian distribusi");
            }
            jPanel2.revalidate();
            jPanel2.repaint();
        } catch (RuntimeException ex) { ViewHelper.error(this, ex); }
    }

    private void siapkanFilterLaporan() {
        javax.swing.JLabel labelStatus = new javax.swing.JLabel("Status Transaksi");
        labelStatus.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 12));
        jLabel30.setText("Periode");
        jLabel31.setText("Jenis Laporan");
        jLabel32.setText("Produk");

        jPanel11.removeAll();
        jPanel11.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel11.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 14, 180, 20));
        jPanel11.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 38, 900, 20));
        jPanel11.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 68, 140, 18));
        jPanel11.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 88, 150, 32));
        jPanel11.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(185, 68, 140, 18));
        jPanel11.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(185, 88, 135, 32));
        jPanel11.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(335, 68, 150, 18));
        jPanel11.add(jComboBox2, new org.netbeans.lib.awtextra.AbsoluteConstraints(335, 88, 150, 32));
        jPanel11.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 68, 170, 18));
        jPanel11.add(jComboBox3, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 88, 180, 32));
        jPanel11.add(labelStatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(695, 68, 150, 18));
        jPanel11.add(jComboBox4, new org.netbeans.lib.awtextra.AbsoluteConstraints(695, 88, 150, 32));
        jPanel11.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 88, 130, 32));
        jPanel11.revalidate();
        jPanel11.repaint();
    }

    private model.type.JenisProduk jenisDariNama(String nama) {
        for (model.type.JenisProduk jenis : model.type.JenisProduk.values()) {
            if (jenis.getNamaProduk().equalsIgnoreCase(nama)) return jenis;
        }
        throw new IllegalArgumentException("Jenis produksi tidak dikenali: " + nama);
    }

@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox<>();
        jComboBox4 = new javax.swing.JComboBox<>();
        jButton2 = new javax.swing.JButton();
        jPanel13 = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel14 = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel15 = new javax.swing.JPanel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        jButton10 = new javax.swing.JButton();
        jSeparator10 = new javax.swing.JSeparator();
        jLabel3 = new javax.swing.JLabel();

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

        jButton1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(0, 105, 148));
        jButton1.setText("Dashboard");
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, 122, 33));

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

        jLabel16.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel16.setText("Filter Laporan");

        jLabel17.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(128, 128, 128));
        jLabel17.setText("Pilih tanggal acuan dan periode laporan yang ingin ditinjau.");

        jLabel29.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel29.setText("Tanggal Acuan");

        jTextField1.setText("27 Okt 2026 – 27 Okt 2026");

        jLabel30.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel30.setText("Produk");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Minggu", "Bulan", "Tahun" }));

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Gabungan", "Produksi", "Distribusi" }));

        jLabel31.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel31.setText("Jenis Laporan");

        jLabel32.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel32.setText("Cakupan Data");

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Semua Data" }));

        jButton2.setBackground(new java.awt.Color(0, 105, 148));
        jButton2.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Tampilkan");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 77, Short.MAX_VALUE)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(44, 44, 44))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29)
                    .addComponent(jLabel30)
                    .addComponent(jLabel31)
                    .addComponent(jLabel32))
                .addGap(10, 10, 10)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 1050, 150));

        jLabel28.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel28.setText("Rekap Operasional Produk");

        jTable1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel28)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 290, 1050, 210));

        jLabel33.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel33.setText("Distribusi Barang Keluar");

        jTable2.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Agen", "Produk", "Jumlah", "Status"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Float.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jTable2.setAutoscrolls(false);
        jTable2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jScrollPane2.setViewportView(jTable2);

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 652, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(17, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel33)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 510, 690, 190));

        jLabel34.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel34.setText("Laporan Stok Menipis");

        jLabel35.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel35.setText("Botol 600 Ml");

        jLabel18.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(128, 128, 128));
        jLabel18.setText("Stok Akhir");

        jLabel19.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(128, 128, 128));
        jLabel19.setText("Minimum");

        jLabel36.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel36.setText("180 Dus");

        jLabel37.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel37.setText("250 Dus");

        jLabel20.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(128, 128, 128));
        jLabel20.setText("Kekurangan terhadap minimum: 70 dus. Rekomendasi: jadwalkan tambahan produksi Botol 330 ml.");

        jLabel38.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(255, 204, 51));
        jLabel38.setText("Menipis");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel15Layout.createSequentialGroup()
                                .addGap(11, 11, 11)
                                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel36))
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel15Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jLabel37)
                                .addGap(0, 46, Short.MAX_VALUE))
                            .addGroup(jPanel15Layout.createSequentialGroup()
                                .addGap(29, 29, 29)
                                .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel38)
                        .addGap(43, 43, 43))))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel34)
                .addGap(18, 18, 18)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel35)
                    .addComponent(jLabel38))
                .addGap(18, 18, 18)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(jLabel19))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel36)
                    .addComponent(jLabel37))
                .addGap(21, 21, 21)
                .addComponent(jLabel20)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 510, 300, 190));

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

        jLabel3.setBackground(new java.awt.Color(255, 255, 255));
        jLabel3.setFont(new java.awt.Font("Bahnschrift", 1, 36)); // NOI18N
        jLabel3.setText("Pemantauan dan Laporan");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 510, 42));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 0, 1100, 720));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        prosesLogout();
    }//GEN-LAST:event_jButton10ActionPerformed


    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton8;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
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
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
