/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.pbo.gui.Staff;
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
public class CekStok extends javax.swing.JFrame {

    /**
     * Creates new form Dashboard
     */
    public CekStok() {
        initComponents();
        loadDataStokKeseluruhan();
        loadDataStokMenipis();
        Timer timer = new Timer(2000, new ActionListener() {
       
        public void actionPerformed(ActionEvent e) {
           
            loadDataStokMenipis();
        }
    });
    
    timer.start(); // Mulai timer
    }
    
    private void loadDataStokMenipis() {
    // Ambil model dari TabelStokBarangMenipis
    DefaultTableModel model = (DefaultTableModel) TabelStokBarangMenipis.getModel();
    
    // Bersihkan data sebelumnya (jika ada)
    model.setRowCount(0);

    try {
        // Koneksi ke database
        Connection conn = GUI.mycon(); // Metode koneksi database
        String sql = """
            SELECT id_barang, nama_barang, jumlah, stok_keluar, stok_sisa
            FROM Stokbarang
            WHERE stok_sisa < 20
        """; // Query untuk mengambil data barang yang stok sisa < 20
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        // Iterasi hasil query dan tambahkan data ke model
        while (rs.next()) {
            model.addRow(new Object[] {
                rs.getString("id_barang"),   // Kolom id_barang
                rs.getString("nama_barang"), // Kolom nama_barang
                rs.getInt("jumlah"),         // Kolom jumlah (stok awal)
                rs.getInt("stok_keluar"),    // Kolom stok_keluar
                rs.getInt("stok_sisa"),
                "Jangan lupa isi stok" // Kolom status// Kolom stok_sisa
            });
        }

        rs.close();
        ps.close();
    } catch (Exception e) {
        // Tampilkan pesan error jika terjadi masalah
        JOptionPane.showMessageDialog(this, "Gagal memuat data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}

    
    private void loadDataStokKeseluruhan() {
    // Ambil model dari TabelStokKeseluruhan
    DefaultTableModel model = (DefaultTableModel) TabelStokKeseluruhan.getModel();
    
    // Bersihkan data sebelumnya (jika ada)
    model.setRowCount(0);

    try {
        // Koneksi ke database
        Connection conn = GUI.mycon(); // Metode koneksi database
        String sql = "SELECT * FROM stokbarang"; // Query untuk mengambil data dari tabel Stokbarang
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        // Iterasi hasil query dan tambahkan data ke model
        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getString("id_barang"),    // Kolom id_barang
                rs.getString("nama_barang"),  // Kolom nama_barang
                rs.getInt("jumlah"),          // Kolom jumlah (stok awal)
                rs.getInt("stok_keluar"),     // Kolom stok_keluar
                rs.getInt("stok_sisa")        // Kolom stok_sisa
            });
        }

        rs.close();
        ps.close();
    } catch (Exception e) {
        // Tampilkan pesan error jika terjadi masalah
        JOptionPane.showMessageDialog(this, "Gagal memuat data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}

    
    private void updateStokBarang() {
    try {
        // Koneksi ke database
        Connection conn = GUI.mycon();

        // Query untuk mendapatkan data dari barang_masuk
        String sqlStokAwal = """
            SELECT id_barang, nama_barang, SUM(jumlah) AS jumlah
            FROM barang_masuk
            GROUP BY id_barang
        """;
        PreparedStatement psStokAwal = conn.prepareStatement(sqlStokAwal);
        ResultSet rsStokAwal = psStokAwal.executeQuery();

        // Iterasi setiap barang dari barang_masuk
        while (rsStokAwal.next()) {
            String idBarang = rsStokAwal.getString("id_barang");
            String namaBarang = rsStokAwal.getString("nama_barang");
            int stokAwal = rsStokAwal.getInt("jumlah");

            // Query untuk mendapatkan jumlah barang keluar
            String sqlStokKeluar = """
                SELECT SUM(jumlah) AS stok_keluar
                FROM barang_keluar
                WHERE id_barang = ?
            """;
            PreparedStatement psStokKeluar = conn.prepareStatement(sqlStokKeluar);
            psStokKeluar.setString(1, idBarang);
            ResultSet rsStokKeluar = psStokKeluar.executeQuery();

            int stokKeluar = 0;
            if (rsStokKeluar.next()) {
                stokKeluar = rsStokKeluar.getInt("stok_keluar");
            }

            // Hitung stok sisa
            int stokSisa = stokAwal - stokKeluar;

            // Perbarui atau masukkan data ke tabel Stokbarang
            String sqlUpdateStok = """
    INSERT INTO Stokbarang (id_barang, nama_barang, jumlah, stok_keluar)
    VALUES (?, ?, ?, ?)
    ON DUPLICATE KEY UPDATE
    jumlah = VALUES(jumlah),
    stok_keluar = VALUES(stok_keluar)
""";
PreparedStatement psUpdateStok = conn.prepareStatement(sqlUpdateStok);
psUpdateStok.setString(1, idBarang);
psUpdateStok.setString(2, namaBarang);
psUpdateStok.setInt(3, stokAwal);
psUpdateStok.setInt(4, stokKeluar);
psUpdateStok.executeUpdate();

            // Tutup PreparedStatement stok_keluar
            rsStokKeluar.close();
            psStokKeluar.close();
        }

        // Tutup PreparedStatement stok_awal
        rsStokAwal.close();
        psStokAwal.close();

        JOptionPane.showMessageDialog(this, "Stok barang berhasil diperbarui!", "Sukses", JOptionPane.INFORMATION_MESSAGE);

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
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jScrollPane3 = new javax.swing.JScrollPane();
        TabelStokBarangMenipis = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        TabelStokKeseluruhan = new javax.swing.JTable();
        ButtonPerbarui = new javax.swing.JButton();
        IdBarang = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        ButtonSearch = new javax.swing.JButton();
        ButtonReset = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 51, 102));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Selamat Datang Kembali Staff");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel7)
                .addGap(402, 402, 402))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel7)
                .addContainerGap(18, Short.MAX_VALUE))
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

