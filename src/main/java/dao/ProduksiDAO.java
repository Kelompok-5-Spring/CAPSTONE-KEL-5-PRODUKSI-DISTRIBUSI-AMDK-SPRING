/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import model.Produksi;
import model.type.StatusProduksi;

public interface ProduksiDAO extends CrudDAO<Produksi> {
    String generateIdBaru();
    void ubahStatusDanBuatProduk(String idProduksi, StatusProduksi statusBaru);
}
