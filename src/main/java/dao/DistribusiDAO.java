/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import java.time.LocalDate;
import java.util.List;
import model.DetailDistribusi;
import model.Distribusi;
import model.type.StatusKirim;

public interface DistribusiDAO extends CrudDAO<Distribusi> {
    String generateIdBaru();
    List<String> generateIdDetailBaru(int jumlah);
    void insertWithDetails(Distribusi distribusi, List<DetailDistribusi> details);
    List<DetailDistribusi> findDetails(String idDistribusi);
    List<Distribusi> findByPetugasDistribusi(String idPetugas);
    void ubahStatus(String idDistribusi, StatusKirim statusBaru, boolean barangSudahKembali);
    void updateTanggalKirim(String idDistribusi, LocalDate tanggalKirim);
}