        jLabel3.setText("Stok Barang Menipis");

        TabelStokBarangMenipis.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"BRG-004", "Modem D-Link", "Jaringan", "5 pcs", null, null},
                {"BRG-005", "Kabel Fiber Optik", "Aksesoris", "	8 pcs", null, null},
                {"BRG-006", "Patch Panel", "Jaringan", "3 pcs", null, null}
            },
            new String [] {
                "ID Barang", "Nama Barang", "Jumlah", "Stok Keluar", "Stok Sisa", "Status"
            }
        ));
        jScrollPane3.setViewportView(TabelStokBarangMenipis);

        jScrollPane1.setViewportView(jScrollPane3);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 882, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 131, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));

        jLabel6.setText("Stok Barang Keseluruhan");

        TabelStokKeseluruhan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"BRG-001", "Router Cisco", "Jaringan", "50 pcs", null},
                {"BRG-002", "Switch TP-Link", "Jaringan", "80 pcs", null},
                {"BRG-003", "Kabel LAN	", "	Aksesoris", "120 pcs", null}
            },
            new String [] {
                "ID Barang", "Nama Barang", "Jumlah", "Stok Keluar", "Stok Sisa"
            }
        ));
        jScrollPane4.setViewportView(TabelStokKeseluruhan);

        ButtonPerbarui.setIcon(new javax.swing.ImageIcon("C:\\Users\\Tiaraaa\\PBO\\GUI(2)\\GUI(2)\\GUI\\src\\main\\java\\com\\pbo\\gui\\image\\Icon_Update.png")); // NOI18N
        ButtonPerbarui.setText("Perbarui");
        ButtonPerbarui.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonPerbaruiActionPerformed(evt);
            }
        });

        IdBarang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IdBarangActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel14.setText("Id barang:");

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

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(IdBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ButtonSearch)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ButtonReset)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ButtonPerbarui)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 873, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(IdBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addComponent(ButtonSearch)
                    .addComponent(ButtonReset))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(ButtonPerbarui)
                .addGap(14, 14, 14))
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
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
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
        this.setVisible(true);
    }//GEN-LAST:event_CekStokActionPerformed

    private void LogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LogoutActionPerformed
        // TODO add your handling code here:
        new Login1().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_LogoutActionPerformed

    private void BarangMasukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BarangMasukActionPerformed
        // TODO add your handling code here:
        new BarangMasuk().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_BarangMasukActionPerformed

    private void BarangKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BarangKeluarActionPerformed
        // TODO add your handling code here:
        new BarangKeluar().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_BarangKeluarActionPerformed

    private void ButtonPerbaruiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonPerbaruiActionPerformed
        // TODO add your handling code here:
        updateStokBarang();
    }//GEN-LAST:event_ButtonPerbaruiActionPerformed

    private void IdBarangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_IdBarangActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_IdBarangActionPerformed

    private void ButtonSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonSearchActionPerformed
        DefaultTableModel model = (DefaultTableModel) TabelStokKeseluruhan.getModel();

    // Bersihkan data sebelumnya (jika ada)
    model.setRowCount(0);

    // Ambil ID Barang dari inputan teks
    String idBarang = IdBarang.getText().trim(); // ganti dengan nama inputan sesuai form kamu

    // Validasi jika ID Barang kosong
    if (idBarang.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Masukkan ID Barang untuk pencarian!", "Peringatan", JOptionPane.WARNING_MESSAGE);
        return;
    }

    try {
        // Koneksi ke database
        Connection conn = GUI.mycon(); // Metode koneksi database
        String sql = "SELECT * FROM stokbarang WHERE id_barang = ?"; // Query untuk mencari berdasarkan id_barang
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, idBarang); // Set ID Barang yang dicari
        ResultSet rs = ps.executeQuery();

        // Iterasi hasil query dan tambahkan data ke model
        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getString("id_barang"),    // Kolom id_barang
                rs.getString("nama_barang"),  // Kolom nama_barang
                rs.getInt("jumlah"),          // Kolom jumlah (stok awal)
                rs.getInt("stok_keluar"),     // Kolom stok_keluar
                rs.getInt("stok_sisa")        // Kolom stok_sisa
            });
        }

        rs.close();
        ps.close();
    } catch (Exception e) {
        // Tampilkan pesan error jika terjadi masalah
        JOptionPane.showMessageDialog(this, "Gagal memuat data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_ButtonSearchActionPerformed

    private void ButtonResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonResetActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) TabelStokKeseluruhan.getModel();

    // Bersihkan data sebelumnya (jika ada)
    model.setRowCount(0);

    // Kosongkan inputan ID Barang
    IdBarang.setText(""); // ganti dengan nama inputan sesuai form kamu

    try {
        // Koneksi ke database
        Connection conn = GUI.mycon(); // Metode koneksi database
        String sql = "SELECT * FROM stokbarang"; // Query untuk mengambil semua data
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        // Iterasi hasil query dan tambahkan data ke model
        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getString("id_barang"),    // Kolom id_barang
                rs.getString("nama_barang"),  // Kolom nama_barang
                rs.getInt("jumlah"),          // Kolom jumlah (stok awal)
                rs.getInt("stok_keluar"),     // Kolom stok_keluar
                rs.getInt("stok_sisa")        // Kolom stok_sisa
            });
        }

        rs.close();
        ps.close();
    } catch (Exception e) {
        // Tampilkan pesan error jika terjadi masalah
        JOptionPane.showMessageDialog(this, "Gagal memuat data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_ButtonResetActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
     
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CekStok().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BarangKeluar;
    private javax.swing.JButton BarangMasuk;
    private javax.swing.JButton ButtonPerbarui;
    private javax.swing.JButton ButtonReset;
    private javax.swing.JButton ButtonSearch;
    private javax.swing.JButton CekStok;
    private javax.swing.JButton Dashboard;
    private javax.swing.JTextField IdBarang;
    private javax.swing.JButton Logout;
    private javax.swing.JTable TabelStokBarangMenipis;
    private javax.swing.JTable TabelStokKeseluruhan;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    // End of variables declaration//GEN-END:variables
}
