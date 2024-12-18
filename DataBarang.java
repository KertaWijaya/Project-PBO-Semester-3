/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.pbo.gui.Admin;
import com.pbo.gui.GUI;
import com.pbo.gui.Login1;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author asus
 */
public class DataBarang extends javax.swing.JFrame {

    /**
     * Creates new form Dashboard
     */
    public DataBarang() {
        initComponents();
        loadData();
        loadNamaBarang();
        loadDataCustomer();
        Timer timer = new Timer(2000, new ActionListener() {
       
        public void actionPerformed(ActionEvent e) {
          
            loadDataCustomer();
        }
    });
        timer.start();
         
    }
    
    private void loadDataCustomer() {
        DefaultTableModel model = (DefaultTableModel) TabelTransaksi.getModel();

        model.setRowCount(0);

        try {
            Connection conn = GUI.mycon();
            String sql = "SELECT id_penjualan, tanggal_keluar, id_barang, jumlah, pendapatan, id_customer, status FROM barang_keluar";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id_penjualan"),         
                    rs.getDate("tanggal_keluar"),    
                    rs.getString("id_barang"),         
                    rs.getInt("jumlah"),               
                    rs.getDouble("pendapatan"),        
                    rs.getInt("id_customer"),        
                    rs.getString("status")             
                });
            }

        rs.close();
        ps.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data barang: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadData() {
        DefaultTableModel model = (DefaultTableModel) TabelBarangMasuk.getModel();
        model.setRowCount(0);

        try {
            Connection conn = GUI.mycon();
            String sql = "SELECT * FROM barang_masuk"; // Sesuaikan dengan nama tabel Anda
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
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
            JOptionPane.showMessageDialog(this, "Gagal memuat data barang masuk: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadNamaBarang() {
        try {
            Connection conn = GUI.mycon();
      
            String sql = "SELECT DISTINCT nama_barang FROM barang_masuk"; 
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            NamaBarang.removeAllItems();

            while (rs.next()) {
                NamaBarang.addItem(rs.getString("nama_barang"));
            }

        rs.close();
        ps.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat nama barang: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private String getSelectedBarangId() {
        String namaDipilih = (String) NamaBarang.getSelectedItem();
        String idBarang = null;

        try {
            Connection conn = GUI.mycon();
            String sql = "SELECT id_barang FROM barang_masuk WHERE nama_barang = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, namaDipilih);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                idBarang = rs.getString("id_barang");
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        return idBarang;
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
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        Cari = new javax.swing.JTextField();
        ButtonSearch = new javax.swing.JButton();
        ButtonReset = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jScrollPane5 = new javax.swing.JScrollPane();
        TabelBarangMasuk = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        Deskripsi = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        ButtonLaporan = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        NamaBarang = new javax.swing.JComboBox<>();
        jLabel19 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        TabelTransaksi = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 51, 102));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Selamat Datang Kembali Admin");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(432, 432, 432)
                .addComponent(jLabel7)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel7)
                .addContainerGap(20, Short.MAX_VALUE))
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
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(Customer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(DataBarang, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Dashboard, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Logout, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(DataSupplier, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Laporan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(19, Short.MAX_VALUE))
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
                .addGap(19, 19, 19))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("Data Barang");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel9.setText("Cari Barang:");

        ButtonSearch.setIcon(new javax.swing.ImageIcon("C:\\Users\\Tiaraaa\\PBO\\GUI(2)\\GUI(2)\\GUI\\src\\main\\java\\com\\pbo\\gui\\image\\Search.png")); // NOI18N
        ButtonSearch.setText("Search");
        ButtonSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonSearchActionPerformed(evt);
            }
        });

        ButtonReset.setText("Reset");
        ButtonReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonResetActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(ButtonSearch)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(ButtonReset))
                            .addComponent(Cari, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(33, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel1)
                .addGap(27, 27, 27)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(Cari, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ButtonSearch)
                    .addComponent(ButtonReset, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(47, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        TabelBarangMasuk.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"Router A", "PT. Teknologi Jaya", "50", "01 Nov 2024", "Stok baru", null, null, null},
                {"Modem X", "PT. Sumber Elektronik", "30", "05 Nov 2024", "Restock", null, null, null}
            },
            new String [] {
                "Id Masuk", "Id Supplier", "Tanggal", "Id Barang", "Nama Barang", "Kategori", "Jumlah", "Harga Beli"
            }
        ));
        jScrollPane5.setViewportView(TabelBarangMasuk);

        jScrollPane1.setViewportView(jScrollPane5);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 558, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));

        Deskripsi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeskripsiActionPerformed(evt);
            }
        });

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel18.setText("Nama Barang:");

        ButtonLaporan.setIcon(new javax.swing.ImageIcon("C:\\Users\\Tiaraaa\\PBO\\GUI(2)\\GUI(2)\\GUI\\src\\main\\java\\com\\pbo\\gui\\image\\Icon_Save.png")); // NOI18N
        ButtonLaporan.setText("Save");
        ButtonLaporan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonLaporanActionPerformed(evt);
            }
        });

        jLabel20.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel20.setText("Deskripsi:");

        NamaBarang.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel19.setText("Laporan Barang");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel20)
                        .addGap(49, 49, 49)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Deskripsi, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ButtonLaporan)))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addGap(18, 18, 18)
                        .addComponent(NamaBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel19))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jLabel19)
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(NamaBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Deskripsi, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20))
                .addGap(18, 18, 18)
                .addComponent(ButtonLaporan)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setText("Riwayat Transaksi");

        TabelTransaksi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id Penjualan", "Tanggal", "Id  Barang", "Jumlah", "Pendapatan", "Id Customer", "Status"
            }
        ));
        jScrollPane2.setViewportView(TabelTransaksi);

        jScrollPane3.setViewportView(jScrollPane2);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 558, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap(12, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void LogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LogoutActionPerformed
        // TODO add your handling code here:
        new Login1().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_LogoutActionPerformed

    private void LaporanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LaporanActionPerformed
        // TODO add your handling code here:
        new Laporan().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_LaporanActionPerformed

    private void DashboardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DashboardActionPerformed
        // TODO add your handling code here:
        new Dashboard().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_DashboardActionPerformed

    private void DataBarangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DataBarangActionPerformed
        // TODO add your handling code here:
        this.setVisible(true);
    }//GEN-LAST:event_DataBarangActionPerformed

    private void DataSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DataSupplierActionPerformed
        // TODO add your handling code here:
        new DataSupplier().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_DataSupplierActionPerformed

    private void DeskripsiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeskripsiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_DeskripsiActionPerformed

    private void ButtonLaporanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonLaporanActionPerformed
        // TODO add your handling code here:
        String idBarang1 = getSelectedBarangId(); // ID Barang dari ComboBox
        String deskripsi = Deskripsi.getText(); // Harga per unit
        if (idBarang1 == null || deskripsi.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
            return; // Hentikan proses jika ada field kosong
        }

        try {
            Connection conn = GUI.mycon();
            String sql = """
            INSERT INTO laporan_barang (id_barang, deskripsi)
            VALUES (?, ?)
            """;
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, idBarang1);
            ps.setString(2, deskripsi);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data laporan berhasil disimpan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            NamaBarang.setSelectedIndex(0);
            Deskripsi.setText("");

            loadData();
            ps.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Tidak valid!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_ButtonLaporanActionPerformed

    private void ButtonSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonSearchActionPerformed
        // TODO add your handling code here:
        String cari = Cari.getText().trim();
        if (cari.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Masukkan kata kunci untuk mencari!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        DefaultTableModel model = (DefaultTableModel) TabelBarangMasuk.getModel();

        model.setRowCount(0);

        try {
            Connection conn = GUI.mycon();
            String sql = "SELECT * FROM barang_masuk WHERE id_barang LIKE ? OR nama_barang LIKE ?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, "%" + cari + "%");
            ps.setString(2, "%" + cari + "%");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id_masuk"),
                    rs.getInt("id_supplier"),
                    rs.getString("tanggal_masuk"),
                    rs.getString("id_barang"),
                    rs.getString("nama_barang"),
                    rs.getString("kategori"),
                    rs.getInt("jumlah"),
                    rs.getDouble("harga_beli")
                });
            }

            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Data tidak ditemukan!", "Pencarian Gagal", JOptionPane.INFORMATION_MESSAGE);
            }

        rs.close();
        ps.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_ButtonSearchActionPerformed

    private void ButtonResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonResetActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) TabelBarangMasuk.getModel();

    // Bersihkan data sebelumnya (jika ada)
    model.setRowCount(0);

    try {
        // Koneksi ke database
        Connection conn = GUI.mycon();
        
        // Query SQL untuk mengambil semua data dari tabel barang_masuk
        String sql = "SELECT * FROM barang_masuk";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        // Iterasi hasil query dan tambahkan data ke model tabel
        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getInt("id_masuk"), // Sesuaikan dengan kolom tabel Anda
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
        // Tampilkan error jika terjadi masalah
        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_ButtonResetActionPerformed

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
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DataBarang().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ButtonLaporan;
    private javax.swing.JButton ButtonReset;
    private javax.swing.JButton ButtonSearch;
    private javax.swing.JTextField Cari;
    private javax.swing.JButton Customer;
    private javax.swing.JButton Dashboard;
    private javax.swing.JButton DataBarang;
    private javax.swing.JButton DataSupplier;
    private javax.swing.JTextField Deskripsi;
    private javax.swing.JButton Laporan;
    private javax.swing.JButton Logout;
    private javax.swing.JComboBox<String> NamaBarang;
    private javax.swing.JTable TabelBarangMasuk;
    private javax.swing.JTable TabelTransaksi;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane5;
    // End of variables declaration//GEN-END:variables
}
