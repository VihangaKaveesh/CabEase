package com.cabService.controller;

import com.cabService.dao.DriverAssignDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.io.IOException;
import static org.mockito.Mockito.*;

class DriverAssignServletTest {

    private DriverAssignServlet driverAssignServlet;
    private DriverAssignDAO mockDriverAssignDAO;
    private HttpServletRequest mockRequest;
    private HttpServletResponse mockResponse;

    @BeforeEach
    void setUp() {
        mockDriverAssignDAO = mock(DriverAssignDAO.class);
        driverAssignServlet = new DriverAssignServlet(mockDriverAssignDAO);
        mockRequest = mock(HttpServletRequest.class);
        mockResponse = mock(HttpServletResponse.class);
    }

    @Test
    void testDoPost_AssignDriver_Success() throws ServletException, IOException {
        when(mockRequest.getParameter("bookingID")).thenReturn("55");
        when(mockRequest.getParameter("driverID")).thenReturn("34");
        when(mockDriverAssignDAO.assignDriverToBooking(55, 34)).thenReturn(true);

        driverAssignServlet.doPost(mockRequest, mockResponse);

        verify(mockDriverAssignDAO).assignDriverToBooking(55, 34);
        verify(mockResponse).sendRedirect("pages/driverAssign.jsp?message=Driver assigned successfully");
    }

    @Test
    void testDoPost_AssignDriver_Failure() throws ServletException, IOException {
        when(mockRequest.getParameter("bookingID")).thenReturn("102");
        when(mockRequest.getParameter("driverID")).thenReturn("6");
        when(mockDriverAssignDAO.assignDriverToBooking(102, 6)).thenReturn(false);

        driverAssignServlet.doPost(mockRequest, mockResponse);

        verify(mockDriverAssignDAO).assignDriverToBooking(102, 6);
        verify(mockResponse).sendRedirect("pages/driverAssign.jsp?message=Failed to assign driver");
    }

 
    @Test
    void testDoPost_ExceptionHandling() throws ServletException, IOException {
        when(mockRequest.getParameter("bookingID")).thenReturn("105");
        when(mockRequest.getParameter("driverID")).thenReturn("8");
        when(mockDriverAssignDAO.assignDriverToBooking(105, 8)).thenThrow(new RuntimeException("DB error"));

        driverAssignServlet.doPost(mockRequest, mockResponse);

        verify(mockResponse).sendRedirect("pages/driverAssign.jsp?message=Error occurred while assigning driver");
    }
}
