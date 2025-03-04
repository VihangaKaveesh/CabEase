/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cabService.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author vihan
 */
public class DBConnection {
      private static final String URL = "jdbc:mysql://localhost:3306/cabservice";
    private static final String USER = "root";
    private static final String PASSWORD = "admin";
    
    private static Connection connection = null;

    // Static block to initialize the database connection at the start of the application
    static {
        try {
            // Register JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver"); 
            
            // Open a connection
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            // Log successful connection
            System.out.println("Database connected successfully to " + URL);
        } 
        catch (ClassNotFoundException e) {
            // Log if the JDBC driver class is not found
            System.out.println("JDBC Driver not found: " + e.getMessage());
            e.printStackTrace();
        } 
        catch (SQLException e) {
            // Log any SQL exceptions
            System.out.println("Database connection failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
