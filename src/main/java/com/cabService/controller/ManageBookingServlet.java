package com.cabService.controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.cabService.dao.BookingDAO;
import java.sql.SQLException;

/**
 * Servlet for managing booking status updates.
 */
public class ManageBookingServlet extends HttpServlet {

    private BookingDAO bookingDAO;

    public ManageBookingServlet() throws SQLException {
        this.bookingDAO = new BookingDAO(); // Default constructor
    }

    public ManageBookingServlet(BookingDAO bookingDAO) {
        this.bookingDAO = bookingDAO; // Dependency injection for testing
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
            String status = "";

            if ("Complete".equals(action)) {
                status = "Completed";
            } else if ("Reject".equals(action)) {
                status = "Rejected";
            } else {
                response.sendRedirect(redirectURL + "?message=Invalid action.");
                return;
            }

            boolean success = bookingDAO.updateBookingStatus(bookingID, status);

            response.sendRedirect(redirectURL + "?message=" + (success ? "Booking updated successfully!" : "Failed to update booking."));

        } catch (NumberFormatException e) {
            response.sendRedirect(redirectURL + "?message=Invalid booking ID format.");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(redirectURL + "?message=Unexpected error occurred.");
        }
    }
}
