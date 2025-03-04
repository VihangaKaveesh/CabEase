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

import com.cabService.dao.CustomerDAO;

public class RegisterServletTest {
    @Mock
    private HttpServletRequest mockRequest;
    
    @Mock
    private HttpServletResponse mockResponse;

    @Mock
    private CustomerDAO mockCustomerDAO;

    private RegisterServlet registerServlet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        registerServlet = new RegisterServlet(mockCustomerDAO); // Inject mocked DAO
    }

    @Test
    void testDoPost_Success() throws ServletException, IOException {
        // Simulate form input
        when(mockRequest.getParameter("nic")).thenReturn("2345667V");
        when(mockRequest.getParameter("name")).thenReturn("sasindu");
        when(mockRequest.getParameter("email")).thenReturn("sasindu@example.com");
        when(mockRequest.getParameter("password")).thenReturn("password123");
        when(mockRequest.getParameter("phone")).thenReturn("0771234567");

        // Simulate successful registration
        when(mockCustomerDAO.registerCustomer(anyString(), anyString(), anyString(), anyString(), anyString()))
            .thenReturn(true);

        // Run servlet doPost()
        registerServlet.doPost(mockRequest, mockResponse);

        // Verify redirect for success
        verify(mockResponse).sendRedirect("pages/register.jsp?message=Registration successful! You can now log in.");
    }

    @Test
    void testDoPost_Failure() throws ServletException, IOException {
        // Simulate form input
        when(mockRequest.getParameter("nic")).thenReturn("2345667V");
        when(mockRequest.getParameter("name")).thenReturn("sasindu");
        when(mockRequest.getParameter("email")).thenReturn("sasindu@example.com");
        when(mockRequest.getParameter("password")).thenReturn("password123");
        when(mockRequest.getParameter("phone")).thenReturn("0771234567");

        // Simulate failed registration
        when(mockCustomerDAO.registerCustomer(anyString(), anyString(), anyString(), anyString(), anyString()))
            .thenReturn(false);

        // Run servlet doPost()
        registerServlet.doPost(mockRequest, mockResponse);

        // Verify redirect for failure
        verify(mockResponse).sendRedirect("pages/register.jsp?message=Registration failed. Try again.");
    }
}
