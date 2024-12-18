/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.pbo.gui.Staff;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import com.pbo.gui.GUI;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import com.pbo.gui.Admin.*;
import com.pbo.gui.Login;
import com.pbo.gui.Login1;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 *
 * @author asus
 */
public class BarangKeluar extends javax.swing.JFrame {

    /**
     * Creates new form Dashboard
     */
    public BarangKeluar() {
        initComponents();
        loadData();
        
        loadNamaBarang();
        loadPenerima();
        Status.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Terkirim", "Pending", "Batal"}));
        
        
        Timer timer = new Timer(2000, new ActionListener() {
       
        public void actionPerformed(ActionEvent e) {
            loadData();
        }
    });
    
    timer.start(); // Mulai timer
    
    Pendapatan.setEditable(false);
     
    Jumlah.addKeyListener(new KeyAdapter() {
    @Override
    public void keyReleased(KeyEvent e) {
        updatePendapatan(); // Hitung pendapatan setiap kali jumlah barang diubah
    }
});
        
    }
    
    private void loadNamaBarang() {
    try {
        // Koneksi ke database
        Connection conn = GUI.mycon();
        
        // Gunakan DISTINCT untuk hanya mengambil nama_barang unik
        String sql = "SELECT DISTINCT nama_barang FROM barang_masuk "; 
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        // Bersihkan data sebelumnya di ComboBox
        NamaBarang.removeAllItems();

        // Tambahkan data nama barang unik ke ComboBox
        while (rs.next()) {
            NamaBarang.addItem(rs.getString("nama_barang"));
        }

        rs.close();
        ps.close();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal memuat nama barang: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
    
    private void loadPenerima() {
    try {
        // Koneksi ke database
        Connection conn = GUI.mycon();
        String sql = "SELECT id_customer, nama FROM customer"; // Query tabel barang
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        // Bersihkan data sebelumnya di ComboBox
        Penerima.removeAllItems();

        // Tambahkan data nama barang ke ComboBox
        while (rs.next()) {
            // Hanya tambahkan nama_barang ke ComboBox
            Penerima.addItem(rs.getString("nama"));
        }

        rs.close();
        ps.close();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal memuat nama penerima: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
   

// Ambil ID barang berdasarkan nama barang yang dipilih
private String getSelectedBarangId() {
    String namaDipilih = (String) NamaBarang.getSelectedItem();
    String idBarang = null;

    try {
        Connection conn = GUI.mycon();
        String sql = "SELECT id_barang FROM barang_masuk WHERE nama_barang = ?"; // Cari ID berdasarkan nama
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, namaDipilih);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            idBarang = rs.getString("id_barang"); // Ambil ID barang
        }

        rs.close();
        ps.close();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

    return idBarang; // Kembalikan ID barang
}
    

private void loadData() {
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
    private void updatePendapatan() {
    try {
        String idBarang = getSelectedBarangId(); // ID Barang diambil dari ComboBox
        String jumlah = Jumlah.getText(); // Jumlah barang keluar

        if (jumlah.isEmpty()) {
            Pendapatan.setText(""); // Kosongkan pendapatan jika jumlah kosong
            return;
        }

        int jumlahBarang = Integer.parseInt(jumlah);

        // Ambil harga per unit dan harga grosir barang dari tabel barang
        Connection conn = GUI.mycon();
        String sqlHarga = "SELECT hargaunit, hargagrosir FROM hargajual WHERE id_barang = ?";
        PreparedStatement psHarga = conn.prepareStatement(sqlHarga);
        psHarga.setString(1, idBarang);
        ResultSet rsHarga = psHarga.executeQuery();

        if (rsHarga.next()) {
            double hargaUnit = rsHarga.getDouble("hargaunit");
            double hargaGrosir = rsHarga.getDouble("hargagrosir");

            double pendapatan;

            if (jumlahBarang < 25) {
                // Jika jumlah barang kurang dari 25, gunakan harga per unit
                pendapatan = hargaUnit * jumlahBarang;
            } else {
                // Jika jumlah barang 25 atau lebih, gunakan harga grosir
                pendapatan = hargaGrosir * jumlahBarang;
            }

            // Update nilai pendapatan di GUI
            Pendapatan.setText(String.valueOf(pendapatan));
        }

        psHarga.close();
        rsHarga.close();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
        BarangMasuk = new javax.swing.JButton();
        BarangKeluar = new javax.swing.JButton();
        CekStok = new javax.swing.JButton();
        Logout = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        TabelCustomer = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        Tanggalkeluar = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        Jumlah = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        ButtonSave = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        ButtonSearch = new javax.swing.JButton();
        Status = new javax.swing.JComboBox<>();
        NamaBarang = new javax.swing.JComboBox<>();
        jLabel18 = new javax.swing.JLabel();
        Pendapatan = new javax.swing.JTextField();
        Penerima = new javax.swing.JComboBox<>();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        Cari = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 51, 102));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Selamat Datang Kembali Staff");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(442, 442, 442)
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

        BarangMasuk.setIcon(new javax.swing.ImageIcon("C:\\Users\\Tiaraaa\\PBO\\GUI(2)\\GUI(2)\\GUI\\src\\main\\java\\com\\pbo\\gui\\image\\BarangMasukkk.png")); // NOI18N
        BarangMasuk.setText("Barang Masuk");
        BarangMasuk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BarangMasukActionPerformed(evt);
            }
        });

        BarangKeluar.setIcon(new javax.swing.ImageIcon("C:\\Users\\Tiaraaa\\PBO\\GUI(2)\\GUI(2)\\GUI\\src\\main\\java\\com\\pbo\\gui\\image\\BarangKeluarr.png")); // NOI18N
        BarangKeluar.setText("Barang Keluar");
        BarangKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BarangKeluarActionPerformed(evt);
            }
        });

        CekStok.setIcon(new javax.swing.ImageIcon("C:\\Users\\Tiaraaa\\PBO\\GUI(2)\\GUI(2)\\GUI\\src\\main\\java\\com\\pbo\\gui\\image\\Icon_Cek Stok Barang.png")); // NOI18N
        CekStok.setText("Cek Stok");
        CekStok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CekStokActionPerformed(evt);
            }
        });

        Logout.setIcon(new javax.swing.ImageIcon("C:\\Users\\Tiaraaa\\PBO\\GUI(2)\\GUI(2)\\GUI\\src\\main\\java\\com\\pbo\\gui\\image\\Exit.png")); // NOI18N
        Logout.setText("Logout");
        Logout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LogoutActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(BarangMasuk, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Dashboard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Logout, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(BarangKeluar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(CekStok, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(44, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(Dashboard)
                .addGap(18, 18, 18)
                .addComponent(BarangMasuk)
                .addGap(18, 18, 18)
                .addComponent(BarangKeluar)
                .addGap(18, 18, 18)
                .addComponent(CekStok)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Logout)
                .addContainerGap())
        );

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setText("Riwayat Transaksi");

        TabelCustomer.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id Penjualan", "Tanggal", "Id Barang", "Jumlah", "Pendapatan", "Id Customer", "Status"
            }
        ));
        jScrollPane2.setViewportView(TabelCustomer);
        if (TabelCustomer.getColumnModel().getColumnCount() > 0) {
            TabelCustomer.getColumnModel().getColumn(1).setResizable(false);
        }

        jScrollPane1.setViewportView(jScrollPane2);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 565, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel11.setText("Tanggal Keluar:");

        Tanggalkeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TanggalkeluarActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel12.setText("Nama Barang:");

        Jumlah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JumlahActionPerformed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel13.setText("Jumlah");

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel14.setText("Penerima:");

        ButtonSave.setIcon(new javax.swing.ImageIcon("C:\\Users\\Tiaraaa\\PBO\\GUI(2)\\GUI(2)\\GUI\\src\\main\\java\\com\\pbo\\gui\\image\\Icon_Save.png")); // NOI18N
        ButtonSave.setText("Save");
        ButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonSaveActionPerformed(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel17.setText("Status:");

        ButtonSearch.setIcon(new javax.swing.ImageIcon("C:\\Users\\Tiaraaa\\PBO\\GUI(2)\\GUI(2)\\GUI\\src\\main\\java\\com\\pbo\\gui\\image\\Icon_Save.png")); // NOI18N
        ButtonSearch.setText("Search");
        ButtonSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonSearchActionPerformed(evt);
            }
        });

        Status.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        NamaBarang.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel18.setText("Pendapatan");

        Pendapatan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PendapatanActionPerformed(evt);
            }
        });

        Penerima.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        Penerima.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PenerimaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addComponent(jLabel17)
                            .addComponent(jLabel12)
                            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel13)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel7Layout.createSequentialGroup()
                                .addComponent(ButtonSearch)
                                .addGap(18, 18, 18)
                                .addComponent(ButtonSave))
                            .addComponent(Jumlah, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 268, Short.MAX_VALUE)
                            .addComponent(Tanggalkeluar, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Status, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(NamaBarang, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Pendapatan, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Penerima, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel18))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Tanggalkeluar, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addGap(19, 19, 19)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addComponent(NamaBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Jumlah, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(Pendapatan, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(Penerima, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Status, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17))
                .addGap(29, 29, 29)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ButtonSave, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ButtonSearch))
                .addGap(48, 48, 48))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("Data Transaksi");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel9.setText("Cari Riwayat:");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Cari, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addContainerGap(31, Short.MAX_VALUE))
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
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 345, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void DashboardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DashboardActionPerformed
        // TODO add your handling code here:
        new Dashboard().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_DashboardActionPerformed

    private void CekStokActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CekStokActionPerformed
        // TODO add your handling code here:
        new CekStok().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_CekStokActionPerformed

    private void LogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LogoutActionPerformed
        // TODO add your handling code here:
        new Login1().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_LogoutActionPerformed

    private void JumlahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JumlahActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_JumlahActionPerformed

    
    private void ButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonSaveActionPerformed
    String idBarang = getSelectedBarangId(); // ID Barang dari ComboBox Barang
    String tanggalKeluar = Tanggalkeluar.getText(); // Tanggal keluar
    String jumlah = Jumlah.getText(); // Jumlah barang keluar
    String idCustomer = getCustomerIdFromCombo(Penerima.getSelectedItem().toString()); // Dapatkan ID Customer berdasarkan nama
    String status = Status.getSelectedItem().toString(); // Status barang keluar
    String pendapatan = Pendapatan.getText(); // Pendapatan yang dihitung sebelumnya

    // Validasi input kosong
    if (idBarang == null || tanggalKeluar.isEmpty() || jumlah.isEmpty() || idCustomer == null || status.isEmpty() || pendapatan.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Semua field harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
        return; // Hentikan proses jika ada field kosong
    }

    try {
        // Koneksi ke database
        Connection conn = GUI.mycon();

        // Validasi stok sisa
        String sqlCheck = "SELECT stok_sisa FROM stokbarang WHERE id_barang = ?";
        PreparedStatement psCheck = conn.prepareStatement(sqlCheck);
        psCheck.setString(1, idBarang); // Set ID Barang ke query
        ResultSet rs = psCheck.executeQuery();

        if (rs.next()) {
            int stokSisa = rs.getInt("stok_sisa");
            int jumlahKeluar = Integer.parseInt(jumlah);

            if (jumlahKeluar > stokSisa) {
                // Jika jumlah lebih besar dari stok sisa, tampilkan pesan error
                JOptionPane.showMessageDialog(this, "Jumlah stok melebihi! Stok tersedia: " + stokSisa, "Error", JOptionPane.ERROR_MESSAGE);
                return; // Hentikan proses
            }
        } else {
            JOptionPane.showMessageDialog(this, "Barang tidak ditemukan di stok!", "Error", JOptionPane.ERROR_MESSAGE);
            return; // Hentikan proses jika barang tidak ditemukan
        }

        // Tutup ResultSet dan PreparedStatement
        rs.close();
        psCheck.close();

        // Query untuk insert data
        String sql = """
            INSERT INTO barang_keluar (id_barang, tanggal_keluar, jumlah, id_customer, status, pendapatan) 
            VALUES (?, ?, ?, ?, ?, ?)
        """;
        PreparedStatement ps = conn.prepareStatement(sql);

        // Set nilai dari input GUI ke query
        ps.setString(1, idBarang);       // ID Barang
        ps.setString(2, tanggalKeluar); // Tanggal Keluar
        ps.setString(3, jumlah);        // Jumlah Barang Keluar
        ps.setInt(4, Integer.parseInt(idCustomer));    // ID Customer sebagai integer
        ps.setString(5, status);        // Status
        ps.setString(6, pendapatan);    // Pendapatan

        // Eksekusi query
        ps.executeUpdate();

        // Beri notifikasi berhasil
        JOptionPane.showMessageDialog(this, "Data barang keluar berhasil disimpan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);

        // Reset input GUI setelah berhasil insert
        Tanggalkeluar.setText("");
        Jumlah.setText("");
        Penerima.setSelectedIndex(0); // Reset ComboBox Penerima
        Status.setSelectedIndex(0);          // Reset ComboBox Status
        NamaBarang.setSelectedIndex(0);      // Reset ComboBox Barang
        Pendapatan.setText("");              // Reset Pendapatan

        loadData(); // Muat ulang data di tabel

        // Tutup PreparedStatement
        ps.close();
    } catch (Exception e) {
        // Tangani error
        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_ButtonSaveActionPerformed

    private String getCustomerIdFromCombo(String customerName) {
    String customerId = null;
    try {
        Connection conn = GUI.mycon();
        String sql = "SELECT id_customer FROM customer WHERE nama = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, customerName);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            customerId = rs.getString("id_customer"); // Ambil ID customer
        }
        rs.close();
        ps.close();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error mencari ID Customer: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    return customerId;
}
    
    private void BarangMasukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BarangMasukActionPerformed
        // TODO add your handling code here:
        new BarangMasuk().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_BarangMasukActionPerformed

    private void BarangKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BarangKeluarActionPerformed
        // TODO add your handling code here:
        this.setVisible(true);
    }//GEN-LAST:event_BarangKeluarActionPerformed

    private void ButtonSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonSearchActionPerformed
        // TODO add your handling code here:
        String cari = Cari.getText().trim(); 

    // Validasi jika field Cari kosong
    if (cari.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Masukkan ID Penjualan yang ingin dicari!", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    try {
        // Koneksi ke database
        Connection conn = GUI.mycon();
        
        // Query SQL untuk mencari data berdasarkan ID Penjualan
        String sql = "SELECT * FROM barang_keluar WHERE id_penjualan LIKE ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, "%" + cari + "%");  // Gunakan LIKE dengan wildcard untuk mencari ID yang mengandung kata kunci

        // Eksekusi query
        ResultSet rs = ps.executeQuery();

        // Cek apakah ada data yang ditemukan
        if (rs.next()) {
            // Jika ditemukan, tampilkan data pada form input
            Tanggalkeluar.setText(rs.getString("tanggal_keluar"));
            
            // Set ComboBox untuk NamaBarang dengan id_barang dari database
            String idBarang = rs.getString("id_barang");
            NamaBarang.setSelectedItem(idBarang); // Sesuaikan dengan item yang ada di ComboBox
            
            // Set nilai jumlah dan pendapatan
            Jumlah.setText(rs.getString("jumlah"));
            Pendapatan.setText(rs.getString("pendapatan"));
            
            // Set ComboBox Penerima berdasarkan customer yang terkait dengan id_customer
            String penerimaId = rs.getString("id_customer");
            // Misalnya, ComboBox Penerima memuat nama customer berdasarkan ID
            setPenerimaComboBox(penerimaId); // Menyesuaikan comboBox penerima berdasarkan ID customer
            
            // Set Status combo box dengan nilai status dari database
            String status = rs.getString("status");
            if (status != null) {
                Status.setSelectedItem(status); // Pilih status yang sesuai
            }
        } else {
            // Jika tidak ada hasil pencarian
            JOptionPane.showMessageDialog(this, "Data tidak ditemukan!", "Pencarian Gagal", JOptionPane.INFORMATION_MESSAGE);
        }

        // Tutup ResultSet, PreparedStatement, dan koneksi
        rs.close();
        ps.close();
        
    } catch (Exception e) {
        // Tangani error koneksi atau lainnya
        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Exception", JOptionPane.ERROR_MESSAGE);
    }
} 

