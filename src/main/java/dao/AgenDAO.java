/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import model.Agen;

public interface AgenDAO extends CrudDAO<Agen> {
    String generateIdBaru();
    boolean nomorTeleponDipakai(String nomorTelepon, String idYangDikecualikan);
}
