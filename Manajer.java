/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.pbo.gui.Manajer;

import com.pbo.gui.GUI;
import com.pbo.gui.Login;
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
public class Manajer extends javax.swing.JFrame {

    /**
     * Creates new form Dashboard
     */
    public Manajer() {
        initComponents();
        updateTotalBarangMasuk();
        updateTotalBarangKeluar();
        updateTotalSupplier();
        updateTotalPendapatan();
        updateTotalPengeluaran();
        loadPenjualanTerbaru();
        updateTotalCustomer();
        loadBarangTanpaHargaJual();
        Timer timer = new Timer(2000, new ActionListener() {
       
        public void actionPerformed(ActionEvent e) {
            updateTotalBarangMasuk();
            updateTotalBarangKeluar();
            updateTotalSupplier();
            updateTotalPendapatan();
            updateTotalPengeluaran();
            loadPenjualanTerbaru();
            updateTotalCustomer();
            loadBarangTanpaHargaJual();
           
        }
    });
    
    timer.start(); // Mulai timer
        
    }
    
    private void loadBarangTanpaHargaJual() {
    DefaultTableModel model = (DefaultTableModel) TabelNotifikasi.getModel();
    model.setRowCount(0); // Bersihkan tabel

    try {
        Connection conn = GUI.mycon();
        String sql = "SELECT bm.id_masuk, bm.id_barang, bm.nama_barang, bm.jumlah, bm.harga_beli " +
                     "FROM barang_masuk bm " +
                     "LEFT JOIN hargajual hj ON bm.id_barang = hj.id_barang " +
                     "WHERE hj.id_barang IS NULL";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getInt("id_masuk"),
                rs.getString("id_barang"),
                rs.getString("nama_barang"),
                rs.getInt("jumlah"),
                rs.getDouble("harga_beli"),
                "Berikan Harga" // Kolom status
            });
        }

        rs.close();
        ps.close();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    }
    
    
    private void loadPenjualanTerbaru() {
    try {
        // Koneksi ke database
        Connection conn = GUI.mycon();
        
        // Query SQL untuk mengambil data terbaru berdasarkan tanggal_keluar
        String sql = """
            SELECT bk.tanggal_keluar, bk.id_barang, bk.jumlah, bk.pendapatan, 
                   hj.hargaunit, hj.hargagrosir
            FROM barang_keluar bk
            JOIN hargajual hj ON bk.id_barang = hj.id_barang
            ORDER BY bk.tanggal_keluar DESC
        """;
        
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        
        // Kosongkan model tabel atau area tampilan terlebih dahulu
        DefaultTableModel model = (DefaultTableModel) TabelPenjualanTerbaru.getModel();
        model.setRowCount(0); // Reset data tabel sebelumnya

        // Tambahkan data dari hasil query ke tabel
        while (rs.next()) {
            String tanggalKeluar = rs.getString("tanggal_keluar");
            String idBarang = rs.getString("id_barang");
            int jumlah = rs.getInt("jumlah");
            double pendapatan = rs.getDouble("pendapatan");
            double hargaUnit = rs.getDouble("hargaunit");
            double hargaGrosir = rs.getDouble("hargagrosir");

            // Tambahkan baris data ke tabel
            model.addRow(new Object[]{tanggalKeluar, idBarang, jumlah, pendapatan, hargaUnit, hargaGrosir});
        }

        rs.close();
        ps.close();
    } catch (Exception e) {
        // Tangani error koneksi atau query
        JOptionPane.showMessageDialog(this, "Gagal memuat data penjualan terbaru: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
    
    private void updateTotalPendapatan() {
    try {
        // Set nilai awal label menjadi 0
        LabelTotalPendapatan.setText("0");

        // Koneksi ke database
        Connection conn = GUI.mycon(); // Sesuaikan dengan metode koneksi Anda

        // Query untuk menghitung total pendapatan dari kolom pendapatan
        String sql = """
            SELECT SUM(pendapatan) AS total_pendapatan
            FROM barang_keluar
        """;

        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        // Jika ada hasil dari query
        if (rs.next()) {
            // Ambil total pendapatan
            double totalPendapatan = rs.getDouble("total_pendapatan");

            // Jika total pendapatan null (tidak ada data), set menjadi 0
            if (rs.wasNull()) {
                totalPendapatan = 0;
            }

            // Set label untuk menampilkan total pendapatan
            LabelTotalPendapatan.setText("RP: " + String.format("%.2f", totalPendapatan));
        }

        rs.close();
        ps.close();
    } catch (Exception e) {
        // Menangani error jika terjadi masalah dengan koneksi atau query
        JOptionPane.showMessageDialog(this, "Gagal memuat total pendapatan: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

}    
    
    private void updateTotalPengeluaran() {
    try {
        // Set nilai awal label menjadi 0
        LabelTotalPengeluaran.setText("0");

        // Koneksi ke database
        Connection conn = GUI.mycon(); // Sesuaikan dengan metode koneksi Anda

        // Query untuk menghitung total pendapatan dari kolom pendapatan
        String sql = """
            SELECT SUM(harga_beli) AS total_pengeluaran
            FROM barang_masuk
        """;

        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        // Jika ada hasil dari query
        if (rs.next()) {
            // Ambil total pendapatan
            double totalPengeluaran = rs.getDouble("total_pengeluaran");

            // Jika total pendapatan null (tidak ada data), set menjadi 0
            if (rs.wasNull()) {
                totalPengeluaran = 0;
            }

            // Set label untuk menampilkan total pendapatan
            LabelTotalPengeluaran.setText("RP: " + String.format("%.2f", totalPengeluaran));
        }

        rs.close();
        ps.close();
    } catch (Exception e) {
        // Menangani error jika terjadi masalah dengan koneksi atau query
        JOptionPane.showMessageDialog(this, "Gagal memuat total pendapatan: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

}    
    public void updateTotalBarangMasuk() {
        try {
            // Koneksi ke database
            Connection conn = GUI.mycon();

            // Query untuk menghitung total barang keluar
            String sql = "SELECT COUNT(*) AS totalBarangMasuk FROM barang_masuk";
            PreparedStatement ps = conn.prepareStatement(sql);

            // Eksekusi query
            ResultSet rs = ps.executeQuery();

            // Ambil hasil perhitungan
            if (rs.next()) {
                int totalBarangMasuk = rs.getInt("totalBarangMasuk"); // Total dari kolom jumlah
                if (rs.wasNull()) {
                    totalBarangMasuk = 0; // Default jika tidak ada data
                }
                // Update text JLabel
                LabelTotalBarangMasuk.setText(String.valueOf(totalBarangMasuk));
            }

            // Tutup resources
            rs.close();
            ps.close();
        } catch (Exception e) {
            // Tampilkan error di Label jika ada masalah
            LabelTotalBarangMasuk.setText("Error: " + e.getMessage());
        }
    }
    
    public void updateTotalBarangKeluar() {
        try {
            // Koneksi ke database
            Connection conn = GUI.mycon();

            // Query untuk menghitung total barang keluar
            String sql = "SELECT COUNT(*) AS totalBarangKeluar FROM barang_keluar";
            PreparedStatement ps = conn.prepareStatement(sql);

            // Eksekusi query
            ResultSet rs = ps.executeQuery();

            // Ambil hasil perhitungan
            if (rs.next()) {
                int totalBarangKeluar = rs.getInt("totalBarangKeluar"); // Total dari kolom jumlah
                if (rs.wasNull()) {
                    totalBarangKeluar = 0; // Default jika tidak ada data
                }
                // Update text JLabel
                LabelTotalBarangKeluar.setText(String.valueOf(totalBarangKeluar));
            }

            // Tutup resources
            rs.close();
            ps.close();
        } catch (Exception e) {
            // Tampilkan error di Label jika ada masalah
            LabelTotalBarangMasuk.setText("Error: " + e.getMessage());
        }
    }

public void updateTotalCustomer() {
        try {
            // Koneksi ke database
            Connection conn = GUI.mycon();

            // Query untuk menghitung total barang keluar
            String sql = "SELECT COUNT(*) AS totalCustomer FROM customer";
            PreparedStatement ps = conn.prepareStatement(sql);

            // Eksekusi query
            ResultSet rs = ps.executeQuery();

            // Ambil hasil perhitungan
            if (rs.next()) {
                int totalCustomer = rs.getInt("totalCustomer"); // Total dari kolom jumlah
                if (rs.wasNull()) {
                    totalCustomer = 0; // Default jika tidak ada data
                }
                // Update text JLabel
                LabelTotalCustomer.setText(String.valueOf(totalCustomer));
            }

            // Tutup resources
            rs.close();
            ps.close();
        } catch (Exception e) {
            // Tampilkan error di Label jika ada masalah
            LabelTotalBarangMasuk.setText("Error: " + e.getMessage());
        }
    }    
    public void updateTotalSupplier() {
        try {
            // Koneksi ke database
            Connection conn = GUI.mycon();

            // Query untuk menghitung total barang keluar
            String sql = "SELECT COUNT(*) AS totalSupplier FROM supplier";
            PreparedStatement ps = conn.prepareStatement(sql);

            // Eksekusi query
            ResultSet rs = ps.executeQuery();

            // Ambil hasil perhitungan
            if (rs.next()) {
                int totalSupplier = rs.getInt("totalSupplier"); // Total dari kolom jumlah
                if (rs.wasNull()) {
                    totalSupplier = 0; // Default jika tidak ada data
                }
                // Update text JLabel
                LabelTotalSupplier.setText(String.valueOf(totalSupplier));
            }

            // Tutup resources
            rs.close();
            ps.close();
        } catch (Exception e) {
            // Tampilkan error di Label jika ada masalah
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
        Dashboard = new javax.swing.JButton();
        HargaJual = new javax.swing.JButton();
        Laporan = new javax.swing.JButton();
        Logout = new javax.swing.JButton();
        Analisis = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        LabelTotalBarangMasuk = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jScrollPane3 = new javax.swing.JScrollPane();
        TabelNotifikasi = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TabelPenjualanTerbaru = new javax.swing.JTable();
        jPanel9 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        LabelTotalPendapatan = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        LabelTotalSupplier = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        LabelTotalBarangKeluar = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        LabelTotalCustomer = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        LabelTotalPengeluaran = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 51, 102));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Selamat Datang Kembali Manajer");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(393, 393, 393)
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

        Dashboard.setIcon(new javax.swing.ImageIcon("C:\\Users\\Tiaraaa\\PBO\\GUI(2)\\GUI(2)\\GUI\\src\\main\\java\\com\\pbo\\gui\\image\\home_1946488.png")); // NOI18N
        Dashboard.setText("Dashboard");
        Dashboard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DashboardActionPerformed(evt);
            }
        });

        HargaJual.setIcon(new javax.swing.ImageIcon("C:\\Users\\Tiaraaa\\PBO\\GUI(2)\\GUI(2)\\GUI\\src\\main\\java\\com\\pbo\\gui\\image\\Icon_Price Tag.png")); // NOI18N
        HargaJual.setText("Harga Jual");
        HargaJual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HargaJualActionPerformed(evt);
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

        Analisis.setIcon(new javax.swing.ImageIcon("C:\\Users\\Tiaraaa\\PBO\\GUI(2)\\GUI(2)\\GUI\\src\\main\\java\\com\\pbo\\gui\\image\\Analisis.png")); // NOI18N
        Analisis.setText("Analisis");
        Analisis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AnalisisActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(Analisis, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(HargaJual, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Dashboard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Logout, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Laporan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(29, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(Dashboard)
                .addGap(18, 18, 18)
                .addComponent(HargaJual)
                .addGap(18, 18, 18)
                .addComponent(Analisis)
                .addGap(18, 18, 18)
                .addComponent(Laporan)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Logout)
                .addContainerGap())
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
                .addContainerGap(45, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(32, 32, 32))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(LabelTotalBarangMasuk)
                        .addGap(75, 75, 75))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(LabelTotalBarangMasuk)
                .addContainerGap(55, Short.MAX_VALUE))
        );

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));

        jLabel6.setText("Membutuhkan Penetapan Harga");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setText("Note: ACC Harga di Menu Harga Jual");

        TabelNotifikasi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"BRG-004", "Modem D-Link", "Jaringan", "5 pcs", null, null},
                {"BRG-005", "Kabel Fiber Optik", "Aksesoris", "	8 pcs", null, null},
                {"BRG-006", "Patch Panel", "Jaringan", "3 pcs", null, null}
            },
            new String [] {
                "Id Masuk", "Id Barang", "Nama Barang", "Jumlah", "Harga Beli", "Status"
            }
        ));
        jScrollPane3.setViewportView(TabelNotifikasi);

        jScrollPane2.setViewportView(jScrollPane3);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel6))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));

        jLabel5.setText("Penjualan Terbaru");

        TabelPenjualanTerbaru.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"12 Nov 2024", "Kabel LAN", "20", "20000", "400000", null},
                {"13 Nov 2024", "Switch TP-Link", "10", "400000", "4000000", null},
                {"14 Nov 2024", "ESP 32", "30", "50000", "1500000", null},
                {"15 Nov 2024", "Router Cisco", "10", "500000", "5000000", null}
            },
            new String [] {
                "Tanggal Penjualan", "Id Barang", "Jumlah Terjual", "Pendapatan", "Harga Unit", "Harga Grosir"
            }
        ));
        jScrollPane1.setViewportView(TabelPenjualanTerbaru);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 681, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));

        jLabel8.setText("Total Pendapatan");

        LabelTotalPendapatan.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        LabelTotalPendapatan.setText("0");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(LabelTotalPendapatan)
                    .addComponent(jLabel8))
                .addContainerGap(38, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel8)
                .addGap(18, 18, 18)
                .addComponent(LabelTotalPendapatan)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));

        jLabel12.setText("Total Supplier");

        LabelTotalSupplier.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        LabelTotalSupplier.setText("0");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(LabelTotalSupplier)))
                .addContainerGap(41, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel12)
                .addGap(18, 18, 18)
                .addComponent(LabelTotalSupplier)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setText("Total Barang Keluar");

        LabelTotalBarangKeluar.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        LabelTotalBarangKeluar.setText("0");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap(45, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(32, 32, 32))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(LabelTotalBarangKeluar)
                        .addGap(72, 72, 72))))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(LabelTotalBarangKeluar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        jLabel3.setText("Total Customer");

        LabelTotalCustomer.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        LabelTotalCustomer.setText("0");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addComponent(jLabel3))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(68, 68, 68)
                        .addComponent(LabelTotalCustomer)))
                .addContainerGap(42, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(LabelTotalCustomer)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));

        jLabel9.setText("Total Pengeluaran");

        LabelTotalPengeluaran.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        LabelTotalPengeluaran.setText("0");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap(42, Short.MAX_VALUE)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(LabelTotalPengeluaran)
                    .addComponent(jLabel9))
                .addGap(28, 28, 28))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel9)
                .addGap(18, 18, 18)
                .addComponent(LabelTotalPengeluaran)
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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void DashboardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DashboardActionPerformed
        // TODO add your handling code here:
        this.setVisible(true);
    }//GEN-LAST:event_DashboardActionPerformed

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

    private void HargaJualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HargaJualActionPerformed
        // TODO add your handling code here:
        new HargaJual().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_HargaJualActionPerformed

    private void AnalisisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AnalisisActionPerformed
        // TODO add your handling code here:
        new Analisis().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_AnalisisActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Manajer().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Analisis;
    private javax.swing.JButton Dashboard;
    private javax.swing.JButton HargaJual;
    private javax.swing.JLabel LabelTotalBarangKeluar;
    private javax.swing.JLabel LabelTotalBarangMasuk;
    private javax.swing.JLabel LabelTotalCustomer;
    private javax.swing.JLabel LabelTotalPendapatan;
    private javax.swing.JLabel LabelTotalPengeluaran;
    private javax.swing.JLabel LabelTotalSupplier;
    private javax.swing.JButton Laporan;
    private javax.swing.JButton Logout;
    private javax.swing.JTable TabelNotifikasi;
    private javax.swing.JTable TabelPenjualanTerbaru;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    // End of variables declaration//GEN-END:variables
}
