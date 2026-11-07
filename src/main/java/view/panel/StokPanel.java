package view.panel;

import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import controller.ProdukController;
import model.type.JenisProduk;
import model.type.StatusKirim;

/** Ringkasan stok produk berdasarkan record produk pada database. */
public class StokPanel extends JPanel {
    private final ProdukController controller;
    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"Jenis Produk", "Total Produk", "Tersedia", "Terkirim"}, 0) {
        @Override public boolean isCellEditable(int row, int col) { return false; }
    };

    public StokPanel(ProdukController controller) {
        super(new BorderLayout(10, 10));
        this.controller = controller;
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JLabel title = new JLabel("Posisi Stok Produk Jadi");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        JLabel note = new JLabel("Perhitungan mengikuti status distribusi dan record produk pada database.");
        note.setForeground(new java.awt.Color(105, 120, 135));

        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.add(title);
        header.add(note);
        add(header, BorderLayout.NORTH);

        JTable table = new JTable(model);
        table.setRowHeight(34);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        add(new JScrollPane(table), BorderLayout.CENTER);
        loadData();
    }

    private void loadData() {
        model.setRowCount(0);

        for (JenisProduk jenis : JenisProduk.values()) {
            int total = controller.loadProduk(jenis).size();
            int tersedia = controller.hitungStokTersedia(jenis);
            int terkirim = controller.hitungProdukDenganStatus(jenis, StatusKirim.TERKIRIM);

            model.addRow(new Object[]{
                jenis.getNamaProduk(),
                total,
                tersedia,
                terkirim
            });
        }
    }
}
