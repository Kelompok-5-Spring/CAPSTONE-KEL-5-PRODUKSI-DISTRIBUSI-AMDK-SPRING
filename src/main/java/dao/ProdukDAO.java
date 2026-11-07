/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import java.util.List;
import model.Produk;
import model.Produksi;
import model.type.JenisProduk;
import model.type.StatusKirim;

public interface ProdukDAO extends CrudDAO<Produk> {
    List<Produk> findByJenis(JenisProduk jenis);
    int hitungStokTersedia(JenisProduk jenis);
    int hitungProdukDenganStatus(JenisProduk jenis, StatusKirim status);
    void buatProdukDariProduksi(Produksi produksi);
}
