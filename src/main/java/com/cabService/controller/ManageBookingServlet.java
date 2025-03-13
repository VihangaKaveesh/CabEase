package com.cabService.controller;

import com.cabService.dao.BookingDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.cabService.service.BookingService;
import java.sql.SQLException;


public class ManageBookingServlet extends HttpServlet {

    private BookingService bookingService;

    public ManageBookingServlet() throws SQLException {
        this.bookingService = new BookingService(new BookingDAO()); // Default constructor
    }

    public ManageBookingServlet(BookingService bookingService) {
        this.bookingService = bookingService; // Dependency injection for testing
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String redirectURL = "pages/manageBookings.jsp";
        
        try {
            // Validate booking ID
            String bookingIDParam = request.getParameter("bookingID");
            String action = request.getParameter("action");

            if (bookingIDParam == null || action == null) {
                response.sendRedirect(redirectURL + "?message=Invalid request parameters.");
                return;
            }

            int bookingID = Integer.parseInt(bookingIDParam);

            // Call service layer to update booking status
            boolean success = bookingService.updateBookingStatus(bookingID, action);

            response.sendRedirect(redirectURL + "?message=" + (success ? "Booking updated successfully!" : "Failed to update booking."));

        } catch (NumberFormatException e) {
            response.sendRedirect(redirectURL + "?message=Invalid booking ID format.");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(redirectURL + "?message=Unexpected error occurred.");
        }
    }
}
