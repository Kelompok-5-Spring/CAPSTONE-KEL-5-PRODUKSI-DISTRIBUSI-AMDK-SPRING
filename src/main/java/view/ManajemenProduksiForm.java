/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

/**
 *
 * @author Dovs
 */
public class ManajemenProduksiForm extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger =
            java.util.logging.Logger.getLogger(ManajemenProduksiForm.class.getName());
    private controller.AkunController akunController;
    private controller.SistemController sistemController;
    private String idProduksiTerpilih;

    public ManajemenProduksiForm() {
        initComponents();
        KalenderTanggal.pasang(jTextField4);
        KalenderTanggal.pasang(jTextField5);
    }

    public ManajemenProduksiForm(controller.SistemController sistemController) {
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
        jLabel36.setText("Selamat datang, " + akunController.getPetugasAktif().getNama()
                + ". Catat dan pantau hasil produksi sesuai hak akses.");
        jTextField3.setText("");
        jTextField4.setText(java.time.LocalDate.now().toString());
        jTextField2.setText("");
        jTextField5.setText(java.time.LocalDate.now().toString());
        jTextField6.setText("");

        jComboBox6.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{
            "Spring Galon 5L", "Spring Galon 19L", "Spring Botol 1500ml", "Spring Botol 600ml"}));
        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{
            "Direncanakan", "Diproses", "Selesai", "Dibatalkan"}));
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{
            "Spring Galon 5L", "Spring Galon 19L", "Spring Botol 1500ml", "Spring Botol 600ml"}));
        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{
            "Direncanakan", "Diproses", "Selesai", "Dibatalkan"}));
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{
            "Semua Periode", "Hari Ini", "7 Hari Terakhir", "Bulan Ini"}));
        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{
            "Semua Produk", "Botol", "Galon"}));

        ViewHelper.pasangTombol(jButton1, e -> ViewHelper.bukaDashboard(this, sistemController));
        ViewHelper.pasangTombol(jButton7, e -> ViewHelper.kembali(this, sistemController));
        ViewHelper.pasangTombol(jButton8, e -> ViewHelper.buka(this, sistemController, "LAPORAN"));
        ViewHelper.pasangTombol(jButton2, e -> ViewHelper.logout(this, sistemController));
        ViewHelper.pasangTombol(jButton9, e -> simpanProduksiBaru());
        ViewHelper.pasangTombol(jButton6, e -> { kosongkanFormProduksi(); muatProduksi(); });
        ViewHelper.pasangTombol(jButton11, e -> simpanPerubahanProduksi());
        ViewHelper.pasangTombol(jButton12, e -> hapusProduksiTerpilih());

        boolean bolehKelola = role == model.type.Role.Admin || role == model.type.Role.Produksi;
        jButton9.setEnabled(bolehKelola);
        jButton11.setEnabled(bolehKelola);
        jButton12.setEnabled(role == model.type.Role.Admin);
        jTextField4.setEditable(bolehKelola);
        jTextField2.setEditable(bolehKelola);
        jComboBox6.setEnabled(bolehKelola);
        jComboBox4.setEnabled(bolehKelola);
        jTextField5.setEditable(bolehKelola);
        jTextField6.setEditable(bolehKelola);
        jComboBox1.setEnabled(bolehKelola);
        jComboBox5.setEnabled(bolehKelola);

        jTable2.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && jTable2.getSelectedRow() >= 0) isiFormProduksi();
        });
        jTextField3.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { muatProduksi(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { muatProduksi(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { muatProduksi(); }
        });
        jComboBox2.addActionListener(e -> muatProduksi());
        jComboBox3.addActionListener(e -> muatProduksi());

        javax.swing.JPopupMenu menu = new javax.swing.JPopupMenu();
        javax.swing.JMenuItem status = new javax.swing.JMenuItem("Ubah Status Produksi");
        status.addActionListener(e -> ubahStatusProduksiTerpilih());
        status.setEnabled(bolehKelola);
        javax.swing.JMenuItem hapus = new javax.swing.JMenuItem("Hapus Produksi");
        hapus.addActionListener(e -> hapusProduksiTerpilih());
        hapus.setEnabled(role == model.type.Role.Admin);
        menu.add(status);
        menu.add(hapus);
        jTable2.setComponentPopupMenu(menu);
        ViewHelper.pilihBarisSaatKlikKanan(jTable2);
        muatProduksi();
    }

    private String jenisProduksiBaru() {
        return String.valueOf(jComboBox6.getSelectedItem());
    }

    private String petugasProduksiUntukSimpan() {
        model.Petugas aktif = akunController.getPetugasAktif();
        if (aktif.getRole() == model.type.Role.Produksi) return aktif.getIdPetugas();
        for (model.Petugas p : akunController.loadAkun()) {
            if (p.getRole() == model.type.Role.Produksi) return p.getIdPetugas();
        }
        throw new IllegalArgumentException("Belum ada akun Petugas Produksi. Admin perlu mendaftarkan petugas Produksi.");
    }

    private model.type.StatusProduksi statusDipilih(javax.swing.JComboBox<String> combo) {
        String nilai = String.valueOf(combo.getSelectedItem()).trim();
        if (nilai.equalsIgnoreCase("Direncanakan")) return model.type.StatusProduksi.Direncanakan;
        if (nilai.equalsIgnoreCase("Diproses")) return model.type.StatusProduksi.Diproses;
        if (nilai.equalsIgnoreCase("Selesai")) return model.type.StatusProduksi.Selesai;
        if (nilai.equalsIgnoreCase("Dibatalkan") || nilai.equalsIgnoreCase("Batal")) return model.type.StatusProduksi.Dibatalkan;
        throw new IllegalArgumentException("Pilih status produksi yang valid.");
    }

    private void terapkanStatusProduksi(String id, model.type.StatusProduksi tujuan) {
        model.Produksi saatIni = sistemController.getProduksiController().cariProduksi(id);
        if (saatIni == null || saatIni.getStatusProduksi() == tujuan) return;
        if (saatIni.getStatusProduksi() == model.type.StatusProduksi.Direncanakan
                && tujuan == model.type.StatusProduksi.Selesai) {
            sistemController.getProduksiController().ubahStatusProduksi(id, model.type.StatusProduksi.Diproses);
        }
        sistemController.getProduksiController().ubahStatusProduksi(id, tujuan);
    }

    private void simpanProduksiBaru() {
        if (!ViewHelper.konfirmasi(this, "Simpan pencatatan produksi baru?")) return;
        try {
            java.time.LocalDate tanggal = ViewHelper.tanggal(jTextField4.getText());
            String jenis = jenisProduksiBaru();
            String jumlah = jTextField2.getText().trim();
            String idPetugas = petugasProduksiUntukSimpan();
            model.Produksi baru = sistemController.getProduksiController()
                    .simpanProduksi(jenis, tanggal, jumlah, idPetugas);
            model.type.StatusProduksi statusTujuan = statusDipilih(jComboBox4);
            if (statusTujuan != model.type.StatusProduksi.Direncanakan) {
                terapkanStatusProduksi(baru.getIdProduksi(), statusTujuan);
            }
            kosongkanFormProduksi();
            muatProduksi();
            ViewHelper.info(this, "Produksi berhasil dicatat. ID: " + baru.getIdProduksi());
        } catch (RuntimeException ex) { ViewHelper.error(this, ex); }
    }

    private void simpanPerubahanProduksi() {
        if (idProduksiTerpilih == null) {
            ViewHelper.info(this, "Pilih data produksi dari tabel terlebih dahulu.");
            return;
        }
        if (!ViewHelper.konfirmasi(this, "Simpan perubahan data produksi terpilih?")) return;
        try {
            model.Produksi lama = sistemController.getProduksiController().cariProduksi(idProduksiTerpilih);
            if (lama == null) throw new IllegalArgumentException("Produksi tidak ditemukan.");
            String idPetugas = lama.getIdPetugasProduksi();
            sistemController.getProduksiController().ubahProduksi(idProduksiTerpilih,
                    String.valueOf(jComboBox1.getSelectedItem()),
                    ViewHelper.tanggal(jTextField5.getText()),
                    jTextField6.getText().trim(), idPetugas);
            model.type.StatusProduksi statusTujuan = statusDipilih(jComboBox5);
            if (statusTujuan != lama.getStatusProduksi()) terapkanStatusProduksi(idProduksiTerpilih, statusTujuan);
            kosongkanFormProduksi();
            muatProduksi();
            ViewHelper.info(this, "Data produksi berhasil diperbarui.");
        } catch (RuntimeException ex) { ViewHelper.error(this, ex); }
    }

    private void muatProduksi() {
        if (sistemController == null) return;
        try {
            javax.swing.table.DefaultTableModel m = new javax.swing.table.DefaultTableModel(
                    new Object[]{"ID Produksi", "Produk", "Tanggal Produksi", "Status", "Jumlah Produksi", "ID Petugas"}, 0) {
                @Override public boolean isCellEditable(int r, int c) { return false; }
            };
            String cari = jTextField3.getText().trim().toLowerCase(java.util.Locale.ROOT);
            String filterJenis = String.valueOf(jComboBox3.getSelectedItem());
            String periode = String.valueOf(jComboBox2.getSelectedItem());
            java.time.LocalDate hariIni = java.time.LocalDate.now();
            java.time.LocalDate awal = null;
            java.time.LocalDate akhir = hariIni;
            if ("Hari Ini".equals(periode)) awal = hariIni;
            else if ("7 Hari Terakhir".equals(periode)) awal = hariIni.minusDays(6);
            else if ("Bulan Ini".equals(periode)) awal = hariIni.withDayOfMonth(1);

            for (model.Produksi p : sistemController.getProduksiController().loadProduksi()) {
                String jenis = p.getJenisProduksi().toLowerCase(java.util.Locale.ROOT);
                boolean cocokCari = cari.isEmpty() || cari.startsWith("cari id")
                        || p.getIdProduksi().toLowerCase(java.util.Locale.ROOT).contains(cari)
                        || jenis.contains(cari);
                boolean cocokJenis = "Semua Produk".equals(filterJenis)
                        || ("Botol".equals(filterJenis) && jenis.contains("botol"))
                        || ("Galon".equals(filterJenis) && jenis.contains("galon"));
                boolean cocokTanggal = awal == null || (!p.getTanggalProduksi().isBefore(awal)
                        && !p.getTanggalProduksi().isAfter(akhir));
                if (cocokCari && cocokJenis && cocokTanggal) {
                    m.addRow(new Object[]{p.getIdProduksi(), p.getJenisProduksi(),
                        p.getTanggalProduksi(), p.getStatusProduksi(),
                        p.getJumlahProduksi(), p.getIdPetugasProduksi()});
                }
            }
            jTable2.setModel(m);
            jLabel16.setText(m.getRowCount() == 0
                    ? "Tidak ada baris data produksi yang sesuai dengan pencarian atau filter."
                    : "Data Produksi (" + m.getRowCount() + " transaksi)");
        } catch (RuntimeException ex) { ViewHelper.error(this, ex); }
    }

    private void isiFormProduksi() {
        int row = jTable2.getSelectedRow();
        if (row < 0) return;
        idProduksiTerpilih = String.valueOf(jTable2.getValueAt(row, 0));
        model.Produksi p = sistemController.getProduksiController().cariProduksi(idProduksiTerpilih);
        if (p == null) return;
        jComboBox1.setSelectedItem(p.getJenisProduksi());
        if (p.getStatusProduksi() == model.type.StatusProduksi.Direncanakan) {
            jComboBox5.setModel(new javax.swing.DefaultComboBoxModel<>(
                    new String[]{"Direncanakan", "Diproses", "Dibatalkan"}));
        } else if (p.getStatusProduksi() == model.type.StatusProduksi.Diproses) {
            jComboBox5.setModel(new javax.swing.DefaultComboBoxModel<>(
                    new String[]{"Diproses", "Selesai", "Dibatalkan"}));
        } else {
            jComboBox5.setModel(new javax.swing.DefaultComboBoxModel<>(
                    new String[]{p.getStatusProduksi().getNilaiDatabase()}));
        }
        jComboBox5.setSelectedItem(p.getStatusProduksi().getNilaiDatabase());
        jTextField5.setText(p.getTanggalProduksi().toString());
        jTextField6.setText(String.valueOf(p.getJumlahProduksi()));
        boolean belumFinal = p.getStatusProduksi() != model.type.StatusProduksi.Selesai
                && p.getStatusProduksi() != model.type.StatusProduksi.Dibatalkan;
        boolean bolehUbah = belumFinal && (akunController.getPetugasAktif().getRole() == model.type.Role.Admin
                || akunController.getPetugasAktif().getRole() == model.type.Role.Produksi);
        jButton11.setEnabled(bolehUbah);
        jButton12.setEnabled(akunController.getPetugasAktif().getRole() == model.type.Role.Admin
                && p.getStatusProduksi() != model.type.StatusProduksi.Selesai);
    }

    private void ubahStatusProduksiTerpilih() {
        if (idProduksiTerpilih == null) {
            ViewHelper.info(this, "Pilih transaksi produksi terlebih dahulu.");
            return;
        }
        try {
            model.Produksi p = sistemController.getProduksiController().cariProduksi(idProduksiTerpilih);
            if (p == null) throw new IllegalArgumentException("Produksi tidak ditemukan.");
            Object[] pilihan;
            if (p.getStatusProduksi() == model.type.StatusProduksi.Direncanakan) {
                pilihan = new Object[]{model.type.StatusProduksi.Diproses, model.type.StatusProduksi.Dibatalkan};
            } else if (p.getStatusProduksi() == model.type.StatusProduksi.Diproses) {
                pilihan = new Object[]{model.type.StatusProduksi.Selesai, model.type.StatusProduksi.Dibatalkan};
            } else {
                ViewHelper.info(this, "Status produksi sudah final dan tidak dapat diubah.");
                return;
            }
            Object hasil = javax.swing.JOptionPane.showInputDialog(this, "Pilih status berikutnya:",
                    "Ubah Status Produksi", javax.swing.JOptionPane.QUESTION_MESSAGE, null, pilihan, pilihan[0]);
            if (hasil == null || !ViewHelper.konfirmasi(this, "Ubah status menjadi " + hasil + "?")) return;
            sistemController.getProduksiController().ubahStatusProduksi(idProduksiTerpilih,
                    (model.type.StatusProduksi) hasil);
            muatProduksi();
            ViewHelper.info(this, "Status produksi berhasil diperbarui.");
        } catch (RuntimeException ex) { ViewHelper.error(this, ex); }
    }

    private void hapusProduksiTerpilih() {
        if (akunController.getPetugasAktif().getRole() != model.type.Role.Admin) {
            ViewHelper.info(this, "Hanya Admin yang dapat menghapus data produksi.");
            return;
        }
        if (idProduksiTerpilih == null) {
            ViewHelper.info(this, "Pilih produksi yang akan dihapus.");
            return;
        }
        if (!ViewHelper.konfirmasi(this, "Hapus produksi " + idProduksiTerpilih + "?")) return;
        try {
            sistemController.getProduksiController().hapusProduksi(idProduksiTerpilih);
            kosongkanFormProduksi();
            muatProduksi();
            ViewHelper.info(this, "Data produksi berhasil dihapus.");
        } catch (RuntimeException ex) { ViewHelper.error(this, ex); }
    }

    private void kosongkanFormProduksi() {
        idProduksiTerpilih = null;
        jTextField4.setText(java.time.LocalDate.now().toString());
        jTextField2.setText("");
        jTextField5.setText(java.time.LocalDate.now().toString());
        jTextField6.setText("");
        jTable2.clearSelection();
        jButton11.setEnabled(akunController.getPetugasAktif().getRole() == model.type.Role.Admin
                || akunController.getPetugasAktif().getRole() == model.type.Role.Produksi);
    }

    private void prosesLogout() { ViewHelper.logout(this, sistemController); }

@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel37 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jComboBox4 = new javax.swing.JComboBox<>();
        jComboBox6 = new javax.swing.JComboBox<>();
        jLabel32 = new javax.swing.JLabel();
        jButton9 = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jTextField3 = new javax.swing.JTextField();
        jComboBox2 = new javax.swing.JComboBox<>();
        jComboBox3 = new javax.swing.JComboBox<>();
        jPanel12 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel31 = new javax.swing.JLabel();
        jComboBox5 = new javax.swing.JComboBox<>();
        jLabel39 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jButton6 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jLabel53 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jSeparator10 = new javax.swing.JSeparator();
        jLabel3 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(0, 105, 148));
        jPanel1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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

        jButton7.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jButton7.setForeground(new java.awt.Color(0, 105, 148));
        jButton7.setText("Kembali");
        jPanel1.add(jButton7, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 330, 122, 30));

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

        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setBackground(new java.awt.Color(255, 255, 255));
        jLabel6.setFont(new java.awt.Font("Arial Black", 0, 18)); // NOI18N
        jLabel6.setText("Catat produksi harian");
        jPanel10.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 6, 238, -1));

        jLabel27.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(128, 128, 128));
        jLabel27.setText("Lengkapi data hasil produksi yang sudah selesai.");
        jPanel10.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 272, -1));

        jLabel29.setBackground(new java.awt.Color(255, 255, 255));
        jLabel29.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel29.setText("Jenis Produk *");
        jPanel10.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 70, 110, -1));

        jLabel30.setBackground(new java.awt.Color(255, 255, 255));
        jLabel30.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel30.setText("Status *");
        jPanel10.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 150, 148, -1));

        jTextField2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jTextField2.setText("121");
        jTextField2.addActionListener(this::jTextField2ActionPerformed);
        jPanel10.add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, 170, 40));

        jLabel37.setBackground(new java.awt.Color(255, 255, 255));
        jLabel37.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel37.setText("Tanggal Produksi *");
        jPanel10.add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 164, -1));

        jTextField4.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jTextField4.setText("DD/MM/YYYY");
        jPanel10.add(jTextField4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 170, 40));

        jComboBox4.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Proses", "Selesai", "Batal", " " }));
        jComboBox4.addActionListener(this::jComboBox4ActionPerformed);
        jPanel10.add(jComboBox4, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 170, 179, 40));

        jComboBox6.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jComboBox6.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Galon 5L", "Galon 19L", "Botol 1500 ml", "Botol 600 ml" }));
        jComboBox6.addActionListener(this::jComboBox6ActionPerformed);
        jPanel10.add(jComboBox6, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 90, 179, 40));

        jLabel32.setBackground(new java.awt.Color(255, 255, 255));
        jLabel32.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel32.setText("Kuantitas Produksi *");
        jPanel10.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 150, 148, -1));

        jButton9.setBackground(new java.awt.Color(0, 105, 148));
        jButton9.setFont(new java.awt.Font("Arial Black", 1, 14)); // NOI18N
        jButton9.setForeground(new java.awt.Color(255, 255, 255));
        jButton9.setText("Simpan");
        jPanel10.add(jButton9, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 20, 100, 30));

        jPanel2.add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 150, 510, 230));

        jLabel16.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel16.setText("Data Produksi");

        jTable2.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Id Produksi", "Produk", "Tanggal Produksi", "Status", "Jumlah Produksi"
            }
        ));
        jTable2.setAutoscrolls(false);
        jTable2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jScrollPane2.setViewportView(jTable2);

        jTextField3.setText("Cari Id atau Nama Produk");

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "27 Okt 2026 – 27 Nov 2026", "28 Nov 2026 – 27 Des 2026", "28 Des 2026 – 27 Jan 2027", "28 Jan 2027 – 27 Feb 2027" }));

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Semua Produk", "Galon", "Botol", " " }));

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jTextField3, javax.swing.GroupLayout.DEFAULT_SIZE, 622, Short.MAX_VALUE)
                        .addGap(24, 24, 24)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 848, Short.MAX_VALUE)))
                .addGap(20, 20, 20))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 390, 1050, 290));

        jLabel7.setBackground(new java.awt.Color(255, 255, 255));
        jLabel7.setFont(new java.awt.Font("Arial Black", 0, 18)); // NOI18N
        jLabel7.setText("Ubah produksi harian");

        jLabel28.setBackground(new java.awt.Color(255, 255, 255));
        jLabel28.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel28.setText("Produk");

        jLabel38.setBackground(new java.awt.Color(255, 255, 255));
        jLabel38.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel38.setText("Tanggal Produksi");

        jTextField5.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jTextField5.setText("Kalender");

        jComboBox1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Galon 5L", "Galon 19L", "Botol 1500 ml", "Botol 600 ml" }));
        jComboBox1.addActionListener(this::jComboBox1ActionPerformed);

        jLabel31.setBackground(new java.awt.Color(255, 255, 255));
        jLabel31.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel31.setText("Status");

        jComboBox5.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Galon 5L", "Galon 19L", "Botol 1500 ml", "Botol 600 ml" }));
        jComboBox5.addActionListener(this::jComboBox5ActionPerformed);

        jLabel39.setBackground(new java.awt.Color(255, 255, 255));
        jLabel39.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel39.setText("Jumlah Produksi");

        jTextField6.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jTextField6.setText("Angka");
        jTextField6.addActionListener(this::jTextField6ActionPerformed);

        jButton6.setFont(new java.awt.Font("Arial Black", 1, 14)); // NOI18N
        jButton6.setForeground(new java.awt.Color(0, 105, 148));
        jButton6.setText("Refresh");
        jButton6.addActionListener(this::jButton6ActionPerformed);

        jButton11.setBackground(new java.awt.Color(0, 105, 148));
        jButton11.setFont(new java.awt.Font("Arial Black", 1, 14)); // NOI18N
        jButton11.setForeground(new java.awt.Color(255, 255, 255));
        jButton11.setText("Simpan");

        jButton12.setBackground(new java.awt.Color(255, 0, 51));
        jButton12.setFont(new java.awt.Font("Arial Black", 1, 14)); // NOI18N
        jButton12.setForeground(new java.awt.Color(255, 255, 255));
        jButton12.setText("Hapus");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(28, 28, 28)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel12Layout.createSequentialGroup()
                                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(34, 34, 34))
                            .addGroup(jPanel12Layout.createSequentialGroup()
                                .addComponent(jLabel39, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(16, 18, Short.MAX_VALUE))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24))))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel7))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel28)
                            .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel12Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jLabel38))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel39)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(35, Short.MAX_VALUE))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29))))
        );

        jPanel2.add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 150, 520, 230));

        jLabel53.setText("4 November 2026");
        jPanel2.add(jLabel53, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 30, 110, -1));

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

        jSeparator10.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel2.add(jSeparator10, new org.netbeans.lib.awtextra.AbsoluteConstraints(873, 20, -1, 33));

        jLabel3.setBackground(new java.awt.Color(255, 255, 255));
        jLabel3.setFont(new java.awt.Font("Bahnschrift", 1, 36)); // NOI18N
        jLabel3.setText("Kelola Produksi");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 409, 42));

        jLabel36.setBackground(new java.awt.Color(255, 255, 255));
        jLabel36.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        jLabel36.setText("Selamat siang, Budi. Berikut hasil produksi yang Anda catat hari ini.");
        jPanel2.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 120, 580, -1));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 0, 1100, 720));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        prosesLogout();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jComboBox4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox4ActionPerformed

    private void jComboBox5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox5ActionPerformed

    private void jComboBox6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox6ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jTextField6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField6ActionPerformed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JComboBox<String> jComboBox5;
    private javax.swing.JComboBox<String> jComboBox6;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    // End of variables declaration//GEN-END:variables
}
