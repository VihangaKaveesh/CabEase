package com.cabService.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import jakarta.servlet.ServletException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.cabService.dao.BookingDAO;
import com.cabService.dao.DBConnection;
import com.cabService.dao.DriverAssignDAO;
import java.sql.SQLException;

public class DriverAssignServlet extends HttpServlet {
    
    
    private DriverAssignDAO driverAssignDAO;

    // Default constructor for production use
    public DriverAssignServlet() throws SQLException {
        this.driverAssignDAO = new DriverAssignDAO();
    }

    // Constructor for testing (injects a mock DAO)
    public DriverAssignServlet(DriverAssignDAO driverAssignDAO) {
        this.driverAssignDAO = driverAssignDAO;
    }
    
     protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int bookingID = Integer.parseInt(request.getParameter("bookingID"));
            int driverID = Integer.parseInt(request.getParameter("driverID"));

            boolean success = driverAssignDAO.assignDriverToBooking(bookingID, driverID);

            if (success) {
                response.sendRedirect("pages/driverAssign.jsp?message=Driver assigned successfully");
            } else {
                response.sendRedirect("pages/driverAssign.jsp?message=Failed to assign driver");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("pages/driverAssign.jsp?message=Invalid input data");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("pages/driverAssign.jsp?message=Error occurred while assigning driver");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ArrayList<String[]> pendingBookings = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT b.BookingID, b.CustomerID, b.PickupLocation, b.DropoffLocation, p.VehicleType " +
                         "FROM Bookings b " +
                         "JOIN Packages p ON b.PackageID = p.PackageID " +
                         "WHERE b.Status = 'Pending'";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String[] booking = new String[5];
                booking[0] = String.valueOf(rs.getInt("BookingID"));
                booking[1] = String.valueOf(rs.getInt("CustomerID"));
                booking[2] = rs.getString("PickupLocation");
                booking[3] = rs.getString("DropoffLocation");
                booking[4] = rs.getString("VehicleType");
                pendingBookings.add(booking);
            }

            request.setAttribute("pendingBookings", pendingBookings);
            RequestDispatcher dispatcher = request.getRequestDispatcher("driverAssign.jsp");
            dispatcher.forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("pages/driverAssign.jsp?message=Error fetching pending bookings");
        }
    }
}
