package com.pbo.gui;

import com.mysql.jdbc.Driver;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class db {
    
    private static Connection conn;
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/pergudangan";
        String user = "root";
        String password = "";
        
        try {
            Driver driver = new Driver();
            DriverManager.registerDriver(driver);
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Koneksi Berhasil");
            Statement statement = conn.createStatement();
            
            String sql = "INSERT INTO supplier values('8', 'Toko Jawa', '08656644667', 'Beji', 'jawa@gmail.com', 'Aktif')";
            statement.execute(sql);
            System.out.println("SQL Berhasil");
        } catch (SQLException ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Koneksi Gagal");
        }
    }
}
