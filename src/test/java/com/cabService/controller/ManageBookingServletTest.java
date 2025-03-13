package com.cabService.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.cabService.service.BookingService;
import java.sql.SQLException;

public class ManageBookingServletTest {

    @Mock
    private HttpServletRequest mockRequest;
    
    @Mock
    private HttpServletResponse mockResponse;

    @Mock
    private HttpSession mockSession;

    @Mock
    private BookingService mockBookingService;

    private ManageBookingServlet manageBookingServlet;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        manageBookingServlet = new ManageBookingServlet(mockBookingService); // Inject mock service
        when(mockRequest.getSession()).thenReturn(mockSession); // Mock session
    }

    @Test
    void testDoPost_UpdateBookingStatusSuccess() throws ServletException, IOException, SQLException {
        // Simulate form input
        when(mockRequest.getParameter("bookingID")).thenReturn("1");
        when(mockRequest.getParameter("action")).thenReturn("Complete");

        // Simulate successful status update
        when(mockBookingService.updateBookingStatus(anyInt(), anyString())).thenReturn(true);

        // Run servlet doPost()
        manageBookingServlet.doPost(mockRequest, mockResponse);

        // Verify redirection to the manage bookings page with success message
        verify(mockResponse).sendRedirect("pages/manageBookings.jsp?message=Booking updated successfully!");
    }

    @Test
    void testDoPost_UpdateBookingStatusFailure() throws ServletException, IOException, SQLException {
        // Simulate form input
        when(mockRequest.getParameter("bookingID")).thenReturn("1");
        when(mockRequest.getParameter("action")).thenReturn("InvalidAction");

        // Simulate failed status update
        when(mockBookingService.updateBookingStatus(anyInt(), anyString())).thenReturn(false);

        // Run servlet doPost()
        manageBookingServlet.doPost(mockRequest, mockResponse);

        // Verify redirection to the manage bookings page with failure message
        verify(mockResponse).sendRedirect("pages/manageBookings.jsp?message=Failed to update booking.");
    }

    @Test
    void testDoPost_InvalidBookingID() throws ServletException, IOException {
        // Simulate invalid booking ID
        when(mockRequest.getParameter("bookingID")).thenReturn("invalidID");
        when(mockRequest.getParameter("action")).thenReturn("Complete");

        // Run servlet doPost()
        manageBookingServlet.doPost(mockRequest, mockResponse);

        // Verify redirection to the manage bookings page with error message
        verify(mockResponse).sendRedirect("pages/manageBookings.jsp?message=Invalid booking ID format.");
    }
}
