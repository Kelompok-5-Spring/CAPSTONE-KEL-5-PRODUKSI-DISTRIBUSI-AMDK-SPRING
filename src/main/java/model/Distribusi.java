/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import java.time.LocalDate;
import model.type.StatusKirim;

// Header transaksi distribusi. Pesanan agen disimpan pada DetailDistribusi.
public final class Distribusi {
    private final String idDistribusi;
    private LocalDate tanggalKirim;
    private StatusKirim statusKirim;
    private final String idPetugasGudang;
    private final String idPetugasDistribusi;

    public Distribusi(String idDistribusi, LocalDate tanggalKirim, StatusKirim statusKirim,
            String idPetugasGudang, String idPetugasDistribusi) {
        this.idDistribusi = teksWajib(idDistribusi, "ID distribusi");
        if (tanggalKirim == null) throw new IllegalArgumentException("Tanggal kirim wajib diisi.");
        this.tanggalKirim = tanggalKirim;
        if (statusKirim == null) {
            throw new IllegalArgumentException("Status pengiriman wajib dipilih.");
        }
        this.statusKirim = statusKirim;
        this.idPetugasGudang = teksWajib(idPetugasGudang, "ID petugas gudang");
        this.idPetugasDistribusi = teksWajib(idPetugasDistribusi, "ID petugas distribusi");
    }

    public String getIdDistribusi() { return idDistribusi; }
    public LocalDate getTanggalKirim() { return tanggalKirim; }
    public StatusKirim getStatusKirim() { return statusKirim; }
    public String getIdPetugasGudang() { return idPetugasGudang; }
    public String getIdPetugasDistribusi() { return idPetugasDistribusi; }

    public final void setTanggalKirim(LocalDate tanggalKirim) {
        if (tanggalKirim == null) {
            throw new IllegalArgumentException("Tanggal kirim wajib diisi.");
        }
        this.tanggalKirim = tanggalKirim;
    }

    public void ubahStatus(StatusKirim statusBaru) {
        if (statusKirim != StatusKirim.DALAM_PENGIRIMAN) {
            throw new IllegalArgumentException("Status akhir distribusi tidak dapat diubah lagi.");
        }
        if (statusBaru != StatusKirim.TERKIRIM && statusBaru != StatusKirim.TIDAK_TERKIRIM) {
            throw new IllegalArgumentException("Status berikutnya harus Terkirim atau Tidak Terkirim.");
        }
        statusKirim = statusBaru;
    }

    private static String teksWajib(String nilai, String namaKolom) {
        if (nilai == null || nilai.trim().isEmpty()) {
            throw new IllegalArgumentException(namaKolom + " wajib diisi.");
        }
        return nilai.trim();
    }
}