// Fungsi untuk mengisi ComboBox Penerima berdasarkan id_customer
private void setPenerimaComboBox(String idCustomer) {
    // Cari nama customer berdasarkan id_customer
    try {
        Connection conn = GUI.mycon();
        String sql = "SELECT nama FROM customer WHERE id_customer = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, idCustomer);
        ResultSet rs = ps.executeQuery();
        
        if (rs.next()) {
            String namaCustomer = rs.getString("nama");
            Penerima.setSelectedItem(namaCustomer);  // Set nama customer ke dalam ComboBox
        }

        rs.close();
        ps.close();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error saat memuat data penerima: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_ButtonSearchActionPerformed

    private void TanggalkeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TanggalkeluarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TanggalkeluarActionPerformed

    private void PendapatanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PendapatanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_PendapatanActionPerformed

    private void PenerimaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PenerimaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_PenerimaActionPerformed

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
            java.util.logging.Logger.getLogger(BarangKeluar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BarangKeluar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BarangKeluar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BarangKeluar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
       java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new BarangKeluar().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BarangKeluar;
    private javax.swing.JButton BarangMasuk;
    private javax.swing.JButton ButtonSave;
    private javax.swing.JButton ButtonSearch;
    private javax.swing.JTextField Cari;
    private javax.swing.JButton CekStok;
    private javax.swing.JButton Dashboard;
    private javax.swing.JTextField Jumlah;
    private javax.swing.JButton Logout;
    private javax.swing.JComboBox<String> NamaBarang;
    private javax.swing.JTextField Pendapatan;
    private javax.swing.JComboBox<String> Penerima;
    private javax.swing.JComboBox<String> Status;
    private javax.swing.JTable TabelCustomer;
    private javax.swing.JTextField Tanggalkeluar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
}
