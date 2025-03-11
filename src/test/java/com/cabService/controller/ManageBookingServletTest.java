package com.cabService.controller;

import com.cabService.dao.BookingDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.io.IOException;

import static org.mockito.Mockito.*;

class ManageBookingServletTest {

    private ManageBookingServlet servlet;
    private BookingDAO bookingDAO;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        bookingDAO = mock(BookingDAO.class);
        servlet = new ManageBookingServlet(bookingDAO); // Use dependency injection

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @Test
    void testDoPost_CompleteBooking_Success() throws ServletException, IOException {
        when(request.getParameter("bookingID")).thenReturn("63");
        when(request.getParameter("action")).thenReturn("Complete");
        when(bookingDAO.updateBookingStatus(63, "Completed")).thenReturn(true);

        servlet.doPost(request, response);

        verify(response).sendRedirect("pages/manageBookings.jsp?message=Booking updated successfully!");
    }

    @Test
    void testDoPost_RejectBooking_Failure() throws ServletException, IOException {
        when(request.getParameter("bookingID")).thenReturn("2");
        when(request.getParameter("action")).thenReturn("Reject");
        when(bookingDAO.updateBookingStatus(2, "Rejected")).thenReturn(false);

        servlet.doPost(request, response);

        verify(response).sendRedirect("pages/manageBookings.jsp?message=Failed to update booking.");
    }

    @Test
    void testDoPost_InvalidBookingIDFormat() throws ServletException, IOException {
        when(request.getParameter("bookingID")).thenReturn("invalid");
        when(request.getParameter("action")).thenReturn("Complete");

        servlet.doPost(request, response);

        verify(response).sendRedirect("pages/manageBookings.jsp?message=Invalid booking ID format.");
    }

    @Test
    void testDoPost_InvalidAction() throws ServletException, IOException {
        when(request.getParameter("bookingID")).thenReturn("1");
        when(request.getParameter("action")).thenReturn("InvalidAction");

        servlet.doPost(request, response);

        verify(response).sendRedirect("pages/manageBookings.jsp?message=Invalid action.");
    }

    @Test
    void testDoPost_MissingParameters() throws ServletException, IOException {
        when(request.getParameter("bookingID")).thenReturn(null);
        when(request.getParameter("action")).thenReturn("Complete");

        servlet.doPost(request, response);

        verify(response).sendRedirect("pages/manageBookings.jsp?message=Invalid request parameters.");
    }
}
