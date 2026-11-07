/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.type;
// Nilai enum harus sama dengan kolom detail_distribusi.jenis_produk.
public enum JenisProduk {
    BOTOL_600ML("BOTOL_600ML", "Spring Botol 600ml", true, 600.0),
    BOTOL_1500ML("BOTOL_1500ML", "Spring Botol 1500ml", true, 1500.0),
    GALON_5L("GALON_5L", "Spring Galon 5L", false, 5.0),
    GALON_19L("GALON_19L", "Spring Galon 19L", false, 19.0);

    private final String nilaiDatabase;
    private final String namaProduk;
    private final boolean botol;
    private final double volume;

    JenisProduk(String nilaiDatabase, String namaProduk, boolean botol, double volume) {
        this.nilaiDatabase = nilaiDatabase;
        this.namaProduk = namaProduk;
        this.botol = botol;
        this.volume = volume;
    }

    public String getNilaiDatabase(){ 
        return nilaiDatabase; }
    
    public String getNamaProduk(){ 
        return namaProduk; }
    
    public boolean isBotol(){ 
        return botol; }
    
    public double getVolume(){ 
        return volume; }

    public static JenisProduk fromDatabase(String nilai) {
        for (JenisProduk jenis : values()) {
            if (jenis.nilaiDatabase.equalsIgnoreCase(nilai)) {
                return jenis;
            }
        }
        throw new IllegalArgumentException("Jenis produk database tidak dikenal: " + nilai);
    }

    @Override
    public String toString() { return namaProduk; }
}
