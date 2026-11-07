package view;

import controller.SistemController;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ArrayDeque;
import java.util.Deque;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.Font;
import java.awt.Color;
import java.awt.Cursor;
import javax.swing.AbstractButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import model.Petugas;
import model.PetugasAdmin;
import model.PetugasDistribusi;
import model.PetugasGudang;
import model.PetugasProduksi;
import model.type.Role;

/** Bantuan navigasi, validasi akses, dan komponen bersama untuk seluruh form SPRING. */
public final class ViewHelper {
    private static final DateTimeFormatter FORMAT_TANGGAL_ID =
            DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.forLanguageTag("id-ID"));
    private static final Deque<String> RIWAYAT_HALAMAN = new ArrayDeque<>();
    private static final int BATAS_RIWAYAT = 30;

    private ViewHelper() { }

    public static void pasangTombol(AbstractButton tombol, ActionListener aksi) {
        if (tombol == null || aksi == null) return;
        for (ActionListener lama : tombol.getActionListeners()) tombol.removeActionListener(lama);
        tombol.addActionListener(aksi);
    }

    public static void pilihBarisSaatKlikKanan(JTable tabel) {
        if (tabel == null) return;
        tabel.addMouseListener(new java.awt.event.MouseAdapter() {
            private void pilih(java.awt.event.MouseEvent e) {
                if (e.isPopupTrigger()) {
                    int row = tabel.rowAtPoint(e.getPoint());
                    if (row >= 0) tabel.setRowSelectionInterval(row, row);
                }
            }
            @Override public void mousePressed(java.awt.event.MouseEvent e) { pilih(e); }
            @Override public void mouseReleased(java.awt.event.MouseEvent e) { pilih(e); }
        });
    }

    public static boolean konfirmasi(Component induk, String pesan) {
        return JOptionPane.showConfirmDialog(induk, pesan, "Konfirmasi",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;
    }

    public static void info(Component induk, String pesan) {
        JOptionPane.showMessageDialog(induk, pesan, "Informasi", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void error(Component induk, Exception ex) {
        String pesan = ex == null ? "Terjadi kesalahan yang tidak diketahui." : ex.getMessage();
        if (pesan == null || pesan.isBlank()) pesan = ex.getClass().getSimpleName();
        JOptionPane.showMessageDialog(induk, pesan, "Terjadi Kesalahan", JOptionPane.ERROR_MESSAGE);
    }

    public static LocalDate tanggal(String nilai) {
        if (nilai == null || nilai.isBlank()) throw new IllegalArgumentException("Tanggal wajib diisi.");
        String teks = nilai.trim();
        for (DateTimeFormatter formatter : new DateTimeFormatter[]{
                DateTimeFormatter.ISO_LOCAL_DATE,
                DateTimeFormatter.ofPattern("dd/MM/yyyy"),
                DateTimeFormatter.ofPattern("dd-MM-yyyy")}) {
            try { return LocalDate.parse(teks, formatter); }
            catch (RuntimeException ignored) { }
        }
        throw new IllegalArgumentException("Format tanggal harus yyyy-MM-dd atau dd/MM/yyyy.");
    }

    public static String spesialisasi(Petugas petugas) {
        if (petugas instanceof PetugasAdmin) return ((PetugasAdmin) petugas).getSpesialisasiAdmin();
        if (petugas instanceof PetugasProduksi) return ((PetugasProduksi) petugas).getSpesialisasiProses();
        if (petugas instanceof PetugasGudang) return ((PetugasGudang) petugas).getSpesialisasiTugas();
        if (petugas instanceof PetugasDistribusi) return ((PetugasDistribusi) petugas).getWilayahTugas();
        return "";
    }

    public static String mintaSpesialisasi(Component induk, Role role) {
        Object[] pilihan;
        if (role == Role.Admin) pilihan = new Object[]{"Admin Utama", "Admin Keuangan"};
        else if (role == Role.Produksi) pilihan = new Object[]{"Pengisian", "Pengemasan", "Quality Check"};
        else if (role == Role.Gudang) pilihan = new Object[]{"Inbound", "Outbound", "Stock Checker"};
        else pilihan = new Object[]{"Palaran", "Samarinda Ilir", "Samarinda Kota",
                "Samarinda Utara", "Samarinda Ulu", "Sungai Kunjang"};
        Object hasil = JOptionPane.showInputDialog(induk, "Pilih spesialisasi / wilayah tugas:",
                "Detail Petugas", JOptionPane.QUESTION_MESSAGE, null, pilihan, pilihan[0]);
        return hasil == null ? null : hasil.toString();
    }

    public static void setHeader(JLabel tanggal, JLabel nama, JLabel role, SistemController sistem) {
        if (tanggal != null) tanggal.setText(LocalDate.now().format(FORMAT_TANGGAL_ID));
        if (sistem == null || sistem.getAkunController() == null
                || sistem.getAkunController().getPetugasAktif() == null) return;
        Petugas aktif = sistem.getAkunController().getPetugasAktif();
        if (nama != null) nama.setText(aktif.getNama());
        if (role != null) role.setText(aktif.getRole().getNilaiDatabase());
    }

    public static void bukaDashboard(JFrame asal, SistemController sistem) {
        Petugas aktif = petugasAktif(sistem);
        switch (aktif.getRole()) {
            case Admin: buka(asal, sistem, "DASHBOARD_ADMIN"); break;
            case Produksi: buka(asal, sistem, "DASHBOARD_PRODUKSI"); break;
            case Gudang: buka(asal, sistem, "DASHBOARD_GUDANG"); break;
            case Distribusi: buka(asal, sistem, "DASHBOARD_DISTRIBUSI"); break;
            default: throw new SecurityException("Role pengguna tidak dikenali.");
        }
    }

    /** Membuka halaman baru dan menyimpan halaman asal sebagai tujuan tombol Kembali. */
    public static void buka(JFrame asal, SistemController sistem, String tujuan) {
        bukaInternal(asal, sistem, tujuan, true);
    }

    /** Kembali ke halaman sebelumnya. Jika riwayat kosong, kembali ke dashboard sesuai role. */
    public static void kembali(JFrame asal, SistemController sistem) {
        String tujuan = null;
        while (!RIWAYAT_HALAMAN.isEmpty() && tujuan == null) {
            String kandidat = RIWAYAT_HALAMAN.pop();
            if (bolehAkses(petugasAktif(sistem).getRole(), kandidat)) tujuan = kandidat;
        }
        if (tujuan == null) {
            Petugas aktif = petugasAktif(sistem);
            switch (aktif.getRole()) {
                case Admin: tujuan = "DASHBOARD_ADMIN"; break;
                case Produksi: tujuan = "DASHBOARD_PRODUKSI"; break;
                case Gudang: tujuan = "DASHBOARD_GUDANG"; break;
                case Distribusi: tujuan = "DASHBOARD_DISTRIBUSI"; break;
                default: throw new SecurityException("Role pengguna tidak dikenali.");
            }
        }
        bukaInternal(asal, sistem, tujuan, false);
    }

    /** Menambahkan tombol Kembali pada sidebar tanpa mengubah komponen drag-and-drop lama. */
    public static JButton tambahTombolKembali(JPanel sidebar, JFrame asal,
            SistemController sistem, int posisiY) {
        if (sidebar == null) return null;
        JButton kembali = new JButton("Kembali");
        kembali.setFont(new Font("Arial", Font.BOLD, 12));
        kembali.setForeground(new Color(0, 105, 148));
        kembali.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        kembali.addActionListener(e -> kembali(asal, sistem));
        sidebar.add(kembali, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, posisiY, 122, 30));
        sidebar.revalidate();
        sidebar.repaint();
        return kembali;
    }

    private static void bukaInternal(JFrame asal, SistemController sistem,
            String tujuan, boolean simpanRiwayat) {
        if ("LOGIN".equals(tujuan)) {
            RIWAYAT_HALAMAN.clear();
            if (sistem == null || sistem.getAkunController() == null) {
                throw new IllegalStateException("Sistem login belum diinisialisasi.");
            }
            JFrame login = new LoginFrameForm(sistem);
            login.setLocationRelativeTo(null);
            login.setVisible(true);
            if (asal != null && asal != login) asal.dispose();
            return;
        }

        Petugas aktif = petugasAktif(sistem);
        if (!bolehAkses(aktif.getRole(), tujuan)) {
            info(asal, "Akses ditolak. Akun " + aktif.getRole()
                    + " tidak memiliki izin untuk membuka menu tersebut.");
            return;
        }

        JFrame tujuanFrame;
        switch (tujuan) {
            case "DASHBOARD_ADMIN": tujuanFrame = new AdminDashboardForm(sistem); break;
            case "DASHBOARD_PRODUKSI": tujuanFrame = new DashboardProduksiForm(sistem); break;
            case "DASHBOARD_GUDANG": tujuanFrame = new DashboardGudangForm(sistem); break;
            case "DASHBOARD_DISTRIBUSI": tujuanFrame = new DashboardDistribusiForm(sistem); break;
            case "AGEN": tujuanFrame = new DataAgenForm(sistem); break;
            case "TAMBAH_AGEN": tujuanFrame = new DataAgenForm(sistem, true); break;
            case "PRODUK": tujuanFrame = new DataProdukForm(sistem); break;
            case "TAMBAH_PRODUK": tujuanFrame = new DataProdukForm(sistem); break;
            case "PRODUKSI": tujuanFrame = new ManajemenProduksiForm(sistem); break;
            case "DISTRIBUSI": tujuanFrame = new ManajemenDistribusiForm(sistem); break;
            case "STOK": tujuanFrame = new StokGudangForm(sistem); break;
            case "LAPORAN": tujuanFrame = new PantauLaporanForm(sistem); break;
            case "AKUN": tujuanFrame = new ManajemenAkunPengguna(sistem); break;
            default: throw new IllegalArgumentException("Halaman tidak dikenal: " + tujuan);
        }

        if (simpanRiwayat && asal != null && !(asal instanceof LoginFrameForm)) {
            String routeAsal = routeHalaman(asal);
            if (routeAsal != null && !routeAsal.equals(tujuan)) {
                RIWAYAT_HALAMAN.push(routeAsal);
                while (RIWAYAT_HALAMAN.size() > BATAS_RIWAYAT) RIWAYAT_HALAMAN.removeLast();
            }
        }
        tujuanFrame.setLocationRelativeTo(null);
        tujuanFrame.setVisible(true);
        if (asal != null && asal != tujuanFrame) asal.dispose();
    }

    private static String routeHalaman(JFrame frame) {
        if (frame instanceof AdminDashboardForm) return "DASHBOARD_ADMIN";
        if (frame instanceof DashboardProduksiForm) return "DASHBOARD_PRODUKSI";
        if (frame instanceof DashboardGudangForm) return "DASHBOARD_GUDANG";
        if (frame instanceof DashboardDistribusiForm) return "DASHBOARD_DISTRIBUSI";
        if (frame instanceof DataAgenForm) return "AGEN";
        if (frame instanceof DataProdukForm) return "PRODUK";
        if (frame instanceof ManajemenProduksiForm) return "PRODUKSI";
        if (frame instanceof ManajemenDistribusiForm) return "DISTRIBUSI";
        if (frame instanceof StokGudangForm) return "STOK";
        if (frame instanceof PantauLaporanForm) return "LAPORAN";
        if (frame instanceof ManajemenAkunPengguna) return "AKUN";
        return null;
    }

    private static Petugas petugasAktif(SistemController sistem) {
        if (sistem == null || sistem.getAkunController() == null
                || sistem.getAkunController().getPetugasAktif() == null) {
            throw new IllegalStateException("Sesi pengguna belum tersedia. Silakan login kembali.");
        }
        return sistem.getAkunController().getPetugasAktif();
    }

    public static boolean bolehAkses(Role role, String tujuan) {
        if (role == null || tujuan == null) return false;
        if (tujuan.startsWith("DASHBOARD_")) {
            return (role == Role.Admin && tujuan.equals("DASHBOARD_ADMIN"))
                    || (role == Role.Produksi && tujuan.equals("DASHBOARD_PRODUKSI"))
                    || (role == Role.Gudang && tujuan.equals("DASHBOARD_GUDANG"))
                    || (role == Role.Distribusi && tujuan.equals("DASHBOARD_DISTRIBUSI"));
        }
        if (role == Role.Admin) return true;
        switch (tujuan) {
            case "PRODUK": case "LAPORAN": return true;
            case "AGEN": case "TAMBAH_AGEN": case "DISTRIBUSI":
                return role == Role.Gudang || role == Role.Distribusi;
            case "PRODUKSI":
                return role == Role.Produksi || role == Role.Gudang;
            case "STOK":
                return role == Role.Gudang;
            case "AKUN": case "TAMBAH_PRODUK": return false;
            default: return tujuan.equals("LOGIN");
        }
    }

    public static void logout(JFrame asal, SistemController sistem) {
        if (!konfirmasi(asal, "Apakah Anda yakin ingin keluar dari sistem?")) return;
        if (sistem != null && sistem.getAkunController() != null) {
            sistem.getAkunController().prosesLogout();
            buka(asal, sistem, "LOGIN");
        } else if (asal != null) asal.dispose();
    }
}
