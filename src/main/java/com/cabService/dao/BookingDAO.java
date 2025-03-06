/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cabService.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author vihan
 */
public class BookingDAO {
    private Connection connection;

    // Constructor to inject a connection (for testing)
    public BookingDAO(Connection connection) {
        this.connection = connection;
    }

    // Default constructor for real usage
    public BookingDAO() throws SQLException {
        this.connection = DBConnection.getConnection(); // Keep real connection for non-test cases
    }
    
     public boolean addBooking(int customerId, String pickupLocation, String dropoffLocation, int packageId) {
    Connection conn = null;
    PreparedStatement pstmt = null;

    try {
        //conn = DBConnection.getConnection();

        // Insert booking (without VehicleType & Price since they were dropped)
        String insertQuery = "INSERT INTO Bookings (CustomerID, PickupLocation, DropoffLocation, PackageID, Status) " +
                             "VALUES (?, ?, ?, ?, 'Pending')";
        pstmt = connection.prepareStatement(insertQuery);
        pstmt.setInt(1, customerId);
        pstmt.setString(2, pickupLocation);
        pstmt.setString(3, dropoffLocation);
        pstmt.setInt(4, packageId);

        return pstmt.executeUpdate() > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    } finally {
        try {
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
}
