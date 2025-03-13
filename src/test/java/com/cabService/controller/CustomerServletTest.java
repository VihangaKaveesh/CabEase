package com.cabService.controller;

import com.cabService.service.CustomerService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

class CustomerServletTest {
    private CustomerServlet customerServlet;
    private CustomerService mockCustomerService;
    private HttpServletRequest mockRequest;
    private HttpServletResponse mockResponse;

    @BeforeEach
    void setUp() throws Exception {
        mockCustomerService = mock(CustomerService.class);
        customerServlet = new CustomerServlet(mockCustomerService);
        mockRequest = mock(HttpServletRequest.class);
        mockResponse = mock(HttpServletResponse.class);
    }

    @Test
    void testDoPost_DeleteCustomer_Success() throws ServletException, IOException, SQLException {
        when(mockRequest.getParameter("action")).thenReturn("delete");
        when(mockRequest.getParameter("customerID")).thenReturn("1");
        when(mockCustomerService.deleteCustomer(1)).thenReturn(true); // Mocking service call

        customerServlet.doPost(mockRequest, mockResponse);

        verify(mockResponse).sendRedirect("pages/manageCustomers.jsp?message=Customer deleted successfully");
    }

    @Test
    void testDoPost_DeleteCustomer_Failure() throws ServletException, IOException, SQLException {
        when(mockRequest.getParameter("action")).thenReturn("delete");
        when(mockRequest.getParameter("customerID")).thenReturn("1");
        when(mockCustomerService.deleteCustomer(1)).thenReturn(false); // Mocking service failure

        customerServlet.doPost(mockRequest, mockResponse);

        verify(mockResponse).sendRedirect("pages/manageCustomers.jsp?message=Failed to delete customer");
    }

    @Test
    void testDoPost_InvalidAction() throws ServletException, IOException {
        when(mockRequest.getParameter("action")).thenReturn("invalidAction");

        customerServlet.doPost(mockRequest, mockResponse);

        verify(mockResponse).sendRedirect("pages/manageCustomers.jsp");
    }
}
