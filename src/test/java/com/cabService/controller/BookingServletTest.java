package com.cabService.controller;


import com.cabService.service.BookingServletFacade;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.mockito.Mockito.*;

class BookingServletTest {
    private BookingServlet bookingServlet;
    private BookingServletFacade mockBookingFacade;
    private HttpServletRequest mockRequest;
    private HttpServletResponse mockResponse;

    @BeforeEach
    void setUp() {
        mockBookingFacade = mock(BookingServletFacade.class);
        bookingServlet = new BookingServlet(mockBookingFacade);
        mockRequest = mock(HttpServletRequest.class);
        mockResponse = mock(HttpServletResponse.class);
    }

    @Test
    void testDoPost_SuccessfulBooking() throws ServletException, IOException {
        when(mockRequest.getParameter("customerId")).thenReturn("1");
        when(mockRequest.getParameter("pickupLocation")).thenReturn("Location A");
        when(mockRequest.getParameter("dropoffLocation")).thenReturn("Location B");
        when(mockRequest.getParameter("packageId")).thenReturn("2");
        when(mockBookingFacade.handleBookingRequest(1, "Location A", "Location B", 2)).thenReturn(true);

        bookingServlet.doPost(mockRequest, mockResponse);

        verify(mockResponse).sendRedirect("pages/customerDashboard.jsp?message=Ride request submitted successfully!");
    }

    @Test
    void testDoPost_FailedBooking() throws ServletException, IOException {
        when(mockRequest.getParameter("customerId")).thenReturn("1");
        when(mockRequest.getParameter("pickupLocation")).thenReturn("Location A");
        when(mockRequest.getParameter("dropoffLocation")).thenReturn("Location B");
        when(mockRequest.getParameter("packageId")).thenReturn("2");
        when(mockBookingFacade.handleBookingRequest(1, "Location A", "Location B", 2)).thenReturn(false);

        bookingServlet.doPost(mockRequest, mockResponse);

        verify(mockResponse).sendRedirect("pages/rideRequest.jsp?message=Failed to request ride.");
    }

    @Test
    void testDoPost_InvalidInput() throws ServletException, IOException {
        when(mockRequest.getParameter("customerId")).thenReturn("abc"); // Invalid ID format

        bookingServlet.doPost(mockRequest, mockResponse);

        verify(mockResponse).sendRedirect("pages/rideRequest.jsp?message=Invalid input.");
    }
}
