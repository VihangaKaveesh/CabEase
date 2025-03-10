package com.cabService.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DriverAssignDAO {
    private Connection connection;

    // Constructor for production
    public DriverAssignDAO() throws SQLException {
        this.connection = DBConnection.getConnection(); // ✅ Ensure proper connection setup
    }

    // Constructor for testing (dependency injection)
    public DriverAssignDAO(Connection connection) {
        this.connection = connection; // ✅ Allows injection of mocked connection for testing
    }
    
    public boolean assignDriverToBooking(int bookingID, int driverID) {
        PreparedStatement psUpdateBooking = null;
        PreparedStatement psUpdateDriver = null;

        try {
            this.connection.setAutoCommit(false); // ✅ Use `this.connection`

            // Update booking: Assign driver and update status
            String updateBookingSQL = "UPDATE Bookings SET DriverID = ?, Status = 'Assigned' WHERE BookingID = ?";
            psUpdateBooking = this.connection.prepareStatement(updateBookingSQL);
            psUpdateBooking.setInt(1, driverID);
            psUpdateBooking.setInt(2, bookingID);
            psUpdateBooking.executeUpdate();

            // Update driver: Change status to 'Assigned'
            String updateDriverSQL = "UPDATE Drivers SET Status = 'Assigned' WHERE DriverID = ?";
            psUpdateDriver = this.connection.prepareStatement(updateDriverSQL);
            psUpdateDriver.setInt(1, driverID);
            psUpdateDriver.executeUpdate();

            this.connection.commit(); // ✅ Commit transaction
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (this.connection != null) this.connection.rollback(); // ✅ Rollback on error
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            return false;
        } finally {
            closeResources(psUpdateBooking, psUpdateDriver);
        }
    }
     
    // ✅ Close prepared statements only (not the connection)
    private void closeResources(PreparedStatement... statements) {
        try {
            for (PreparedStatement ps : statements) {
                if (ps != null) ps.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // ✅ Get pending bookings
    public static List<Map<String, String>> getPendingBookings() {
        List<Map<String, String>> pendingBookings = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT b.BookingID, b.CustomerID, b.PickupLocation, b.DropoffLocation , p.VehicleType " +
                         "FROM bookings b " +
                         "JOIN ridepackages p ON b.PackageID = p.PackageID " +
                         "WHERE b.Status = 'Pending'";

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Map<String, String> booking = new HashMap<>();
                booking.put("BookingID", rs.getString("BookingID"));
                booking.put("CustomerID", rs.getString("CustomerID"));
                booking.put("PickupLocation", rs.getString("PickupLocation"));
                booking.put("DropoffLocation", rs.getString("DropoffLocation"));
                booking.put("VehicleType", rs.getString("VehicleType"));
                pendingBookings.add(booking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }
        return pendingBookings;
    }

    // ✅ Get available drivers for a specific vehicle type
    public static List<Map<String, String>> getAvailableDrivers(String vehicleType) {
        List<Map<String, String>> availableDrivers = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT DriverID, Name FROM Drivers WHERE Status = 'Available' AND VehicleType = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, vehicleType);
            rs = ps.executeQuery();

            while (rs.next()) {
                Map<String, String> driver = new HashMap<>();
                driver.put("DriverID", rs.getString("DriverID"));
                driver.put("Name", rs.getString("Name"));
                availableDrivers.add(driver);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }
        return availableDrivers;
    }

    // ✅ Utility method to close resources
    private static void closeResources(Connection conn, PreparedStatement ps, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
