/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import java.time.LocalDate;
import model.type.StatusKirim;
import model.type.StatusProduksi;

// Query laporan tidak membutuhkan operasi CRUD umum.
public interface LaporanDAO {
    int hitungJumlahProduksi(LocalDate awal, LocalDate akhir);
    int hitungProduksiDenganStatus(StatusProduksi status, LocalDate awal, LocalDate akhir);
    int hitungJumlahKemasanSelesai(LocalDate awal, LocalDate akhir);
    int hitungJumlahDistribusi(LocalDate awal, LocalDate akhir);
    int hitungDistribusiDenganStatus(StatusKirim status, LocalDate awal, LocalDate akhir);
}
