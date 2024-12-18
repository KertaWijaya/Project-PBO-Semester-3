/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.pbo.gui.Admin;
import com.pbo.gui.GUI;
import com.pbo.gui.Login;
import com.pbo.gui.Login1;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author asus
 */
public class Laporan extends javax.swing.JFrame {

    /**
     * Creates new form Dashboard
     */
    public Laporan() {
        initComponents();
        loadBarangKeluar();
        loadData();
        loadLaporan();

        Timer timer = new Timer(2000, new ActionListener() {
       
        public void actionPerformed(ActionEvent e) {
            loadBarangKeluar();
            loadData();
            loadLaporan();
        }
    });
        timer.start();
    }
    
    private void loadData() {
    // Ambil model dari TabelData
    DefaultTableModel model = (DefaultTableModel) TabelBarangMasuk.getModel();
    
    // Bersihkan data sebelumnya (jika ada)
    model.setRowCount(0);

    try {
        // Koneksi ke database
        Connection conn = GUI.mycon();
        String sql = "SELECT * FROM barang_masuk"; // Sesuaikan dengan nama tabel Anda
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        // Iterasi hasil query dan tambahkan data ke model
        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getInt("id_masuk"), // Ganti sesuai dengan kolom tabel database Anda
                rs.getInt("id_supplier"),
                rs.getString("tanggal_masuk"),
                rs.getString("id_barang"),
                rs.getString("nama_barang"),
                rs.getString("kategori"),
                rs.getInt("jumlah"),
                rs.getDouble("harga_beli")
            });
        }

        rs.close();
        ps.close();
       
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal memuat data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    }
    
    private void loadLaporan() {
    // Ambil model dari TabelData
DefaultTableModel model = (DefaultTableModel) TabelLaporan.getModel();

// Bersihkan data sebelumnya (jika ada)
model.setRowCount(0);

try {
    // Koneksi ke database
    Connection conn = GUI.mycon();

    // SQL untuk laporan
    String sql = "SELECT bm.tanggal_masuk, bm.id_barang, bm.nama_barang, " +
                 "SUM(bk.pendapatan) AS total_pendapatan, " +
                 "SUM(bm.harga_beli) AS total_pengeluaran " +
                 "FROM barang_masuk bm " +
                 "LEFT JOIN barang_keluar bk ON bm.id_barang = bk.id_barang " +
                 "GROUP BY bm.tanggal_masuk, bm.id_barang";

    PreparedStatement ps = conn.prepareStatement(sql);

    ResultSet rs = ps.executeQuery();

    // Iterasi hasil query dan tambahkan data ke model
    while (rs.next()) {
        double pendapatan = rs.getDouble("total_pendapatan");
        double pengeluaran = rs.getDouble("total_pengeluaran");
        double untung = pendapatan - pengeluaran; // Menghitung untung

        model.addRow(new Object[]{
            rs.getString("tanggal_masuk"), // Tanggal Masuk
            rs.getString("id_barang"), // ID Barang
            rs.getString("nama_barang"), // Nama Barang
            pendapatan, // Total Pendapatan
            pengeluaran, // Total Pengeluaran
            untung // Total Untung (Pendapatan - Pengeluaran)
        });
    }

    rs.close();
    ps.close();

} catch (Exception e) {
    JOptionPane.showMessageDialog(this, "Gagal memuat laporan: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
}

    }

    
    private void loadBarangKeluar() {
    // Ambil model dari TabelData
    DefaultTableModel model = (DefaultTableModel) TabelCustomer.getModel();
    
    // Bersihkan data sebelumnya (jika ada)
    model.setRowCount(0);

    try {
        // Koneksi ke database
        Connection conn = GUI.mycon();
        String sql = "SELECT * FROM barang_keluar"; // Sesuaikan dengan nama tabel Anda
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        // Iterasi hasil query dan tambahkan data ke model
        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getInt("id_penjualan"), // Ganti sesuai dengan kolom tabel database Anda
                rs.getDate("tanggal_keluar"),
                rs.getString("id_barang"),
                rs.getDouble("jumlah"),
                rs.getDouble("pendapatan"),
                rs.getInt("id_customer"),
                rs.getString("status")
            });
        }

        rs.close();
        ps.close();
       
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal memuat data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        Dashboard = new javax.swing.JButton();
        DataBarang = new javax.swing.JButton();
        DataSupplier = new javax.swing.JButton();
        Laporan = new javax.swing.JButton();
        Logout = new javax.swing.JButton();
        Customer = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        TabelCustomer = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jScrollPane5 = new javax.swing.JScrollPane();
        TabelBarangMasuk = new javax.swing.JTable();
        jPanel9 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jScrollPane6 = new javax.swing.JScrollPane();
        TabelLaporan = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 51, 102));
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Selamat Datang Kembali Admin");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel7)
                .addGap(522, 522, 522))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel7)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        Dashboard.setIcon(new javax.swing.ImageIcon("C:\\Users\\Tiaraaa\\PBO\\GUI(2)\\GUI(2)\\GUI\\src\\main\\java\\com\\pbo\\gui\\image\\home_1946488.png")); // NOI18N
        Dashboard.setText("Dashboard");
        Dashboard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DashboardActionPerformed(evt);
            }
        });

        DataBarang.setIcon(new javax.swing.ImageIcon("C:\\Users\\Tiaraaa\\PBO\\GUI(2)\\GUI(2)\\GUI\\src\\main\\java\\com\\pbo\\gui\\image\\Barang.png")); // NOI18N
        DataBarang.setText("Data Barang");
        DataBarang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DataBarangActionPerformed(evt);
            }
        });

        DataSupplier.setIcon(new javax.swing.ImageIcon("C:\\Users\\Tiaraaa\\PBO\\GUI(2)\\GUI(2)\\GUI\\src\\main\\java\\com\\pbo\\gui\\image\\Supplier.png")); // NOI18N
        DataSupplier.setText("Data Suplier");
        DataSupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DataSupplierActionPerformed(evt);
            }
        });

        Laporan.setIcon(new javax.swing.ImageIcon("C:\\Users\\Tiaraaa\\PBO\\GUI(2)\\GUI(2)\\GUI\\src\\main\\java\\com\\pbo\\gui\\image\\Laporan.png")); // NOI18N
        Laporan.setText("Laporan");
        Laporan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LaporanActionPerformed(evt);
            }
        });

        Logout.setIcon(new javax.swing.ImageIcon("C:\\Users\\Tiaraaa\\PBO\\GUI(2)\\GUI(2)\\GUI\\src\\main\\java\\com\\pbo\\gui\\image\\Exit.png")); // NOI18N
        Logout.setText("Logout");
        Logout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LogoutActionPerformed(evt);
            }
        });

        Customer.setIcon(new javax.swing.ImageIcon("C:\\Users\\Tiaraaa\\PBO\\GUI(2)\\GUI(2)\\GUI\\src\\main\\java\\com\\pbo\\gui\\image\\User.png")); // NOI18N
        Customer.setText("Customer");
        Customer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CustomerActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(DataBarang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Dashboard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Logout, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Laporan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(DataSupplier, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(Customer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(Dashboard)
                .addGap(18, 18, 18)
                .addComponent(DataBarang)
                .addGap(18, 18, 18)
                .addComponent(DataSupplier)
                .addGap(18, 18, 18)
                .addComponent(Customer)
                .addGap(18, 18, 18)
                .addComponent(Laporan)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Logout)
                .addGap(16, 16, 16))
        );

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jLabel3.setText("Laporan Barang");

        TabelCustomer.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id Penjualan", "Tanggal", "Id Barang", "Jumlah", "Pendapatan", "Id Customer", "Status"
            }
        ));
        jScrollPane3.setViewportView(TabelCustomer);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 599, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(0, 515, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 526, Short.MAX_VALUE)
                .addGap(18, 18, 18))
        );

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setText("Barang Masuk");

        TabelBarangMasuk.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"PT. Teknologi Jaya", "50", "01 Nov 2024", "Stok baru", null, null, null},
                {"PT. Sumber Elektronik", "30", "05 Nov 2024", "Restock", null, null, null}
            },
            new String [] {
                "Id Supplier", "Tanggal", "Id Barang", "Nama Barang", "Kategori", "Jumlah", "Harga Beli"
            }
        ));
        jScrollPane5.setViewportView(TabelBarangMasuk);
        if (TabelBarangMasuk.getColumnModel().getColumnCount() > 0) {
            TabelBarangMasuk.getColumnModel().getColumn(5).setHeaderValue("Jumlah");
            TabelBarangMasuk.getColumnModel().getColumn(6).setHeaderValue("Harga Beli");
        }

        jScrollPane1.setViewportView(jScrollPane5);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 519, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setText("Laporan Bulanan");

        TabelLaporan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"PT. Teknologi Jaya", "50", "01 Nov 2024", "Stok baru", null, null},
                {"PT. Sumber Elektronik", "30", "05 Nov 2024", "Restock", null, null}
            },
            new String [] {
                "Periode", "Id Barang", "Nama Barang", "Pendapatan", "Pengeluaran", "Keuntungan"
            }
        ));
        jScrollPane6.setViewportView(TabelLaporan);

        jScrollPane2.setViewportView(jScrollPane6);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane2))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void DashboardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DashboardActionPerformed
        // TODO add your handling code here:
        new Dashboard().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_DashboardActionPerformed

    private void LaporanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LaporanActionPerformed
        // TODO add your handling code here:
        this.setVisible(true);
    }//GEN-LAST:event_LaporanActionPerformed

    private void LogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LogoutActionPerformed
        // TODO add your handling code here:
        new Login1().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_LogoutActionPerformed

    private void DataBarangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DataBarangActionPerformed
        // TODO add your handling code here:
        new DataBarang().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_DataBarangActionPerformed

    private void DataSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DataSupplierActionPerformed
        // TODO add your handling code here:
        new DataSupplier().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_DataSupplierActionPerformed

    private void CustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CustomerActionPerformed
        // TODO add your handling code here:
        new DataCustomer().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_CustomerActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Laporan().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Customer;
    private javax.swing.JButton Dashboard;
    private javax.swing.JButton DataBarang;
    private javax.swing.JButton DataSupplier;
    private javax.swing.JButton Laporan;
    private javax.swing.JButton Logout;
    private javax.swing.JTable TabelBarangMasuk;
    private javax.swing.JTable TabelCustomer;
    private javax.swing.JTable TabelLaporan;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    // End of variables declaration//GEN-END:variables
}
