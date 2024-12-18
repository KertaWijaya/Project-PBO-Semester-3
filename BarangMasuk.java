/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.pbo.gui.Staff;
import com.pbo.gui.Login;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import com.pbo.gui.GUI;
import com.pbo.gui.Login1;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author asus
 */
public class BarangMasuk extends javax.swing.JFrame {

    /**
     * Creates new form Dashboard
     */
    public BarangMasuk() {
        initComponents();
        loadData();
        loadEvaluasi();
        loadNamaSupplier();
        LaporanBarang();
       
     
        Timer timer = new Timer(2000, new ActionListener() {
       
        public void actionPerformed(ActionEvent e) {
            loadData();
         loadEvaluasi();
        LaporanBarang();
        }
    });
        timer.start(); // Mulai timer
    }
    private void loadEvaluasi() {
    // Ambil model dari TabelData
    DefaultTableModel model = (DefaultTableModel) TabelEvaluasi.getModel();
    
    // Bersihkan data sebelumnya (jika ada)
    model.setRowCount(0);

    try {
        // Koneksi ke database
        Connection conn = GUI.mycon();
        String sql = "SELECT * FROM evaluasisupplier"; // Sesuaikan dengan nama tabel Anda
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        // Iterasi hasil query dan tambahkan data ke model
        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getInt("id_supplier"), // Ganti sesuai dengan kolom tabel database Anda
                rs.getString("kualitas_barang"),
                rs.getString("ketepatan_pengiriman"),
                rs.getString("pelayanan"),
                rs.getString("komentar"),
               
            });
        }

        rs.close();
        ps.close();
       
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal memuat data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}

