/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

package view;
import javax.swing.ImageIcon;
import java.awt.Image;

/**
 *
 * @author Dovs
 */
public class LoginFrameForm extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger =
            java.util.logging.Logger.getLogger(LoginFrameForm.class.getName());
    private controller.AkunController akunController;
    private controller.SistemController sistemController;

    public LoginFrameForm() {
        initComponents();
        setTitle("SPRING | Login");
        setLocationRelativeTo(null);
        Password.setEchoChar('\u2022');
        lblLogo.setText("");
    }

    public LoginFrameForm(controller.AkunController akunController) {
        this();
        this.akunController = akunController;
    }

    public LoginFrameForm(controller.SistemController sistemController) {
        this();
        if (sistemController == null || sistemController.getAkunController() == null) {
            throw new IllegalArgumentException("Sistem login belum diinisialisasi.");
        }
        this.sistemController = sistemController;
        this.akunController = sistemController.getAkunController();
    }

    public void setAkunController(controller.AkunController akunController) {
        this.akunController = akunController;
    }

    private void prosesLogin() {
        if (akunController == null) {
            ViewHelper.info(this, "Controller login belum terhubung. Jalankan aplikasi melalui MainApp.");
            return;
        }
        try {
            akunController.prosesLogin(USERNAME.getText().trim(), new String(Password.getPassword()));
            if (sistemController == null) {
                ViewHelper.info(this, "Login berhasil. Konteks dashboard belum diberikan.");
                return;
            }
            ViewHelper.bukaDashboard(this, sistemController);
        } catch (RuntimeException ex) {
            ViewHelper.error(this, ex);
            Password.setText("");
            Password.requestFocusInWindow();
        }
    }

@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField1 = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        PanelKiri = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        USERNAME = new javax.swing.JTextField();
        Password = new javax.swing.JPasswordField();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jCheckBox1 = new javax.swing.JCheckBox();
        jLabel13 = new javax.swing.JLabel();
        lblLogo = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel4 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        lblGambarbg = new javax.swing.JLabel();

        jTextField1.setText("jTextField1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1386, 0, -1, -1));

        PanelKiri.setBackground(new java.awt.Color(12, 35, 64));
        PanelKiri.setForeground(new java.awt.Color(27, 105, 190));
        PanelKiri.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setBackground(new java.awt.Color(30, 144, 255));
        jLabel1.setFont(new java.awt.Font("Arial Black", 0, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(30, 144, 255));
        jLabel1.setText("SELAMAT DATANG");
        jPanel3.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 120, -1, 32));

        USERNAME.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        USERNAME.setToolTipText("");
        USERNAME.addActionListener(this::USERNAMEActionPerformed);
        jPanel3.add(USERNAME, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 250, 510, 44));

        Password.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        Password.addActionListener(this::PasswordActionPerformed);
        jPanel3.add(Password, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 340, 510, 46));

        jLabel3.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        jLabel3.setText("Username");
        jPanel3.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 230, -1, -1));

        jLabel5.setFont(new java.awt.Font("Bahnschrift", 0, 12)); // NOI18N
        jLabel5.setText("Setelah akun terverifikasi, Anda akan diarahkan ke dashboard  sesuai hak akses yang diberikan Admin.");
        jPanel3.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 490, 560, 30));

        jLabel6.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        jLabel6.setText("Gunakan akun yang telah didaftarkan untuk melanjutkan.");
        jPanel3.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 190, -1, -1));

        jLabel7.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        jLabel7.setText("Password");
        jPanel3.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 320, -1, -1));

        jButton2.setBackground(new java.awt.Color(0, 105, 148));
        jButton2.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jButton2.setForeground(new java.awt.Color(252, 252, 252));
        jButton2.setText("Masuk");
        jButton2.addActionListener(this::jButton2ActionPerformed);
        jPanel3.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 460, 510, 34));

        jCheckBox1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jCheckBox1.setText("Tampilkan Password");
        jCheckBox1.addActionListener(this::jCheckBox1ActionPerformed);
        jPanel3.add(jCheckBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 400, 220, -1));

        jLabel13.setBackground(new java.awt.Color(255, 255, 255));
        jLabel13.setFont(new java.awt.Font("Arial Black", 1, 28)); // NOI18N
        jLabel13.setText("AMDK SPRING");
        jPanel3.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 150, 259, -1));

        PanelKiri.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 0, 660, 740));

        lblLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Spring Logo with Swirl Emblem (2).png"))); // NOI18N
        PanelKiri.add(lblLogo, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, -1, -1));

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setFont(new java.awt.Font("Arial Black", 1, 28)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        PanelKiri.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 430, 259, -1));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jSeparator1.setForeground(new java.awt.Color(0, 105, 148));

        jLabel4.setBackground(new java.awt.Color(255, 255, 255));
        jLabel4.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 105, 148));
        jLabel4.setText("AMDK SPRING OPERASIONAL ");

        jLabel9.setBackground(new java.awt.Color(255, 255, 255));
        jLabel9.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 105, 148));
        jLabel9.setText("Dari mesin produksi hingga distribusi, kendalikan setiap tetes  ");

        jLabel12.setBackground(new java.awt.Color(255, 255, 255));
        jLabel12.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(0, 105, 148));
        jLabel12.setText("kualitas dalam satu kendali terpadu ");

        jLabel11.setBackground(new java.awt.Color(255, 255, 255));
        jLabel11.setFont(new java.awt.Font("Arial Black", 1, 30)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 105, 148));
        jLabel11.setText("Operasional terkendali. ");

        jLabel14.setBackground(new java.awt.Color(0, 105, 148));
        jLabel14.setFont(new java.awt.Font("Arial Black", 1, 28)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(0, 105, 148));
        jLabel14.setText("Aliran kerja jernih. ");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(94, 94, 94)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4))
                    .addComponent(jLabel9))
                .addContainerGap(109, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        PanelKiri.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 460, 640, 160));

        jLabel10.setBackground(new java.awt.Color(255, 255, 255));
        jLabel10.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Spring Operasional");
        PanelKiri.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 680, -1, 12));

        lblGambarbg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/pabrik 2.png"))); // NOI18N
        lblGambarbg.setText("a");
        PanelKiri.add(lblGambarbg, new org.netbeans.lib.awtextra.AbsoluteConstraints(-300, 0, 950, 720));

        getContentPane().add(PanelKiri, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }


    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {
        Password.setEchoChar(jCheckBox1.isSelected() ? (char) 0 : '\u2022');
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        prosesLogin();
    }

    private void PasswordActionPerformed(java.awt.event.ActionEvent evt) {
        prosesLogin();
    }

    private void USERNAMEActionPerformed(java.awt.event.ActionEvent evt) {
        prosesLogin();
    }

// Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PanelKiri;
    private javax.swing.JPasswordField Password;
    private javax.swing.JTextField USERNAME;
    private javax.swing.JButton jButton2;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel lblGambarbg;
    private javax.swing.JLabel lblLogo;
    // End of variables declaration//GEN-END:variables
}
