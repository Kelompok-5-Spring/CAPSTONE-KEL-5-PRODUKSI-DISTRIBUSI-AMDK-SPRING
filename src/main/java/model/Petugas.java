/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import java.util.List;
import model.type.Role;

// Superclass untuk Admin, Produksi, Gudang, dan Distribusi.
public abstract class Petugas implements AksesMenu {
    private final String idPetugas;
    private String username;
    private String passwordHash;
    private final Role role;
    private String nama;

    protected Petugas(String idPetugas, String username, String nama,
            String passwordHash, Role role) {
        this.idPetugas = teksWajib(idPetugas, "ID petugas");
        this.username = teksWajib(username, "Username").toLowerCase();
        if (this.username.length() < 3 || this.username.length() > 30) {
            throw new IllegalArgumentException("Username harus terdiri dari 3 sampai 30 karakter.");
        }
        this.nama = teksWajib(nama, "Nama petugas");
        if (this.nama.length() > 100) {
            throw new IllegalArgumentException("Nama petugas maksimal 100 karakter.");
        }
        this.passwordHash = teksWajib(passwordHash, "Password");
        if (role == null) {
            throw new IllegalArgumentException("Role petugas wajib dipilih.");
        }
        this.role = role;
    }

    public String getIdPetugas(){ 
        return idPetugas; }
    
    public String getUsername(){ 
        return username; }
    
    public String getNama(){ 
        return nama; }
    
    public String getPasswordHash(){ 
        return passwordHash; }
    
    public Role getRole(){ 
        return role; }

    public final void setUsername(String username) {
        String nilai = teksWajib(username, "Username").toLowerCase();
        if (nilai.length() < 3 || nilai.length() > 30) {
            throw new IllegalArgumentException("Username harus terdiri dari 3 sampai 30 karakter.");
        }
        this.username = nilai;
    }

    public final void setNama(String nama) {
        String nilai = teksWajib(nama, "Nama petugas");
        if (nilai.length() > 100) {
            throw new IllegalArgumentException("Nama petugas maksimal 100 karakter.");
        }
        this.nama = nilai;
    }

    public final void setPasswordHash(String passwordHash) {
        this.passwordHash = teksWajib(passwordHash, "Password");
    }

    public abstract List<String> getMenuTersedia();

    public String getInfoPetugas() {
        return idPetugas + " - " + nama + " (" + role + ")";
    }

    protected static String teksWajib(String nilai, String namaKolom) {
        if (nilai == null || nilai.trim().isEmpty()) {
            throw new IllegalArgumentException(namaKolom + " wajib diisi.");
        }
        return nilai.trim();
    }

    @Override
    public String toString() { return getInfoPetugas(); }
}
