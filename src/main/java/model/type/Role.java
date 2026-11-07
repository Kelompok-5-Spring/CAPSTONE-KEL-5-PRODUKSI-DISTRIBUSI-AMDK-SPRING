/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.type;
// Nilai role mengikuti ENUM pada tabel petugas.
public enum Role {
    Admin("Admin"), Produksi("Produksi"), Gudang("Gudang"), Distribusi("Distribusi");

    private final String nilaiDatabase;

    Role(String nilaiDatabase) {
        this.nilaiDatabase = nilaiDatabase;
    }

    public String getNilaiDatabase() {
        return nilaiDatabase;
    }

    public static Role fromDatabase(String nilai) {
        for (Role role : values()) {
            if (role.nilaiDatabase.equalsIgnoreCase(nilai)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Role database tidak dikenal: " + nilai);
    }

    @Override
    public String toString() {
        return nilaiDatabase;
    }
}

