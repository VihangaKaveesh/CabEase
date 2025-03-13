package com.cabService.controller;


import com.cabService.service.BookingServletFacade;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class BookingServlet extends HttpServlet {

    private BookingServletFacade bookingFacade;

    public BookingServlet() {
        try {
            this.bookingFacade = new BookingServletFacade();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public BookingServlet(BookingServletFacade bookingFacade) {
        this.bookingFacade = bookingFacade;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int customerId = Integer.parseInt(request.getParameter("customerId"));
            String pickupLocation = request.getParameter("pickupLocation");
            String dropoffLocation = request.getParameter("dropoffLocation");
            int packageId = Integer.parseInt(request.getParameter("packageId"));

            boolean success = bookingFacade.handleBookingRequest(customerId, pickupLocation, dropoffLocation, packageId);
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
