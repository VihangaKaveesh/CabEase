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
       
       public boolean updateBookingStatus(int bookingID, String status) {
    boolean updated = false;
    //Connection conn = null;
    PreparedStatement ps = null;
    
    try {
        //conn = DBConnection.getConnection();
        connection.setAutoCommit(false); // Start transaction
        
        // Update booking status
        String sql = "UPDATE Bookings SET Status = ? WHERE BookingID = ?";
        ps = connection.prepareStatement(sql);
        ps.setString(1, status);
        ps.setInt(2, bookingID);
        int rowsAffected = ps.executeUpdate();
        
        if (rowsAffected > 0 && "Completed".equals(status)) {
            // Retrieve driverID associated with the booking
            String getDriverSql = "SELECT DriverID FROM Bookings WHERE BookingID = ?";
            try (PreparedStatement psDriver = connection.prepareStatement(getDriverSql)) {
                psDriver.setInt(1, bookingID);
                ResultSet rs = psDriver.executeQuery();
                
                if (rs.next()) {
                    int driverID = rs.getInt("DriverID");

                    if (driverID > 0) { // Ensure driverID exists
                        // Update the driver's status to "Available"
                        String updateDriverSql = "UPDATE Drivers SET Status = 'Available' WHERE DriverID = ?";
                        try (PreparedStatement psUpdateDriver = connection.prepareStatement(updateDriverSql)) {
                            psUpdateDriver.setInt(1, driverID);
                            psUpdateDriver.executeUpdate();
                        }
                    }
                }
            }
        }

        connection.commit(); // Commit transaction
        updated = true;
    } catch (Exception e) {
        if (connection != null) {
            try {
                connection.rollback(); // Rollback on error
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        e.printStackTrace();
    } finally {
        try {
            if (ps != null) ps.close();
            if (connection != null) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    return updated;
}
       
      public static HashMap<String, String> getReceiptDetails(int BookingID, Connection conn) {
        HashMap<String, String> receiptDetails = new HashMap<>();
        String sql = "SELECT b.BookingID, b.PickupLocation, b.DropoffLocation, b.BookingDate, " +
                     "p.VehicleType, p.Price, " +
                     "d.Name, d.Phone, d.VehicleModel, d.LicenseNumber " +
                     "FROM bookings b " +
                     "JOIN ridepackages p ON b.PackageID = p.PackageID " +
                     "JOIN Drivers d ON b.DriverID = d.DriverID " +
                     "WHERE b.BookingID = ? AND b.Status IN ('Assigned', 'Completed')";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, BookingID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    receiptDetails.put("BookingID", String.valueOf(rs.getInt("BookingID")));
                    receiptDetails.put("PickupLocation", rs.getString("PickupLocation"));
                    receiptDetails.put("DropoffLocation", rs.getString("DropoffLocation"));
                    receiptDetails.put("Date", rs.getString("BookingDate"));
                    receiptDetails.put("VehicleType", rs.getString("VehicleType"));
                    receiptDetails.put("Price", String.valueOf(rs.getDouble("Price")));
                    receiptDetails.put("VehicleModel", rs.getString("VehicleModel"));
                    receiptDetails.put("LicenseNumber", rs.getString("LicenseNumber"));
                    receiptDetails.put("DriverName", rs.getString("Name"));
                    receiptDetails.put("Phone", rs.getString("Phone"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return receiptDetails;
    }
}
