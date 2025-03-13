package com.cabService.controller;

import com.cabService.service.DriverAssignService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;

import static org.mockito.Mockito.*;

class DriverAssignServletTest {

    private DriverAssignServlet driverAssignServlet;
    private DriverAssignService mockDriverAssignService;
    private HttpServletRequest mockRequest;
    private HttpServletResponse mockResponse;

    @BeforeEach
    void setUp() {
        mockDriverAssignService = mock(DriverAssignService.class);
        driverAssignServlet = new DriverAssignServlet(mockDriverAssignService);
        mockRequest = mock(HttpServletRequest.class);
        mockResponse = mock(HttpServletResponse.class);
    }

    @Test
    void testDoPost_AssignDriver_Success() throws ServletException, IOException, SQLException {
        when(mockRequest.getParameter("bookingID")).thenReturn("55");
        when(mockRequest.getParameter("driverID")).thenReturn("34");
        when(mockDriverAssignService.assignDriver(55, 34)).thenReturn(true);

        driverAssignServlet.doPost(mockRequest, mockResponse);

        verify(mockDriverAssignService).assignDriver(55, 34);
        verify(mockResponse).sendRedirect("pages/driverAssign.jsp?message=Driver assigned successfully");
    }

    @Test
    void testDoPost_AssignDriver_Failure() throws ServletException, IOException, SQLException {
        when(mockRequest.getParameter("bookingID")).thenReturn("102");
        when(mockRequest.getParameter("driverID")).thenReturn("6");
        when(mockDriverAssignService.assignDriver(102, 6)).thenReturn(false);

        driverAssignServlet.doPost(mockRequest, mockResponse);

        verify(mockDriverAssignService).assignDriver(102, 6);
        verify(mockResponse).sendRedirect("pages/driverAssign.jsp?message=Failed to assign driver");
    }

    @Test
    void testDoPost_InvalidInput() throws ServletException, IOException {
        when(mockRequest.getParameter("bookingID")).thenReturn("abc"); // Invalid number format
        when(mockRequest.getParameter("driverID")).thenReturn("12");

        driverAssignServlet.doPost(mockRequest, mockResponse);

        verify(mockResponse).sendRedirect("pages/driverAssign.jsp?message=Invalid input data");
    }

    @Test
    void testDoPost_ExceptionHandling() throws ServletException, IOException, SQLException {
        when(mockRequest.getParameter("bookingID")).thenReturn("105");
        when(mockRequest.getParameter("driverID")).thenReturn("8");
        doThrow(new SQLException("DB error")).when(mockDriverAssignService).assignDriver(105, 8);

        driverAssignServlet.doPost(mockRequest, mockResponse);

        verify(mockResponse).sendRedirect("pages/driverAssign.jsp?message=Error occurred while assigning driver");
    }

    @Test
    void testDoGet_FetchPendingBookings_Success() throws ServletException, IOException, SQLException {
        when(mockDriverAssignService.getPendingBookings()).thenReturn(Collections.emptyList());

        driverAssignServlet.doGet(mockRequest, mockResponse);

        verify(mockRequest).setAttribute(eq("pendingBookings"), any());
    }

    @Test
    void testDoGet_ExceptionHandling() throws ServletException, IOException, SQLException {
        doThrow(new SQLException("DB error")).when(mockDriverAssignService).getPendingBookings();

        driverAssignServlet.doGet(mockRequest, mockResponse);

        verify(mockResponse).sendRedirect("pages/driverAssign.jsp?message=Error fetching pending bookings");
    }
}
