package com.cabService.controller;

import com.cabService.service.BookingServiceFacade;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.HashMap;

public class BookingHistoryServlet extends HttpServlet {

    private final BookingServiceFacade bookingServiceFacade;

    // Constructor Injection for better testability
    public BookingHistoryServlet(BookingServiceFacade bookingServiceFacade) {
        this.bookingServiceFacade = bookingServiceFacade;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null || !"customer".equals(session.getAttribute("role"))) {
            response.sendRedirect("login.jsp?message=You must log in first");
            return;
        }

        int customerId = (int) session.getAttribute("userId");

        try {
            List<HashMap<String, String>> bookings = bookingServiceFacade.getCustomerBookings(customerId);
            request.setAttribute("bookings", bookings);
            request.getRequestDispatcher("pages/bookingHistory.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Database error while fetching booking history", e);
        }
    }
}
