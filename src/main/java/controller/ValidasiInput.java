    /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.regex.Pattern;

// Satu class pusat untuk validasi input. Dipindahkan dari util ke package controller.
public final class ValidasiInput {
    private static final Pattern POLA_ID = Pattern.compile("[A-Za-z0-9][A-Za-z0-9_-]{0,14}");
    private static final Pattern POLA_USERNAME = Pattern.compile("[A-Za-z][A-Za-z0-9._-]{2,29}");
    private static final Pattern POLA_NAMA = Pattern.compile("[\\p{L}\\p{M}][\\p{L}\\p{M} .'-]{1,98}[\\p{L}\\p{M}.]");
    private static final Pattern POLA_TELPON = Pattern.compile("08[0-9]{8,11}");

    private ValidasiInput() {
        // Seluruh method bersifat static.
    }

    public static String wajibIsi(String nilai, String namaKolom) {
        if (nilai == null) throw new IllegalArgumentException(namaKolom + " wajib diisi.");
        String hasil = nilai.trim();
        if (hasil.isEmpty()) throw new IllegalArgumentException(namaKolom + " tidak boleh kosong.");
        periksaKarakterKontrol(hasil, namaKolom);
        return hasil;
    }

    public static String wajibIsi(String nilai, String namaKolom, int panjangMaksimum) {
        return teksDenganBatas(nilai, namaKolom, 1, panjangMaksimum);
    }

    public static String teksDenganBatas(String nilai, String namaKolom,
            int panjangMinimum, int panjangMaksimum) {
        if (panjangMinimum < 0 || panjangMaksimum < panjangMinimum) {
            throw new IllegalArgumentException("Batas panjang untuk " + namaKolom + " tidak valid.");
        }
        String hasil = wajibIsi(nilai, namaKolom);
        if (hasil.length() < panjangMinimum) {
            throw new IllegalArgumentException(namaKolom + " minimal " + panjangMinimum + " karakter.");
        }
        if (hasil.length() > panjangMaksimum) {
            throw new IllegalArgumentException(namaKolom + " maksimal " + panjangMaksimum + " karakter.");
        }
        return hasil;
    }

    public static String kodeIdentitas(String nilai, String namaKolom) {
        return kodeIdentitas(nilai, namaKolom, 15);
    }

    public static String kodeIdentitas(String nilai, String namaKolom, int panjangMaksimum) {
        String hasil = teksDenganBatas(nilai, namaKolom, 1, panjangMaksimum);
        if (!POLA_ID.matcher(hasil).matches()) {
            throw new IllegalArgumentException(namaKolom + " hanya boleh berisi huruf, angka, garis bawah, atau tanda hubung.");
        }
        return hasil;
    }

    public static String username(String nilai) {
        String hasil = teksDenganBatas(nilai, "Username", 3, 30).toLowerCase(Locale.ROOT);
        if (!POLA_USERNAME.matcher(hasil).matches()) {
            throw new IllegalArgumentException("Username harus 3-30 karakter, diawali huruf, dan hanya memakai huruf, angka, titik, garis bawah, atau tanda hubung.");
        }
        return hasil;
    }

    public static String namaOrang(String nilai, String namaKolom) {
        String hasil = teksDenganBatas(nilai, namaKolom, 3, 100);
        if (!POLA_NAMA.matcher(hasil).matches()) {
            throw new IllegalArgumentException(namaKolom + " hanya boleh berisi huruf, spasi, titik, apostrof, atau tanda hubung.");
        }
        return hasil;
    }

    public static String teksUmum(String nilai, String namaKolom, int panjangMaksimum) {
        return teksDenganBatas(nilai, namaKolom, 2, panjangMaksimum);
    }

    public static String alamat(String nilai) {
        return teksDenganBatas(nilai, "Alamat lengkap", 3, 255);
    }

    public static String nomorTeleponIndonesia(String nilai) {
        String hasil = wajibIsi(nilai, "Nomor telepon").replaceAll("[\\s()\\-]", "");
        if (hasil.startsWith("+62")) hasil = "0" + hasil.substring(3);
        else if (hasil.startsWith("62")) hasil = "0" + hasil.substring(2);
        if (!POLA_TELPON.matcher(hasil).matches()) {
            throw new IllegalArgumentException("Nomor telepon harus diawali 08 dan terdiri dari 10-13 digit. Contoh: 081234567890.");
        }
        return hasil;
    }

    public static String kodeOpsional(String nilai, String namaKolom) {
        if (nilai == null || nilai.trim().isEmpty()) return null;
        return kodeIdentitas(nilai, namaKolom);
    }

