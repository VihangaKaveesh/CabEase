package com.cabService.controller;

import com.cabService.service.DriverAssignService;
import jakarta.servlet.ServletException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class DriverAssignServlet extends HttpServlet {

    private DriverAssignService driverAssignService;

    // Default constructor for production use
    public DriverAssignServlet() throws SQLException {
        this.driverAssignService = new DriverAssignService();
    }

    // Constructor for testing (injects a mock service)
    public DriverAssignServlet(DriverAssignService driverAssignService) {
        this.driverAssignService = driverAssignService;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int bookingID = Integer.parseInt(request.getParameter("bookingID"));
            int driverID = Integer.parseInt(request.getParameter("driverID"));

            boolean success = driverAssignService.assignDriver(bookingID, driverID);

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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<Map<String, String>> pendingBookings = driverAssignService.getPendingBookings();

            request.setAttribute("pendingBookings", pendingBookings);
            RequestDispatcher dispatcher = request.getRequestDispatcher("driverAssign.jsp");
            dispatcher.forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("pages/driverAssign.jsp?message=Error fetching pending bookings");
        }
    }
}
