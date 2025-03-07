package com.cabService.controller;

import com.cabService.dao.CustomerDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.io.IOException;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

class CustomerServletTest {
    private CustomerServlet customerServlet;
    private CustomerDAO mockCustomerDAO;
    private HttpServletRequest mockRequest;
    private HttpServletResponse mockResponse;

    @BeforeEach
    void setUp() throws Exception {
        mockCustomerDAO = mock(CustomerDAO.class);
        customerServlet = new CustomerServlet(mockCustomerDAO);
        mockRequest = mock(HttpServletRequest.class);
        mockResponse = mock(HttpServletResponse.class);
    }

    @Test
    void testDoPost_DeleteCustomer_Success() throws ServletException, IOException, SQLException {
        when(mockRequest.getParameter("action")).thenReturn("delete");
        when(mockRequest.getParameter("customerID")).thenReturn("1");
        when(mockCustomerDAO.deleteCustomer(1)).thenReturn(true);

        customerServlet.doPost(mockRequest, mockResponse);

        verify(mockResponse).sendRedirect("pages/manageCustomers.jsp?message=Customer deleted successfully");
    }

    @Test
    void testDoPost_DeleteCustomer_Failure() throws ServletException, IOException, SQLException {
        when(mockRequest.getParameter("action")).thenReturn("delete");
        when(mockRequest.getParameter("customerID")).thenReturn("1");
        when(mockCustomerDAO.deleteCustomer(1)).thenReturn(false);

        customerServlet.doPost(mockRequest, mockResponse);

        verify(mockResponse).sendRedirect("pages/manageCustomers.jsp?message=Failed to delete customer");
    }

    @Test
    void testDoPost_InvalidAction() throws ServletException, IOException, SQLException {
        when(mockRequest.getParameter("action")).thenReturn("invalidAction");

        customerServlet.doPost(mockRequest, mockResponse);

        verify(mockResponse).sendRedirect("pages/manageCustomers.jsp");
    }
}
