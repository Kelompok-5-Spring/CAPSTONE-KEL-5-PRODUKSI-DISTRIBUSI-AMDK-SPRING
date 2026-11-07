/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import dao.PetugasDAO;
import model.Petugas;
import model.PetugasAdmin;
import model.PetugasDistribusi;
import model.PetugasGudang;
import model.PetugasProduksi;
import model.type.Role;

// Mengelola login, logout, sesi aktif, validasi akun, dan CRUD petugas.
public class AkunController {
    private static final Pattern POLA_USERNAME = Pattern.compile("[A-Za-z][A-Za-z0-9._-]{2,29}");
    private final PetugasDAO dao;
    private Petugas petugasAktif;

    public AkunController(PetugasDAO dao) {
        this.dao = dao;
    }

    public Petugas prosesLogin(String username, String password) {
        String user = validasiUsername(username);
        wajibIsi(password, "Password");
        Petugas akun = dao.findByUsername(user);
        if (akun == null || !passwordCocok(password, akun.getPasswordHash())) {
            petugasAktif = null;
            throw new IllegalArgumentException("Username atau password salah.");
        }
        petugasAktif = akun;
        return petugasAktif;
    }

    public void prosesLogout() {
        petugasAktif = null;
    }

    public Petugas getPetugasAktif() {
        return petugasAktif;
    }

    public boolean sudahLogin() {
        return petugasAktif != null;
    }

    public void wajibRole(Role... roleDiizinkan) {
        if (petugasAktif == null) {
            throw new SecurityException("Silakan login terlebih dahulu.");
        }
        for (Role role : roleDiizinkan) {
            if (petugasAktif.getRole() == role) return;
        }
        throw new SecurityException("Role " + petugasAktif.getRole()
                + " tidak memiliki akses ke fitur ini.");
    }

    public List<Petugas> loadAkun() {
        wajibRole(Role.Admin);
        return dao.findAll();
    }

    public Petugas cariAkun(String id) {
        wajibRole(Role.Admin);
        return dao.findById(validasiKode(id, "ID petugas", 10));
    }

    public Petugas simpanAkun(String username, String nama, String password,
            Role role, String spesialisasi) {
        wajibRole(Role.Admin);
        String user = validasiUsername(username);
        String namaValid = validasiNama(nama, "Nama petugas");
        String pass = validasiPasswordBaru(password);
        if (role == null) throw new IllegalArgumentException("Role wajib dipilih.");
        if (dao.findByUsername(user) != null) {
            throw new IllegalArgumentException("Username sudah digunakan.");
        }

        String idBaru = dao.generateIdBaru();
        Petugas petugas = buatPetugas(idBaru, user, namaValid, hashPassword(pass), role, spesialisasi);
        dao.insert(petugas);
        return petugas;
    }

    public Petugas ubahAkun(String id, String username, String nama,
            String passwordBaru, Role role, String spesialisasi) {
        wajibRole(Role.Admin);
        String idValid = validasiKode(id, "ID petugas", 10);
        Petugas lama = dao.findById(idValid);
        if (lama == null) throw new IllegalArgumentException("Akun petugas tidak ditemukan.");

        String user = validasiUsername(username);
        String namaValid = validasiNama(nama, "Nama petugas");
        if (role == null) throw new IllegalArgumentException("Role wajib dipilih.");

        Petugas usernameLama = dao.findByUsername(user);
        if (usernameLama != null && !usernameLama.getIdPetugas().equals(idValid)) {
            throw new IllegalArgumentException("Username sudah digunakan akun lain.");
        }

        String hash = lama.getPasswordHash();
        if (passwordBaru != null && !passwordBaru.trim().isEmpty()) {
            hash = hashPassword(validasiPasswordBaru(passwordBaru));
        }

        Petugas baru = buatPetugas(idValid, user, namaValid, hash, role, spesialisasi);
        dao.update(baru);
        if (petugasAktif != null && petugasAktif.getIdPetugas().equals(idValid)) {
            petugasAktif = baru;
        }
        return baru;
    }

    public void hapusAkun(String id) {
        wajibRole(Role.Admin);
        String idValid = validasiKode(id, "ID petugas", 10);
        if (petugasAktif.getIdPetugas().equals(idValid)) {
            throw new IllegalArgumentException("Akun yang sedang digunakan tidak dapat dihapus.");
        }
        dao.delete(idValid);
    }

    private Petugas buatPetugas(String id, String username, String nama,
            String hash, Role role, String spesialisasi) {
        String detail = wajibIsi(spesialisasi, "Spesialisasi");
        if (role == Role.Admin) return new PetugasAdmin(id, username, nama, hash, detail);
        if (role == Role.Produksi) return new PetugasProduksi(id, username, nama, hash, detail);
        if (role == Role.Gudang) return new PetugasGudang(id, username, nama, hash, detail);
        if (role == Role.Distribusi) return new PetugasDistribusi(id, username, nama, hash, detail);
        throw new IllegalArgumentException("Role petugas tidak dikenali.");
    }

    private String validasiUsername(String nilai) {
        return ValidasiInput.username(nilai);
    }

    private String validasiNama(String nilai, String kolom) {
        return ValidasiInput.namaOrang(nilai, kolom);
    }

    private String validasiPasswordBaru(String password) {
        return ValidasiInput.passwordBaru(password);
    }

    private String validasiKode(String nilai, String kolom, int panjangMaksimum) {
        return ValidasiInput.kodeIdentitas(nilai, kolom, panjangMaksimum);
    }

    private String wajibIsi(String nilai, String kolom) {
        return ValidasiInput.wajibIsi(nilai, kolom);
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hasil = md.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder teks = new StringBuilder();
            for (byte nilai : hasil) teks.append(String.format("%02x", nilai & 0xff));
            return teks.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 tidak tersedia.", e);
        }
    }

    private boolean passwordCocok(String password, String passwordDatabase) {
        if (password == null || passwordDatabase == null) return false;
        // Data seed saat ini berisi nilai placeholder, sehingga tetap bisa diuji apa adanya.
        if (!passwordDatabase.matches("[0-9a-fA-F]{64}")) {
            return password.equals(passwordDatabase);
        }
        return hashPassword(password).equalsIgnoreCase(passwordDatabase);
    }
}
