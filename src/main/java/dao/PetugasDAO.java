/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import model.Petugas;
public interface PetugasDAO extends CrudDAO<Petugas> {
    Petugas findByUsername(String username);
    String generateIdBaru();
}
