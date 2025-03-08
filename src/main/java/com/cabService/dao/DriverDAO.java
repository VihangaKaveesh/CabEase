/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cabService.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class DriverDAO {
     private Connection connection;

    // Constructor for production
    public DriverDAO() throws SQLException {
        this.connection = DBConnection.getConnection(); // Original connection setup
    }

    // Constructor for testing (dependency injection)
    public DriverDAO(Connection connection) {
        this.connection = connection; // Allows injection of mocked connection for testing
    }

    // Add a new driver
    public void addDriver(String nic, String name, String email, String phone, String licenseNumber, String vehicleType, String vehicleModel) throws SQLException {
        String sql = "INSERT INTO Drivers (NIC, Name, Email, Phone, LicenseNumber, VehicleType, VehicleModel) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nic);
            stmt.setString(2, name);
            stmt.setString(3, email);
            stmt.setString(4, phone);
            stmt.setString(5, licenseNumber);
            stmt.setString(6, vehicleType);
            stmt.setString(7, vehicleModel);
            stmt.executeUpdate();
        }
    }

    // Get all drivers
    public List<String[]> getDrivers() throws SQLException {
        List<String[]> drivers = new ArrayList<>();
        String sql = "SELECT * FROM Drivers";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String[] driver = new String[9];
                driver[0] = String.valueOf(rs.getInt("DriverID"));
                driver[1] = rs.getString("NIC");
                driver[2] = rs.getString("Name");
                driver[3] = rs.getString("Email");
                driver[4] = rs.getString("Phone");
                driver[5] = rs.getString("LicenseNumber");
                driver[6] = rs.getString("VehicleType");
                driver[7] = rs.getString("VehicleModel");
                driver[8] = rs.getString("Status");
                drivers.add(driver);
            }
        }
        return drivers;
    }
    
    // Delete a driver
    public void deleteDriver(int driverID) throws SQLException {
        String sql = "DELETE FROM Drivers WHERE DriverID=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, driverID);
            stmt.executeUpdate();
        }
    }

}
