package com.cabService.controller;

import com.cabService.dao.DriverDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

class DriverServletTest {
    private DriverServlet driverServlet;
    private DriverDAO mockDriverDAO;
    private HttpServletRequest mockRequest;
    private HttpServletResponse mockResponse;

    @BeforeEach
    void setUp() throws Exception {
        mockDriverDAO = mock(DriverDAO.class);
        driverServlet = new DriverServlet();
        driverServlet.driverDAO = mockDriverDAO; // Injecting mock DAO
        mockRequest = mock(HttpServletRequest.class);
        mockResponse = mock(HttpServletResponse.class);
    }

    @Test
    void testDoPost_AddDriver_Success() throws ServletException, IOException, SQLException {
        when(mockRequest.getParameter("action")).thenReturn("add");
        when(mockRequest.getParameter("nic")).thenReturn("123456789V");
        when(mockRequest.getParameter("name")).thenReturn("John Doe");
        when(mockRequest.getParameter("email")).thenReturn("john@example.com");
        when(mockRequest.getParameter("phone")).thenReturn("0712345678");
        when(mockRequest.getParameter("licenseNumber")).thenReturn("B123456");
        when(mockRequest.getParameter("vehicleType")).thenReturn("Car");
        when(mockRequest.getParameter("vehicleModel")).thenReturn("Toyota Prius");

        driverServlet.doPost(mockRequest, mockResponse);

        verify(mockDriverDAO).addDriver("123456789V", "John Doe", "john@example.com", "0712345678", "B123456", "Car", "Toyota Prius");
        verify(mockResponse).sendRedirect("pages/manageDrivers.jsp?message= Driver added ");
    }

    @Test
    void testDoPost_DeleteDriver_Success() throws ServletException, IOException, SQLException {
        when(mockRequest.getParameter("action")).thenReturn("delete");
        when(mockRequest.getParameter("driverID")).thenReturn("1");

        driverServlet.doPost(mockRequest, mockResponse);

        verify(mockDriverDAO).deleteDriver(1);
        verify(mockResponse).sendRedirect("pages/manageDrivers.jsp?message= Driver deleted successfully");
    }

    @Test
    void testDoPost_InvalidAction() throws ServletException, IOException {
        when(mockRequest.getParameter("action")).thenReturn("invalid");

        driverServlet.doPost(mockRequest, mockResponse);

        verify(mockResponse).sendRedirect("manageDrivers.jsp");
    }
}
