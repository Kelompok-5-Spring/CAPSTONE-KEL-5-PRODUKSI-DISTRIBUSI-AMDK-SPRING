/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.type;
// Status pengiriman sesuai ENUM pada tabel distribusi.
public enum StatusKirim {
    DALAM_PENGIRIMAN("Dalam Pengiriman"),
    TERKIRIM("Terkirim"),
    TIDAK_TERKIRIM("Tidak Terkirim");

    private final String nilaiDatabase;

    StatusKirim(String nilaiDatabase) {
        this.nilaiDatabase = nilaiDatabase;
    }

    public String getNilaiDatabase(){ 
        return nilaiDatabase; }

    public static StatusKirim fromDatabase(String nilai) {
        for (StatusKirim status : values()) {
            if (status.nilaiDatabase.equalsIgnoreCase(nilai)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Status pengiriman tidak dikenal: " + nilai);
    }

    @Override
    public String toString(){ 
        return nilaiDatabase; }
}
