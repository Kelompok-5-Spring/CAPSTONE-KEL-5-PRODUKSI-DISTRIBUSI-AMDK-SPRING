/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import java.util.List;
// Kontrak CRUD untuk DAO yang mengelola data utama.
public interface CrudDAO<T> {
    void insert(T objek);
    List<T> findAll();
    T findById(String id);
    void update(T objek);
    void delete(String id);
}
