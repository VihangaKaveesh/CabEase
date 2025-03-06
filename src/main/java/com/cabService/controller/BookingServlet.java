/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.cabService.controller;

import com.cabService.dao.BookingDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;


public class BookingServlet extends HttpServlet {

     private BookingDAO bookingDAO;

    public BookingServlet() throws SQLException {
        this.bookingDAO = new BookingDAO();
    }

    public BookingServlet(BookingDAO bookingDAO) {
        this.bookingDAO = bookingDAO;
    }

     protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int customerId = Integer.parseInt(request.getParameter("customerId"));
            String pickupLocation = request.getParameter("pickupLocation");
            String dropoffLocation = request.getParameter("dropoffLocation");
            int packageId = Integer.parseInt(request.getParameter("packageId"));

            boolean success = bookingDAO.addBooking(customerId, pickupLocation, dropoffLocation, packageId);
            if (success) {
                response.sendRedirect("pages/customerDashboard.jsp?message=Ride request submitted successfully!");
            } else {
                response.sendRedirect("pages/rideRequest.jsp?message=Failed to request ride.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("pages/rideRequest.jsp?message=Invalid input.");
        }
    }
}
