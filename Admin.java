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
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author asus
 */
public class Admin extends javax.swing.JFrame {

    /**
     * Creates new form Dashboard
     */
    public Admin() {
        initComponents();
        loadData();
        loadKategori();
        loadPenjualanTerbaru();
        updateTotalBarangMasuk();
        updateTotalBarangKeluar();
        updateTotalSupplier();
        Timer timer = new Timer(2000, new ActionListener() {
       
        public void actionPerformed(ActionEvent e) {
            loadData();
            loadPenjualanTerbaru();
            updateTotalBarangMasuk();
            updateTotalBarangKeluar();
            updateTotalSupplier();
            loadKategori();
        }
        });
        timer.start(); // Mulai timer
    }
    
    private void loadData() {
        DefaultTableModel model = (DefaultTableModel) TabelBarang.getModel();
        model.setRowCount(0);
        try {
            Connection conn = GUI.mycon();
            String sql = """
            SELECT bm.id_supplier, bm.tanggal_masuk, bm.id_barang, bm.nama_barang, 
               bm.harga_beli, bm.kategori, bm.jumlah, hj.hargaunit, hj.hargagrosir
            FROM barang_masuk bm
            LEFT JOIN hargajual hj ON bm.id_barang = hj.id_barang
            """;
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id_supplier"), // Ganti sesuai dengan kolom tabel database Anda
                    rs.getDate("tanggal_masuk"),
                    rs.getString("id_barang"),
                    rs.getString("nama_barang"),
                    rs.getString("kategori"),
                    rs.getString("jumlah"),
                    rs.getDouble("harga_beli"),
                    rs.getDouble("hargaunit"),
                    rs.getDouble("hargagrosir")
                });
            }

            rs.close();
            ps.close();
       
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
private void loadKategori() {
    DefaultTableModel model = (DefaultTableModel) TabelKategori.getModel();
    model.setRowCount(0); // Bersihkan data tabel sebelumnya
    try {
        // Koneksi ke database
        Connection conn = GUI.mycon();

        // SQL untuk mengelompokkan hanya berdasarkan kategori
        String sql = """
        SELECT bm.kategori, SUM(bm.jumlah) AS total_jumlah
        FROM barang_masuk bm
        GROUP BY bm.kategori
        ORDER BY bm.kategori
        """;

        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        // Iterasi hasil query dan tambahkan data ke model
        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getString("kategori"),         // Nama Kategori
                rs.getInt("total_jumlah")         // Total Jumlah untuk kategori tersebut
            });
        }

        rs.close();
        ps.close();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal memuat data kategori: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}

    private void loadPenjualanTerbaru() {
        try {
            Connection conn = GUI.mycon();
            String sql = """
                SELECT bk.tanggal_keluar, bk.id_barang, bk.jumlah, bk.pendapatan, 
                hj.hargaunit, hj.hargagrosir
                FROM barang_keluar bk
                JOIN hargajual hj ON bk.id_barang = hj.id_barang
                ORDER BY bk.tanggal_keluar DESC
                """;
        
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
       
            DefaultTableModel model = (DefaultTableModel) TabelPenjualanTerbaru.getModel();
            model.setRowCount(0); 

            while (rs.next()) {
                String tanggalKeluar = rs.getString("tanggal_keluar");
                String idBarang = rs.getString("id_barang");
                int jumlah = rs.getInt("jumlah");
                double pendapatan = rs.getDouble("pendapatan");
                double hargaUnit = rs.getDouble("hargaunit");
                double hargaGrosir = rs.getDouble("hargagrosir");

                model.addRow(new Object[]{tanggalKeluar, idBarang, jumlah, pendapatan, hargaUnit, hargaGrosir});
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data penjualan terbaru: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
 
    public void updateTotalBarangMasuk() {
        try {
            Connection conn = GUI.mycon();

            String sql = "SELECT COUNT(*) AS totalBarangMasuk FROM barang_masuk";
            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int totalBarangMasuk = rs.getInt("totalBarangMasuk"); 
                if (rs.wasNull()) {
                    totalBarangMasuk = 0; 
                }
                
                LabelTotalBarangMasuk.setText(String.valueOf(totalBarangMasuk));
            }

            
            rs.close();
            ps.close();
        } catch (Exception e) {
            LabelTotalBarangMasuk.setText("Error: " + e.getMessage());
        }
    }
    
    public void updateTotalBarangKeluar() {
        try {
            Connection conn = GUI.mycon();

            String sql = "SELECT COUNT(*) AS totalBarangKeluar FROM barang_keluar";
            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int totalBarangKeluar = rs.getInt("totalBarangKeluar");
                if (rs.wasNull()) {
                    totalBarangKeluar = 0;
                }
                LabelTotalBarangKeluar.setText(String.valueOf(totalBarangKeluar));
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            LabelTotalBarangMasuk.setText("Error: " + e.getMessage());
        }
    }
    
    public void updateTotalSupplier() {
        try {
            Connection conn = GUI.mycon();

            String sql = "SELECT COUNT(*) AS totalSupplier FROM supplier";
            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int totalSupplier = rs.getInt("totalSupplier");
                if (rs.wasNull()) {
                    totalSupplier = 0; 
                }
                LabelTotalSupplier.setText(String.valueOf(totalSupplier));
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            LabelTotalBarangMasuk.setText("Error: " + e.getMessage());
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
        Dashbord = new javax.swing.JButton();
        DataBarang = new javax.swing.JButton();
        Supplier = new javax.swing.JButton();
        Laporan = new javax.swing.JButton();
        Logout = new javax.swing.JButton();
        Customer = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        LabelTotalBarangMasuk = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TabelPenjualanTerbaru = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        LabelTotalBarangKeluar = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jScrollPane5 = new javax.swing.JScrollPane();
        TabelBarang = new javax.swing.JTable();
        jPanel9 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        LabelTotalSupplier = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        TabelKategori = new javax.swing.JTable();

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
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(502, 502, 502)
                .addComponent(jLabel7)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel7)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        Dashbord.setIcon(new javax.swing.ImageIcon("C:\\Users\\Tiaraaa\\PBO\\GUI(2)\\GUI(2)\\GUI\\src\\main\\java\\com\\pbo\\gui\\image\\home_1946488.png")); // NOI18N
        Dashbord.setText("Dashboard");
        Dashbord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DashbordActionPerformed(evt);
            }
        });

        DataBarang.setIcon(new javax.swing.ImageIcon("C:\\Users\\Tiaraaa\\PBO\\GUI(2)\\GUI(2)\\GUI\\src\\main\\java\\com\\pbo\\gui\\image\\Barang.png")); // NOI18N
        DataBarang.setText("Data Barang");
        DataBarang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DataBarangActionPerformed(evt);
            }
        });

        Supplier.setIcon(new javax.swing.ImageIcon("C:\\Users\\Tiaraaa\\PBO\\GUI(2)\\GUI(2)\\GUI\\src\\main\\java\\com\\pbo\\gui\\image\\Supplier.png")); // NOI18N
        Supplier.setText("Data Suplier");
        Supplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SupplierActionPerformed(evt);
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
                            .addComponent(Dashbord, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Logout, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Laporan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Supplier, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(Customer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(Dashbord)
                .addGap(18, 18, 18)
                .addComponent(DataBarang)
                .addGap(18, 18, 18)
                .addComponent(Supplier)
                .addGap(18, 18, 18)
                .addComponent(Customer)
                .addGap(18, 18, 18)
                .addComponent(Laporan)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Logout)
                .addGap(15, 15, 15))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setText("Total Barang Masuk");

        LabelTotalBarangMasuk.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        LabelTotalBarangMasuk.setText("0");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(41, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(32, 32, 32))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(74, 74, 74)
                .addComponent(LabelTotalBarangMasuk)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LabelTotalBarangMasuk)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel9.setText("Penjualan Terbaru");

        TabelPenjualanTerbaru.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"12 Nov 2024", "Kabel LAN", "20", "20000"},
                {"13 Nov 2024", "Switch TP-Link", "10", "400000"},
                {"14 Nov 2024", "ESP 32", "30", "50000"},
                {"15 Nov 2024", "Router Cisco", "10", "500000"}
            },
            new String [] {
                "Tanggal Penjualan", "Id Barang", "Jumlah Terjual", "Pendapatan"
            }
        ));
        jScrollPane1.setViewportView(TabelPenjualanTerbaru);

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel9)
                .addGap(601, 601, 601))
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 505, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        jLabel4.setText("Total Barang Keluar");

        LabelTotalBarangKeluar.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        LabelTotalBarangKeluar.setText("0");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(33, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addGap(30, 30, 30))
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(65, 65, 65)
                .addComponent(LabelTotalBarangKeluar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LabelTotalBarangKeluar)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setText("Data Barang");

        TabelBarang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"PT. Teknologi Jaya", "50", "01 Nov 2024", null, null, null, null, null},
                {"PT. Sumber Elektronik", "30", "05 Nov 2024", null, null, null, null, null}
            },
            new String [] {
                "Id Supplier", "Tanggal", "Id Barang", "Kategori", "Jumlah", "Harga Beli", "Harga Unit", "Harga Grosir"
            }
        ));
        jScrollPane5.setViewportView(TabelBarang);
        if (TabelBarang.getColumnModel().getColumnCount() > 0) {
            TabelBarang.getColumnModel().getColumn(2).setHeaderValue("Id Barang");
            TabelBarang.getColumnModel().getColumn(5).setHeaderValue("Harga Beli");
            TabelBarang.getColumnModel().getColumn(6).setHeaderValue("Harga Unit");
            TabelBarang.getColumnModel().getColumn(7).setHeaderValue("Harga Grosir");
        }

        jScrollPane2.setViewportView(jScrollPane5);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));

        jLabel8.setText("Total Supplier");

        LabelTotalSupplier.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        LabelTotalSupplier.setText("0");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addComponent(jLabel8)
                .addContainerGap(42, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(LabelTotalSupplier)
                .addGap(63, 63, 63))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LabelTotalSupplier)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setText("Kategori Barang");

        TabelKategori.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null}
            },
            new String [] {
                "Kategori", "Jumlah"
            }
        ));
        jScrollPane6.setViewportView(TabelKategori);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 626, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void DashbordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DashbordActionPerformed
        // TODO add your handling code here:
        this.setVisible(true);
    }//GEN-LAST:event_DashbordActionPerformed

    private void LaporanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LaporanActionPerformed
        // TODO add your handling code here:
        new Laporan().setVisible(true);
        this.dispose();
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

    private void SupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SupplierActionPerformed
        // TODO add your handling code here:
        new DataSupplier().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_SupplierActionPerformed

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
                new Admin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Customer;
    private javax.swing.JButton Dashbord;
    private javax.swing.JButton DataBarang;
    private javax.swing.JLabel LabelTotalBarangKeluar;
    private javax.swing.JLabel LabelTotalBarangMasuk;
    private javax.swing.JLabel LabelTotalSupplier;
    private javax.swing.JButton Laporan;
    private javax.swing.JButton Logout;
    private javax.swing.JButton Supplier;
    private javax.swing.JTable TabelBarang;
    private javax.swing.JTable TabelKategori;
    private javax.swing.JTable TabelPenjualanTerbaru;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    // End of variables declaration//GEN-END:variables
}
