/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;
import config.DatabaseConnection;
import dao.AgenJdbcDAO;
import dao.DistribusiJdbcDAO;
import dao.LaporanJdbcDAO;
import dao.PetugasJdbcDAO;
import dao.ProdukJdbcDAO;
import dao.ProduksiJdbcDAO;

// Menyiapkan koneksi, DAO, Controller, dan menampilkan halaman login.
public class SistemController {
    private AkunController akunController;
    private AgenController agenController;
    private ProdukController produkController;
    private ProduksiController produksiController;
    private DistribusiController distribusiController;
    private LaporanController laporanController;

    public void mulai() {
        try {
            if (!DatabaseConnection.tesKoneksi()) {
                throw new IllegalStateException("Koneksi database SPRING gagal. Periksa MySQL dan konfigurasi database.");
            }

        PetugasJdbcDAO petugasDAO = new PetugasJdbcDAO();
        AgenJdbcDAO agenDAO = new AgenJdbcDAO();
        ProdukJdbcDAO produkDAO = new ProdukJdbcDAO();
        ProduksiJdbcDAO produksiDAO = new ProduksiJdbcDAO();
        DistribusiJdbcDAO distribusiDAO = new DistribusiJdbcDAO();
        LaporanJdbcDAO laporanDAO = new LaporanJdbcDAO();

        akunController = new AkunController(petugasDAO);
        agenController = new AgenController(agenDAO, akunController);
        produkController = new ProdukController(produkDAO, akunController);
        produksiController = new ProduksiController(produksiDAO, petugasDAO, akunController);
        distribusiController = new DistribusiController(distribusiDAO, agenDAO, petugasDAO, akunController);
        laporanController = new LaporanController(laporanDAO, akunController);

            System.out.println("Koneksi database SPRING berhasil.");
            javax.swing.SwingUtilities.invokeLater(() -> {
                view.LoginFrameForm login = new view.LoginFrameForm(this);
                login.setLocationRelativeTo(null);
                login.setVisible(true);
            });
        } catch (RuntimeException ex) {
            javax.swing.SwingUtilities.invokeLater(() ->
                javax.swing.JOptionPane.showMessageDialog(null,
                    ex.getMessage(), "SPRING - Koneksi Gagal",
                    javax.swing.JOptionPane.ERROR_MESSAGE));
        }
    }

    public AkunController getAkunController() { return akunController; }
    public AgenController getAgenController() { return agenController; }
    public ProdukController getProdukController() { return produkController; }
    public ProduksiController getProduksiController() { return produksiController; }
    public DistribusiController getDistribusiController() { return distribusiController; }
    public LaporanController getLaporanController() { return laporanController; }
}
