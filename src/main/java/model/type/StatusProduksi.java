/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.type;
// Status produksi sesuai ENUM pada tabel produksi.
public enum StatusProduksi {
    Direncanakan("Direncanakan"),
    Diproses("Diproses"),
    Selesai("Selesai"),
    Dibatalkan("Dibatalkan");

    private final String nilaiDatabase;

    StatusProduksi(String nilaiDatabase) {
        this.nilaiDatabase = nilaiDatabase;
    }

    public String getNilaiDatabase() { return nilaiDatabase; }

    public static StatusProduksi fromDatabase(String nilai) {
        for (StatusProduksi status : values()) {
            if (status.nilaiDatabase.equalsIgnoreCase(nilai)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Status produksi tidak dikenal: " + nilai);
    }

    @Override
    public String toString() { return nilaiDatabase; }
}
