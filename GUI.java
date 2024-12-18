package com.pbo.gui;

import com.mysql.jdbc.Driver;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class GUI {
    
    private static Connection conn; // Variabel statis untuk koneksi database

    // Method untuk mendapatkan koneksi
    public static Connection mycon() {
        if (conn == null) {
            try {
                // Informasi koneksi database
                String url = "jdbc:mysql://localhost:3306/pergudangan"; // Ganti sesuai database Anda
                String user = "root"; // Username database
                String password = ""; // Password database (kosong jika tidak ada)
                
                // Registrasi driver
                Driver driver = new Driver();
                DriverManager.registerDriver(driver);
                
                // Membuat koneksi
                conn = DriverManager.getConnection(url, user, password);
                System.out.println("Koneksi Berhasil");
            } catch (SQLException ex) {
                System.out.println("Koneksi Gagal: " + ex.getMessage());
            }
        }
        return conn; // Mengembalikan objek koneksi
    }
}