    public static int positif(int angka, String namaKolom) {
        if (angka <= 0) throw new IllegalArgumentException(namaKolom + " harus lebih besar dari 0.");
        return angka;
    }

    public static int parseIntegerPositif(String nilai, String namaKolom) {
        try {
            return positif(Integer.parseInt(wajibIsi(nilai, namaKolom)), namaKolom);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(namaKolom + " harus berupa bilangan bulat.");
        }
    }

    public static int tidakNegatif(int angka, String namaKolom) {
        if (angka < 0) throw new IllegalArgumentException(namaKolom + " tidak boleh negatif.");
        return angka;
    }

    public static int parseIntegerTidakNegatif(String nilai, String namaKolom) {
        try {
            return tidakNegatif(Integer.parseInt(wajibIsi(nilai, namaKolom)), namaKolom);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(namaKolom + " harus berupa bilangan bulat.");
        }
    }

    public static BigDecimal positif(BigDecimal angka, String namaKolom) {
        if (angka == null || angka.signum() <= 0) {
            throw new IllegalArgumentException(namaKolom + " harus lebih besar dari 0.");
        }
        return angka;
    }

    public static BigDecimal tidakNegatif(BigDecimal angka, String namaKolom) {
        if (angka == null || angka.signum() < 0) {
            throw new IllegalArgumentException(namaKolom + " tidak boleh kosong atau negatif.");
        }
        return angka;
    }

    public static BigDecimal parseDesimalPositif(String nilai, String namaKolom) {
        return positif(parseDesimal(nilai, namaKolom), namaKolom);
    }

    public static BigDecimal parseDesimalTidakNegatif(String nilai, String namaKolom) {
        return tidakNegatif(parseDesimal(nilai, namaKolom), namaKolom);
    }

    private static BigDecimal parseDesimal(String nilai, String namaKolom) {
        String teks = wajibIsi(nilai, namaKolom);
        if (teks.contains(",") && teks.contains(".")) {
            throw new IllegalArgumentException(namaKolom + " gunakan titik atau koma sebagai pemisah desimal, tanpa pemisah ribuan.");
        }
        try {
            return new BigDecimal(teks.replace(',', '.'));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(namaKolom + " harus berupa angka yang valid.");
        }
    }

    public static LocalDate tanggalWajib(LocalDate tanggal, String namaKolom) {
        if (tanggal == null) throw new IllegalArgumentException(namaKolom + " wajib diisi.");
        return tanggal;
    }

    public static LocalDate parseTanggalISO(String nilai, String namaKolom) {
        String teks = wajibIsi(nilai, namaKolom);
        try {
            return LocalDate.parse(teks);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(namaKolom + " tidak valid. Gunakan format yyyy-MM-dd.");
        }
    }

    public static <T> T pilihanWajib(T pilihan, String namaKolom) {
        if (pilihan == null) throw new IllegalArgumentException("Silakan pilih " + namaKolom + ".");
        return pilihan;
    }

    public static void validasiRentangTanggal(LocalDate awal, LocalDate akhir) {
        tanggalWajib(awal, "Tanggal awal");
        tanggalWajib(akhir, "Tanggal akhir");
        if (awal.isAfter(akhir)) throw new IllegalArgumentException("Tanggal awal tidak boleh setelah tanggal akhir.");
    }

    public static String passwordBaru(String password) {
        if (password == null || password.length() < 8 || password.length() > 128) {
            throw new IllegalArgumentException("Password harus terdiri dari 8-128 karakter.");
        }
        boolean adaHuruf = false;
        boolean adaAngka = false;
        for (int i = 0; i < password.length(); i++) {
            char karakter = password.charAt(i);
            if (Character.isISOControl(karakter)) {
                throw new IllegalArgumentException("Password tidak boleh mengandung karakter kontrol.");
            }
            if (Character.isLetter(karakter)) adaHuruf = true;
            if (Character.isDigit(karakter)) adaAngka = true;
        }
        if (!adaHuruf || !adaAngka) {
            throw new IllegalArgumentException("Password harus mengandung minimal satu huruf dan satu angka.");
        }
        return password;
    }

    private static void periksaKarakterKontrol(String nilai, String namaKolom) {
        for (int i = 0; i < nilai.length(); i++) {
            if (Character.isISOControl(nilai.charAt(i))) {
                throw new IllegalArgumentException(namaKolom + " mengandung karakter yang tidak diperbolehkan.");
            }
        }
    }
}
