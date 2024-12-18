/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.pbo.gui.Manajer;

import com.pbo.gui.Staff.*;
import com.pbo.gui.Admin.*;
import com.pbo.gui.GUI;
import com.pbo.gui.Login;
import com.pbo.gui.Login1;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author asus
 */
public class Analisis extends javax.swing.JFrame {

    /**
     * Creates new form Dashboard
     */
    public Analisis() {
        initComponents();
        loadRasio();
        loadLaporan();
        loadData();
        loadBarangTerlaris();
        Timer timer = new Timer(2000, new ActionListener() {
       
        public void actionPerformed(ActionEvent e) {
        loadRasio();
        loadBarangTerlaris();
        }
    });
    
    timer.start(); // Mulai timer
    }
    
    
private void loadData() {
    // Ambil model dari TabelData
    DefaultTableModel model = (DefaultTableModel) TabelCustomer.getModel();

// Bersihkan data sebelumnya (jika ada)
model.setRowCount(0);

try {
    // Koneksi ke database
    Connection conn = GUI.mycon();

    // SQL Query dengan JOIN tabel customer dan barang_keluar
    String sql = "SELECT c.id_customer, c.nama, c.kontak, c.alamat, c.tanggal_bergabung, " +
                 "c.jenis_customer, COALESCE(SUM(bk.pendapatan), 0) AS total_pendapatan " +
                 "FROM customer c " +
                 "LEFT JOIN barang_keluar bk ON c.id_customer = bk.id_customer " +
                 "GROUP BY c.id_customer, c.nama, c.kontak, c.alamat, c.tanggal_bergabung, c.jenis_customer";

    PreparedStatement ps = conn.prepareStatement(sql);
    ResultSet rs = ps.executeQuery();

    // Iterasi hasil query dan tambahkan data ke model
    while (rs.next()) {
        model.addRow(new Object[]{
            rs.getInt("id_customer"),       // ID Customer
            rs.getString("nama"),           // Nama
            rs.getString("kontak"),         // Kontak
            rs.getString("alamat"),         // Alamat
            rs.getDate("tanggal_bergabung"),// Tanggal Bergabung
            rs.getString("jenis_customer"), // Jenis Customer
            rs.getDouble("total_pendapatan")// Total Pendapatan dari barang_keluar
        });
    }

    rs.close();
    ps.close();

} catch (Exception e) {
    JOptionPane.showMessageDialog(this, "Gagal memuat data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
}

}
    
private void loadBarangTerlaris() {
    DefaultTableModel model = (DefaultTableModel) TabelBarangTerlaris.getModel();

model.setRowCount(0);

try {
    // Koneksi ke database
    Connection conn = GUI.mycon();
    String sql = "SELECT * FROM barang_keluar WHERE pendapatan > 5000000"; // Sesuaikan dengan nama tabel Anda
    PreparedStatement ps = conn.prepareStatement(sql);
    ResultSet rs = ps.executeQuery();

    // Iterasi hasil query dan tambahkan data ke model
    while (rs.next()) {
        // Tambahkan data ke tabel
        model.addRow(new Object[]{
            rs.getString("id_barang"),        // ID Barang
            rs.getDouble("jumlah"),          // Jumlah
            rs.getDouble("pendapatan"),      // Pendapatan
            rs.getString("tanggal_keluar")   // Tanggal Keluar langsung dari database
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

private void loadRasio() {
    // Ambil model dari TabelData
    DefaultTableModel model = (DefaultTableModel) TabelRasio.getModel();

// Bersihkan data sebelumnya (jika ada)
model.setRowCount(0);

try {
    // Koneksi ke database
    Connection conn = GUI.mycon();

    // SQL Query: Join tabel barang_masuk dan barang_keluar berdasarkan id_barang
    String sql = "SELECT bm.id_barang, bm.nama_barang, " +
                 "SUM(bm.jumlah) AS jumlah_masuk, " +
                 "SUM(bk.jumlah) AS jumlah_keluar " +
                 "FROM barang_masuk bm " +
                 "LEFT JOIN barang_keluar bk ON bm.id_barang = bk.id_barang " +
                 "GROUP BY bm.id_barang, bm.nama_barang";
    PreparedStatement ps = conn.prepareStatement(sql);
    ResultSet rs = ps.executeQuery();

    // Iterasi hasil query dan tambahkan data ke model
    while (rs.next()) {
        int jumlahMasuk = rs.getInt("jumlah_masuk");
        int jumlahKeluar = rs.getInt("jumlah_keluar");
        double rasio = (jumlahKeluar > 0) ? (double) jumlahMasuk / jumlahKeluar : 0; // Rasio dihitung

        model.addRow(new Object[]{
            rs.getString("id_barang"),  // ID Barang
            rs.getString("nama_barang"), // Nama Barang
            jumlahMasuk,                // Jumlah Masuk
            jumlahKeluar,               // Jumlah Keluar
            String.format("%.2f", rasio) // Rasio (diformat dengan 2 desimal)
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
        HargaJual = new javax.swing.JButton();
        Laporan = new javax.swing.JButton();
        Logout = new javax.swing.JButton();
        Analisis = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        TabelLaporan = new javax.swing.JTable();
        Periode = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        ButtonSearch = new javax.swing.JButton();
        ButtonReset = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        TabelBarangTerlaris = new javax.swing.JTable();
        Periode1 = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        ButtonSearch1 = new javax.swing.JButton();
        ButtonReset1 = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jScrollPane5 = new javax.swing.JScrollPane();
        TabelRasio = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        Periode2 = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        ButtonSearch2 = new javax.swing.JButton();
        ButtonReset2 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        TabelCustomer = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 51, 102));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Selamat Datang Kembali Manager");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(517, 517, 517)
                .addComponent(jLabel7)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel7)
                .addContainerGap(17, Short.MAX_VALUE))
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
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(Analisis, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(HargaJual, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Dashboard, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Logout, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 352, Short.MAX_VALUE)
                .addComponent(Logout)
                .addContainerGap())
        );

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setText("Laporan Barang");

        TabelLaporan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"PT. Teknologi Jaya", "50", "01 Nov 2024", "Stok baru", null, null},
                {"PT. Sumber Elektronik", "30", "05 Nov 2024", "Restock", null, null}
            },
            new String [] {
                "Periode", "Id Barang", "Nama Barang", "Pendapatan", "Pengeluaran", "Keuntungan"
            }
        ));
        jScrollPane7.setViewportView(TabelLaporan);

        Periode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PeriodeActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel14.setText("Periode:");

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

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Periode, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ButtonSearch)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ButtonReset)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane7))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addGap(20, 20, 20)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Periode, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addComponent(ButtonSearch)
                    .addComponent(ButtonReset))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setText("Barang Terlaris");

        TabelBarangTerlaris.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"Switch Z", "5", "3,750,000", "Oktober 2024"},
                {"Kabel LAN", "2", "200,000", "Oktober 2024"}
            },
            new String [] {
                "Id Barang", "Jumlah", "Pendapatan", "Periode"
            }
        ));
        jScrollPane6.setViewportView(TabelBarangTerlaris);
        if (TabelBarangTerlaris.getColumnModel().getColumnCount() > 0) {
            TabelBarangTerlaris.getColumnModel().getColumn(1).setResizable(false);
        }

        Periode1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Periode1ActionPerformed(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel15.setText("Periode");

        ButtonSearch1.setIcon(new javax.swing.ImageIcon("C:\\Users\\Tiaraaa\\PBO\\GUI(2)\\GUI(2)\\GUI\\src\\main\\java\\com\\pbo\\gui\\image\\Search.png")); // NOI18N
        ButtonSearch1.setText("Search");
        ButtonSearch1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonSearch1ActionPerformed(evt);
            }
        });

        ButtonReset1.setText("Reset");
        ButtonReset1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonReset1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane6)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel15)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Periode1, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ButtonSearch1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ButtonReset1)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Periode1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15)
                    .addComponent(ButtonSearch1)
                    .addComponent(ButtonReset1))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setText("Rasio Stok");

        TabelRasio.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"01 Nov 2024", "Stok baru", null, null, null},
                {"05 Nov 2024", "Restock", null, null, null}
            },
            new String [] {
                "Id Barang", "Nama Barang", "Jumlah Masuk", "Jumlah Keluar", "Rasio"
            }
        ));
        jScrollPane5.setViewportView(TabelRasio);

        jScrollPane1.setViewportView(jScrollPane5);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setText("Data Customer");

        Periode2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Periode2ActionPerformed(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel16.setText("Periode");

        ButtonSearch2.setIcon(new javax.swing.ImageIcon("C:\\Users\\Tiaraaa\\PBO\\GUI(2)\\GUI(2)\\GUI\\src\\main\\java\\com\\pbo\\gui\\image\\Search.png")); // NOI18N
        ButtonSearch2.setText("Search");
        ButtonSearch2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonSearch2ActionPerformed(evt);
            }
        });

        ButtonReset2.setText("Reset");
        ButtonReset2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonReset2ActionPerformed(evt);
            }
        });

        TabelCustomer.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"SUP-001", "PT. ABC Supplier", "08123456789", "Jl. Merdeka No.1", "abc@supplier.com", "Aktif", null},
                {"SUP-002", "CV. XYZ Jaya", "08987654321", "Jl. Sudirman No.5", "xyz@jaya.com", "Tidak Aktif", null},
                {"SUP-003", "Toko Hardware A", "08190876543", "Jl. Pahlawan No.3", "hardware@toko.com	", "Aktif", null}
            },
            new String [] {
                "ID Customer", "Nama", "Kontak", "Alamat", "Tanggal", "Jenis", "Pendapatan"
            }
        ));
        jScrollPane2.setViewportView(TabelCustomer);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Periode2, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ButtonSearch2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ButtonReset2))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 639, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel5)
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Periode2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16)
                    .addComponent(ButtonSearch2)
                    .addComponent(ButtonReset2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
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
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void DashboardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DashboardActionPerformed
        // TODO add your handling code here:
        new Dashboard().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_DashboardActionPerformed

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

    private void LaporanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LaporanActionPerformed
        // TODO add your handling code here:
        new Laporan().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_LaporanActionPerformed

    private void PeriodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PeriodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_PeriodeActionPerformed

    private void ButtonSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonSearchActionPerformed
      String periode = Periode.getText().trim();  // Misalnya "2024-01", "2024", atau "01"
    
    // Validasi format input periode
    if (!periode.matches("^\\d{4}-\\d{2}$") && !periode.matches("^\\d{4}$") && !periode.matches("^\\d{2}$")) {
        JOptionPane.showMessageDialog(this, "Format periode tidak valid. Gunakan format YYYY-MM, YYYY, atau MM.", "Peringatan", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    // Ambil model dari tabel laporan
    DefaultTableModel model = (DefaultTableModel) TabelLaporan.getModel();

    // Bersihkan data sebelumnya (jika ada)
    model.setRowCount(0);

    // Koneksi ke database
    try {
        Connection conn = GUI.mycon();

        // SQL untuk laporan dengan pengecekan berdasarkan periode
        String sql = "SELECT bm.tanggal_masuk, bm.id_barang, bm.nama_barang, " +
                     "SUM(bk.pendapatan) AS total_pendapatan, " +
                     "SUM(bm.harga_beli) AS total_pengeluaran " +
                     "FROM barang_masuk bm " +
                     "LEFT JOIN barang_keluar bk ON bm.id_barang = bk.id_barang " +
                     "WHERE ";

        // Menyesuaikan kondisi WHERE berdasarkan periode yang dimasukkan
        if (periode.matches("^\\d{4}-\\d{2}$")) {  // Format YYYY-MM
            sql += "DATE_FORMAT(bm.tanggal_masuk, '%Y-%m') = ?";
        } else if (periode.matches("^\\d{4}$")) {  // Format YYYY
            sql += "DATE_FORMAT(bm.tanggal_masuk, '%Y') = ?";
        } else if (periode.matches("^\\d{2}$")) {  // Format MM
            sql += "DATE_FORMAT(bm.tanggal_masuk, '%m') = ?";
        }

        sql += " GROUP BY bm.tanggal_masuk, bm.id_barang";  // Mengelompokkan berdasarkan tanggal masuk dan ID barang

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, periode);  // Mengatur parameter periode pada query

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
      
    }//GEN-LAST:event_ButtonSearchActionPerformed

    private void ButtonResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonResetActionPerformed
        // TODO add your handling code here:
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
    }//GEN-LAST:event_ButtonResetActionPerformed

    private void Periode1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Periode1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Periode1ActionPerformed

    private void ButtonSearch1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonSearch1ActionPerformed
        // TODO add your handling code here:
         DefaultTableModel model = (DefaultTableModel) TabelBarangTerlaris.getModel();
    
    // Bersihkan data sebelumnya (jika ada)
    model.setRowCount(0);

    // Ambil input dari form
    String periode = Periode1.getText().trim(); // Input dari user

    try {
        // Koneksi ke database
        Connection conn = GUI.mycon();

        // Query SQL dengan LIKE untuk fleksibilitas pencarian
        String sql = "SELECT * FROM barang_keluar " +
                     "WHERE pendapatan > 5000000 AND tanggal_keluar LIKE ?";
        PreparedStatement ps = conn.prepareStatement(sql);

        // Tambahkan wildcard untuk pencarian bulan/tahun
        if (periode.matches("\\d{4}-\\d{2}")) { // Format bulan-tahun (yyyy-MM)
            ps.setString(1, periode + "%");    // yyyy-MM%
        } else if (periode.matches("\\d{4}")) { // Format tahun (yyyy)
            ps.setString(1, periode + "-%");   // yyyy-%
        } else if (periode.matches("\\d{2}")) { // Format bulan (MM)
            ps.setString(1, "%-" + periode + "-%"); // %-MM-%
        } else {
            JOptionPane.showMessageDialog(this, "Format pencarian tidak valid! Gunakan MM, yyyy, atau yyyy-MM.", 
                                          "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ResultSet rs = ps.executeQuery();

        // Iterasi hasil query dan tambahkan data ke model
        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getString("id_barang"),        // ID Barang
                rs.getDouble("jumlah"),          // Jumlah
                rs.getDouble("pendapatan"),      // Pendapatan
                rs.getString("tanggal_keluar")   // Tanggal Keluar
            });
        }

        rs.close();
        ps.close();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal memuat data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_ButtonSearch1ActionPerformed

    private void ButtonReset1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonReset1ActionPerformed
        // TODO add your handling code here:
        // Kosongkan input pada Periode1
    Periode1.setText("");

    // Panggil kembali method loadBarangTerlaris untuk mengembalikan data awal
    loadBarangTerlaris();
    }//GEN-LAST:event_ButtonReset1ActionPerformed

    private void Periode2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Periode2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Periode2ActionPerformed

    private void ButtonSearch2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonSearch2ActionPerformed
        // TODO add your handling code here:
        String periode = Periode2.getText().trim(); // TextFieldPeriode adalah input dari user

    // Bersihkan data tabel sebelumnya
    DefaultTableModel model = (DefaultTableModel) TabelCustomer.getModel();
    model.setRowCount(0);

    try {
        // Koneksi ke database
        Connection conn = GUI.mycon();

        // SQL dasar dengan filter berdasarkan periode
        String sql = "SELECT c.id_customer, c.nama, c.kontak, c.alamat, c.tanggal_bergabung, " +
             "c.jenis_customer, COALESCE(SUM(bk.pendapatan), 0) AS total_pendapatan " +
             "FROM customer c " +
             "LEFT JOIN barang_keluar bk ON c.id_customer = bk.id_customer " +
             "WHERE (DATE_FORMAT(c.tanggal_bergabung, '%Y-%m') = ? OR " +
             "DATE_FORMAT(c.tanggal_bergabung, '%Y') = ? OR " +
             "MONTH(c.tanggal_bergabung) = ? OR " +
             "DATE_FORMAT(c.tanggal_bergabung, '%M') = ?) " +
             "GROUP BY c.id_customer, c.nama, c.kontak, c.alamat, c.tanggal_bergabung, c.jenis_customer";

PreparedStatement ps = conn.prepareStatement(sql);
ps.setString(1, periode);  // Format YYYY-MM
ps.setString(2, periode);  // Format YYYY
ps.setInt(3, Integer.parseInt(periode));  // Angka bulan
ps.setString(4, periode);  // Nama bulan


        ResultSet rs = ps.executeQuery();

        // Iterasi hasil query dan tambahkan data ke model
        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getInt("id_customer"),       // ID Customer
                rs.getString("nama"),           // Nama
                rs.getString("kontak"),         // Kontak
                rs.getString("alamat"),         // Alamat
                rs.getDate("tanggal_bergabung"),// Tanggal Bergabung
                rs.getString("jenis_customer"), // Jenis Customer
                rs.getDouble("total_pendapatan")// Total Pendapatan
            });
        }

        rs.close();
        ps.close();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal memuat data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_ButtonSearch2ActionPerformed

    private void ButtonReset2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonReset2ActionPerformed
        // TODO add your handling code here:
        Periode2.setText("");

    // Panggil kembali method loadBarangTerlaris untuk mengembalikan data awal
    loadData();
    }//GEN-LAST:event_ButtonReset2ActionPerformed

    private void AnalisisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AnalisisActionPerformed
        // TODO add your handling code here:
        this.setVisible(true);
    }//GEN-LAST:event_AnalisisActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Create and display the form */
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
                new Analisis().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Analisis;
    private javax.swing.JButton ButtonReset;
    private javax.swing.JButton ButtonReset1;
    private javax.swing.JButton ButtonReset2;
    private javax.swing.JButton ButtonSearch;
    private javax.swing.JButton ButtonSearch1;
    private javax.swing.JButton ButtonSearch2;
    private javax.swing.JButton Dashboard;
    private javax.swing.JButton HargaJual;
    private javax.swing.JButton Laporan;
    private javax.swing.JButton Logout;
    private javax.swing.JTextField Periode;
    private javax.swing.JTextField Periode1;
    private javax.swing.JTextField Periode2;
    private javax.swing.JTable TabelBarangTerlaris;
    private javax.swing.JTable TabelCustomer;
    private javax.swing.JTable TabelLaporan;
    private javax.swing.JTable TabelRasio;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    // End of variables declaration//GEN-END:variables
}
