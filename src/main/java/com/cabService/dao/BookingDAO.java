/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cabService.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author vihan
 */
public class BookingDAO {
   private Connection connection;

    // Constructor for production
    public BookingDAO() throws SQLException {
        this.connection = DBConnection.getConnection(); // Original connection setup
    }

    // Constructor for testing (dependency injection)
    public BookingDAO(Connection connection) {
        this.connection = connection; // Allows injection of mocked connection for testing
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
     
       public List<HashMap<String, String>> getCustomerBookings(int customerID) throws SQLException {
        List<HashMap<String, String>> bookings = new ArrayList<>();

        String sql = "SELECT b.BookingID, b.PickupLocation, b.DropoffLocation, b.BookingDate, " +
                     "p.VehicleType, p.Price, b.Status " +
                     "FROM bookings b " +
                     "JOIN ridepackages p ON b.PackageID = p.PackageID " +
                     "WHERE b.CustomerID = ? " +
                     "ORDER BY b.BookingDate DESC";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
             
            ps.setInt(1, customerID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                HashMap<String, String> booking = new HashMap<>();
                booking.put("BookingID", String.valueOf(rs.getInt("BookingID")));
                booking.put("PickupLocation", rs.getString("PickupLocation"));
                booking.put("DropoffLocation", rs.getString("DropoffLocation"));

                Timestamp bookingDate = rs.getTimestamp("BookingDate");
                booking.put("Date", bookingDate != null ? bookingDate.toString() : "N/A");

                booking.put("VehicleType", rs.getString("VehicleType"));
                booking.put("Price", String.valueOf(rs.getDouble("Price")));
                booking.put("Status", rs.getString("Status"));

                bookings.add(booking);
            }
        }
        return bookings;
    }
}