private void loadNamaSupplier() {
    try {
        // Koneksi ke database
        Connection conn = GUI.mycon();
        String sql = "SELECT id_supplier, nama_supplier FROM supplier WHERE status = 'aktif'"; // Query tabel barang
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        // Bersihkan data sebelumnya di ComboBox
        NamaSupplier.removeAllItems();
        NamaSupplier2.removeAllItems();

        // Tambahkan data nama barang ke ComboBox
        while (rs.next()) {
            // Hanya tambahkan nama_barang ke ComboBox
            NamaSupplier.addItem(rs.getString("nama_supplier"));
            NamaSupplier2.addItem(rs.getString("nama_supplier"));
        }

        rs.close();
        ps.close();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal memuat nama barang: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
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
    
    private void LaporanBarang() {
    // Ambil model dari TabelData
    DefaultTableModel model = (DefaultTableModel) TabelLaporanBarang.getModel();
    
    // Bersihkan data sebelumnya (jika ada)
    model.setRowCount(0);

    try {
        // Koneksi ke database
        Connection conn = GUI.mycon();
        String sql = "SELECT * FROM laporan_barang"; // Sesuaikan dengan nama tabel Anda
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        // Iterasi hasil query dan tambahkan data ke model
        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getInt("id_laporan"), // Ganti sesuai dengan kolom tabel database Anda
                rs.getString("id_barang"),
                rs.getString("deskripsi")
            });
        }

        rs.close();
        ps.close();
       
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal memuat data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    }
 private String getSelectedSupplierId() {
    String namaDipilih = (String) NamaSupplier.getSelectedItem();
    String idSupplier = null;

    try {
        Connection conn = GUI.mycon();
        String sql = "SELECT id_supplier FROM supplier WHERE nama_supplier = ?"; // Cari ID berdasarkan nama
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, namaDipilih);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            idSupplier = rs.getString("id_supplier"); // Ambil ID barang
        }

        rs.close();
        ps.close();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

    return idSupplier; // Kembalikan ID barang
}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        Dashboard = new javax.swing.JButton();
        BarangMasuk = new javax.swing.JButton();
        BarangKeluar = new javax.swing.JButton();
        CekStok = new javax.swing.JButton();
        Logout = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        jScrollPane7 = new javax.swing.JScrollPane();
        TabelLaporanBarang = new javax.swing.JTable();
        jScrollPane1 = new javax.swing.JScrollPane();
        jScrollPane5 = new javax.swing.JScrollPane();
        TabelBarangMasuk = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        IdBarang = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        NamaBarang = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        ButtonSave = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        Kategori = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        Jumlah = new javax.swing.JTextField();
        TanggalMasuk = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        HargaBeli = new javax.swing.JTextField();
        NamaSupplier = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        Tabel2 = new javax.swing.JTabbedPane();
        jPanel11 = new javax.swing.JPanel();
        NamaSupplier2 = new javax.swing.JComboBox<>();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        KualitasBarangRating1 = new javax.swing.JRadioButton();
        KualitasBarangRating2 = new javax.swing.JRadioButton();
        KualitasBarangRating3 = new javax.swing.JRadioButton();
        KualitasBarangRating4 = new javax.swing.JRadioButton();
        KualitasBarangRating5 = new javax.swing.JRadioButton();
        KetepatanKirimRating1 = new javax.swing.JRadioButton();
        KetepatanKirimRating2 = new javax.swing.JRadioButton();
        KetepatanKirimRating3 = new javax.swing.JRadioButton();
        KetepatanKirimRating4 = new javax.swing.JRadioButton();
        KetepatanKirimRating5 = new javax.swing.JRadioButton();
        jLabel30 = new javax.swing.JLabel();
        PelayananRating2 = new javax.swing.JRadioButton();
        PelayananRating3 = new javax.swing.JRadioButton();
        PelayananRating4 = new javax.swing.JRadioButton();
        PelayananRating5 = new javax.swing.JRadioButton();
        jLabel31 = new javax.swing.JLabel();
        PelayananRating1 = new javax.swing.JRadioButton();
        jLabel32 = new javax.swing.JLabel();
        Komentar = new javax.swing.JTextField();
        ButtonSave3 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        TabelEvaluasi = new javax.swing.JTable();

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
                .addGap(472, 472, 472)
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
                    .addComponent(CekStok, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(BarangKeluar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Logout, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

        TabelLaporanBarang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"Router A", "PT. Teknologi Jaya", "50"},
                {"Modem X", "PT. Sumber Elektronik", "30"}
            },
            new String [] {
                "Id Laporan", "Id Barang", "Deskripsi"
            }
        ));
        jScrollPane7.setViewportView(TabelLaporanBarang);

        jScrollPane2.setViewportView(jScrollPane7);

        jTabbedPane2.addTab("Laporan", jScrollPane2);

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

        jTabbedPane2.addTab("Barang Masuk", jScrollPane1);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 584, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel11.setText("Supplier:");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel12.setText("Tanggal Masuk:");

        IdBarang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IdBarangActionPerformed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel13.setText("Id Barang:");

        NamaBarang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NamaBarangActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel14.setText("Nama Barang:");

        ButtonSave.setIcon(new javax.swing.ImageIcon("C:\\Users\\Tiaraaa\\PBO\\GUI(2)\\GUI(2)\\GUI\\src\\main\\java\\com\\pbo\\gui\\image\\Icon_Save.png")); // NOI18N
        ButtonSave.setText("Save");
        ButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonSaveActionPerformed(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel15.setText("Kategori:");

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel16.setText("Jumlah Masuk:");

        TanggalMasuk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TanggalMasukActionPerformed(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel17.setText("Harga Beli:");

        NamaSupplier.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel6.setText("Tambah Barang Masuk");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(IdBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                                .addComponent(jLabel14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(NamaBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(NamaSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel6)
                        .addGap(121, 121, 121))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 68, Short.MAX_VALUE)
                        .addComponent(TanggalMasuk, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(Kategori, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(Jumlah, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ButtonSave)
                            .addComponent(HargaBeli, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel6)
                .addGap(43, 43, 43)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(NamaSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(TanggalMasuk, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(IdBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(NamaBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(Kategori, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15))
                        .addGap(18, 18, 18)
                        .addComponent(Jumlah, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel16))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(HargaBeli, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17))
                .addGap(18, 18, 18)
                .addComponent(ButtonSave)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setText("Evaluasi Kinerja Supplier");

        NamaSupplier2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel28.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel28.setText("Supplier:");

        jLabel29.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel29.setText("Kualitas Barang:");

        buttonGroup1.add(KualitasBarangRating1);
        KualitasBarangRating1.setText("1");
        KualitasBarangRating1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                KualitasBarangRating1ActionPerformed(evt);
            }
        });

        buttonGroup1.add(KualitasBarangRating2);
        KualitasBarangRating2.setText("2");
        KualitasBarangRating2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                KualitasBarangRating2ActionPerformed(evt);
            }
        });

        buttonGroup1.add(KualitasBarangRating3);
        KualitasBarangRating3.setText("3");
        KualitasBarangRating3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                KualitasBarangRating3ActionPerformed(evt);
            }
        });

        buttonGroup1.add(KualitasBarangRating4);
        KualitasBarangRating4.setText("4");
        KualitasBarangRating4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                KualitasBarangRating4ActionPerformed(evt);
            }
        });

        buttonGroup1.add(KualitasBarangRating5);
        KualitasBarangRating5.setText("5");
        KualitasBarangRating5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                KualitasBarangRating5ActionPerformed(evt);
            }
        });

        buttonGroup2.add(KetepatanKirimRating1);
        KetepatanKirimRating1.setText("1");
        KetepatanKirimRating1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                KetepatanKirimRating1ActionPerformed(evt);
            }
        });

        buttonGroup2.add(KetepatanKirimRating2);
        KetepatanKirimRating2.setText("2");
        KetepatanKirimRating2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                KetepatanKirimRating2ActionPerformed(evt);
            }
        });

        buttonGroup2.add(KetepatanKirimRating3);
        KetepatanKirimRating3.setText("3");
        KetepatanKirimRating3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                KetepatanKirimRating3ActionPerformed(evt);
            }
        });

        buttonGroup2.add(KetepatanKirimRating4);
        KetepatanKirimRating4.setText("4");
        KetepatanKirimRating4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                KetepatanKirimRating4ActionPerformed(evt);
            }
        });

        buttonGroup2.add(KetepatanKirimRating5);
        KetepatanKirimRating5.setText("5");
        KetepatanKirimRating5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                KetepatanKirimRating5ActionPerformed(evt);
            }
        });

        jLabel30.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel30.setText("Ketepatan Kirim:");

        buttonGroup3.add(PelayananRating2);
        PelayananRating2.setText("2");
        PelayananRating2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PelayananRating2ActionPerformed(evt);
            }
        });

        buttonGroup3.add(PelayananRating3);
        PelayananRating3.setText("3");
        PelayananRating3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PelayananRating3ActionPerformed(evt);
            }
        });

        buttonGroup3.add(PelayananRating4);
        PelayananRating4.setText("4");
        PelayananRating4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PelayananRating4ActionPerformed(evt);
            }
        });

        buttonGroup3.add(PelayananRating5);
        PelayananRating5.setText("5");
        PelayananRating5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PelayananRating5ActionPerformed(evt);
            }
        });

        jLabel31.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel31.setText("Pelayanan:");

        buttonGroup3.add(PelayananRating1);
        PelayananRating1.setText("1");
        PelayananRating1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PelayananRating1ActionPerformed(evt);
            }
        });

        jLabel32.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel32.setText("Komentar:");

        Komentar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                KomentarActionPerformed(evt);
            }
        });

        ButtonSave3.setIcon(new javax.swing.ImageIcon("C:\\Users\\Tiaraaa\\PBO\\GUI(2)\\GUI(2)\\GUI\\src\\main\\java\\com\\pbo\\gui\\image\\Icon_Save.png")); // NOI18N
        ButtonSave3.setText("Save");
        ButtonSave3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonSave3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel28)
                            .addComponent(jLabel29)
                            .addComponent(jLabel30)
                            .addComponent(jLabel31))
                        .addGap(44, 44, 44)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(NamaSupplier2, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                                    .addComponent(KualitasBarangRating1)
                                    .addGap(18, 18, 18)
                                    .addComponent(KualitasBarangRating2)
                                    .addGap(18, 18, 18)
                                    .addComponent(KualitasBarangRating3)
                                    .addGap(18, 18, 18)
                                    .addComponent(KualitasBarangRating4)
                                    .addGap(18, 18, 18)
                                    .addComponent(KualitasBarangRating5))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                                    .addComponent(KetepatanKirimRating1)
                                    .addGap(18, 18, 18)
                                    .addComponent(KetepatanKirimRating2)
                                    .addGap(18, 18, 18)
                                    .addComponent(KetepatanKirimRating3)
                                    .addGap(18, 18, 18)
                                    .addComponent(KetepatanKirimRating4)
                                    .addGap(18, 18, 18)
                                    .addComponent(KetepatanKirimRating5))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                                    .addComponent(PelayananRating1)
                                    .addGap(18, 18, 18)
                                    .addComponent(PelayananRating2)
                                    .addGap(18, 18, 18)
                                    .addComponent(PelayananRating3)
                                    .addGap(18, 18, 18)
                                    .addComponent(PelayananRating4)
                                    .addGap(18, 18, 18)
                                    .addComponent(PelayananRating5)))))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel32)
                        .addGap(86, 86, 86)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ButtonSave3)
                            .addComponent(Komentar))))
                .addContainerGap(160, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28)
                    .addComponent(NamaSupplier2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel29)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel30)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel31))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(KualitasBarangRating1)
                            .addComponent(KualitasBarangRating2)
                            .addComponent(KualitasBarangRating3)
                            .addComponent(KualitasBarangRating4)
                            .addComponent(KualitasBarangRating5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(KetepatanKirimRating1)
                            .addComponent(KetepatanKirimRating2)
                            .addComponent(KetepatanKirimRating3)
                            .addComponent(KetepatanKirimRating4)
                            .addComponent(KetepatanKirimRating5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(PelayananRating1)
                            .addComponent(PelayananRating2)
                            .addComponent(PelayananRating3)
                            .addComponent(PelayananRating4)
                            .addComponent(PelayananRating5))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Komentar, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel32))
                .addGap(18, 18, 18)
                .addComponent(ButtonSave3, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        Tabel2.addTab("Form", jPanel11);

        TabelEvaluasi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"SUP-001", "PT. ABC Supplier", "08123456789", "Jl. Merdeka No.1", "abc@supplier.com"},
                {"SUP-002", "CV. XYZ Jaya", "08987654321", "Jl. Sudirman No.5", "xyz@jaya.com"},
                {"SUP-003", "Toko Hardware A", "08190876543", "Jl. Pahlawan No.3", "hardware@toko.com	"}
            },
            new String [] {
                "ID Supplier", "Kualitas Barang", "Ketepatan Pengiriman", "Pelayanan", "Komentar"
            }
        ));
        jScrollPane3.setViewportView(TabelEvaluasi);

        Tabel2.addTab("Tabel", jScrollPane3);

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Tabel2)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Tabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
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
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
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

    private void IdBarangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_IdBarangActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_IdBarangActionPerformed

    private void NamaBarangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NamaBarangActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_NamaBarangActionPerformed

    private void ButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonSaveActionPerformed
        // TODO add your handling code here:
    String idSupplier = getSelectedSupplierId(); // ID Supplier diambil dari ComboBox
    String tanggalMasuk = TanggalMasuk.getText(); // Tanggal masuk barang
    String idBarang = IdBarang.getText(); // ID Barang diambil dari ComboBox
    String namaBarang = NamaBarang.getText();
    String kategori = Kategori.getText();
    String jumlah = Jumlah.getText(); // Jumlah barang masuk
    String hargaBeli = HargaBeli.getText(); // Harga beli barang

    // Validasi input kosong
    if (idSupplier == null || tanggalMasuk.isEmpty() || idBarang == null || namaBarang.isEmpty() || jumlah.isEmpty() || hargaBeli.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Semua field harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
        return; // Hentikan proses jika ada field kosong
    }

    try {
        // Koneksi ke database
        Connection conn = GUI.mycon();

        // Query untuk insert data
        String sql = """
            INSERT INTO barang_masuk (id_supplier, tanggal_masuk, id_barang, nama_barang,kategori, jumlah, harga_beli) 
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;
        PreparedStatement ps = conn.prepareStatement(sql);

        // Set nilai dari input GUI ke query
        ps.setString(1, idSupplier);
        ps.setString(2, tanggalMasuk);
        ps.setString(3, idBarang);
        ps.setString(4, namaBarang);
        ps.setString(5, kategori);
        ps.setString(6, jumlah);
        ps.setString(7, hargaBeli);

        // Eksekusi query
        ps.executeUpdate();

        // Beri notifikasi berhasil
        JOptionPane.showMessageDialog(this, "Data barang masuk berhasil disimpan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);

        // Reset input GUI setelah berhasil insert
        TanggalMasuk.setText("");
        IdBarang.setText("");
        NamaBarang.setText("");
        Kategori.setText("");
        Jumlah.setText("");
        HargaBeli.setText("");
        NamaSupplier.setSelectedIndex(0); // Reset ComboBox ke pilihan pertama
        loadData(); // Muat ulang data di tabel

        // Tutup PreparedStatement
        ps.close();
    } catch (Exception e) {
        // Tangani error
        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_ButtonSaveActionPerformed

    private void BarangMasukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BarangMasukActionPerformed
        // TODO add your handling code here:
        this.setVisible(true);
    }//GEN-LAST:event_BarangMasukActionPerformed

    private void BarangKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BarangKeluarActionPerformed
        // TODO add your handling code here:
        new BarangKeluar().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_BarangKeluarActionPerformed

    private void TanggalMasukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TanggalMasukActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TanggalMasukActionPerformed

    private void KualitasBarangRating1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_KualitasBarangRating1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_KualitasBarangRating1ActionPerformed

    private void KualitasBarangRating2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_KualitasBarangRating2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_KualitasBarangRating2ActionPerformed

    private void KualitasBarangRating3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_KualitasBarangRating3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_KualitasBarangRating3ActionPerformed

    private void KualitasBarangRating4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_KualitasBarangRating4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_KualitasBarangRating4ActionPerformed

    private void KualitasBarangRating5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_KualitasBarangRating5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_KualitasBarangRating5ActionPerformed

    private void KetepatanKirimRating1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_KetepatanKirimRating1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_KetepatanKirimRating1ActionPerformed

    private void KetepatanKirimRating2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_KetepatanKirimRating2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_KetepatanKirimRating2ActionPerformed

    private void KetepatanKirimRating3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_KetepatanKirimRating3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_KetepatanKirimRating3ActionPerformed

    private void KetepatanKirimRating4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_KetepatanKirimRating4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_KetepatanKirimRating4ActionPerformed

    private void KetepatanKirimRating5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_KetepatanKirimRating5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_KetepatanKirimRating5ActionPerformed

    private void PelayananRating2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PelayananRating2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_PelayananRating2ActionPerformed

    private void PelayananRating3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PelayananRating3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_PelayananRating3ActionPerformed

    private void PelayananRating4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PelayananRating4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_PelayananRating4ActionPerformed

    private void PelayananRating5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PelayananRating5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_PelayananRating5ActionPerformed

    private void PelayananRating1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PelayananRating1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_PelayananRating1ActionPerformed

    private void KomentarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_KomentarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_KomentarActionPerformed

    private void ButtonSave3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonSave3ActionPerformed
        // TODO add your handling code here:
        String idSupplier = getSelectedSupplierId();

        // Mendapatkan rating kualitas barang
        int kualitasBarangRating = 0;
        if (KualitasBarangRating1.isSelected()) {
            kualitasBarangRating = 1;
        } else if (KualitasBarangRating2.isSelected()) {
            kualitasBarangRating = 2;
        } else if (KualitasBarangRating3.isSelected()) {
            kualitasBarangRating = 3;
        } else if (KualitasBarangRating4.isSelected()) {
            kualitasBarangRating = 4;
        } else if (KualitasBarangRating5.isSelected()) {
            kualitasBarangRating = 5;
        }

        // Mendapatkan rating ketepatan pengiriman
        int ketepatanPengirimanRating = 0;
        if (KetepatanKirimRating1.isSelected()) {
            ketepatanPengirimanRating = 1;
        } else if (KetepatanKirimRating2.isSelected()) {
            ketepatanPengirimanRating = 2;
        } else if (KetepatanKirimRating3.isSelected()) {
            ketepatanPengirimanRating = 3;
        } else if (KetepatanKirimRating4.isSelected()) {
            ketepatanPengirimanRating = 4;
        } else if (KetepatanKirimRating5.isSelected()) {
            ketepatanPengirimanRating = 5;
        }

        // Mendapatkan rating pelayanan
        int pelayananRating = 0;
        if (PelayananRating1.isSelected()) {
            pelayananRating = 1;
        } else if (PelayananRating2.isSelected()) {
            pelayananRating = 2;
        } else if (PelayananRating3.isSelected()) {
            pelayananRating = 3;
        } else if (PelayananRating4.isSelected()) {
            pelayananRating = 4;
        } else if (PelayananRating5.isSelected()) {
            pelayananRating = 5;
        }

        // Mendapatkan komentar
        String komentar = Komentar.getText();

        // Menyimpan data ke database
        try {
            Connection conn = GUI.mycon();
            String sql = "INSERT INTO evaluasisupplier (id_supplier, kualitas_barang, ketepatan_pengiriman, pelayanan, komentar) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);

            // Set parameter untuk query
            ps.setString(1, idSupplier);
            ps.setInt(2, kualitasBarangRating);
            ps.setInt(3, ketepatanPengirimanRating);
            ps.setInt(4, pelayananRating);
            ps.setString(5, komentar);

            // Eksekusi query untuk menyimpan data
            ps.executeUpdate();

            // Menampilkan pesan berhasil
            JOptionPane.showMessageDialog(this, "Evaluasi supplier berhasil disimpan.", "Sukses", JOptionPane.INFORMATION_MESSAGE);

            // Reset form setelah simpan data
            NamaSupplier.setSelectedIndex(0);  // Reset combo box
            buttonGroup1.clearSelection();  // Clear all radio buttons for kualitas
            buttonGroup2.clearSelection();  // Clear all radio buttons for ketepatan
            buttonGroup3.clearSelection();  // Clear all radio buttons for pelayanan
            Komentar.setText("");  // Reset komentar

        } catch (Exception e) {
            // Menampilkan pesan error jika gagal
            JOptionPane.showMessageDialog(this, "Gagal menyimpan data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_ButtonSave3ActionPerformed

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
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new BarangMasuk().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BarangKeluar;
    private javax.swing.JButton BarangMasuk;
    private javax.swing.JButton ButtonSave;
    private javax.swing.JButton ButtonSave3;
    private javax.swing.JButton CekStok;
    private javax.swing.JButton Dashboard;
    private javax.swing.JTextField HargaBeli;
    private javax.swing.JTextField IdBarang;
    private javax.swing.JTextField Jumlah;
    private javax.swing.JTextField Kategori;
    private javax.swing.JRadioButton KetepatanKirimRating1;
    private javax.swing.JRadioButton KetepatanKirimRating2;
    private javax.swing.JRadioButton KetepatanKirimRating3;
    private javax.swing.JRadioButton KetepatanKirimRating4;
    private javax.swing.JRadioButton KetepatanKirimRating5;
    private javax.swing.JTextField Komentar;
    private javax.swing.JRadioButton KualitasBarangRating1;
    private javax.swing.JRadioButton KualitasBarangRating2;
    private javax.swing.JRadioButton KualitasBarangRating3;
    private javax.swing.JRadioButton KualitasBarangRating4;
    private javax.swing.JRadioButton KualitasBarangRating5;
    private javax.swing.JButton Logout;
    private javax.swing.JTextField NamaBarang;
    private javax.swing.JComboBox<String> NamaSupplier;
    private javax.swing.JComboBox<String> NamaSupplier2;
    private javax.swing.JRadioButton PelayananRating1;
    private javax.swing.JRadioButton PelayananRating2;
    private javax.swing.JRadioButton PelayananRating3;
    private javax.swing.JRadioButton PelayananRating4;
    private javax.swing.JRadioButton PelayananRating5;
    private javax.swing.JTabbedPane Tabel2;
    private javax.swing.JTable TabelBarangMasuk;
    private javax.swing.JTable TabelEvaluasi;
    private javax.swing.JTable TabelLaporanBarang;
    private javax.swing.JTextField TanggalMasuk;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    // End of variables declaration//GEN-END:variables
}
