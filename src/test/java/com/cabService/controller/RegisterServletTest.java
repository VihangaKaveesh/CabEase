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

import com.cabService.service.RegistrationService;
import java.sql.SQLException;

public class RegisterServletTest {

    @Mock
    private HttpServletRequest mockRequest;
    
    @Mock
    private HttpServletResponse mockResponse;

    @Mock
    private RegistrationService mockRegistrationService; // Mocking the facade service

    private RegisterServlet registerServlet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        registerServlet = new RegisterServlet(mockRegistrationService); // Inject mocked service
    }

    @Test
    void testDoPost_Success() throws ServletException, IOException, SQLException {
        // Simulate form input
        when(mockRequest.getParameter("nic")).thenReturn("2345667V");
        when(mockRequest.getParameter("name")).thenReturn("sasindu");
        when(mockRequest.getParameter("email")).thenReturn("sasindu@example.com");
        when(mockRequest.getParameter("password")).thenReturn("password123");
        when(mockRequest.getParameter("phone")).thenReturn("0771234567");

        // Simulate successful registration via service
        when(mockRegistrationService.registerCustomer(anyString(), anyString(), anyString(), anyString(), anyString()))
            .thenReturn(true);

        // Run servlet doPost()
        registerServlet.doPost(mockRequest, mockResponse);

        // Verify redirect for success
        verify(mockResponse).sendRedirect("pages/register.jsp?message=Registration successful! You can now log in.");
    }

    @Test
    void testDoPost_Failure() throws ServletException, IOException, SQLException {
        // Simulate form input
        when(mockRequest.getParameter("nic")).thenReturn("2345667V");
        when(mockRequest.getParameter("name")).thenReturn("sasindu");
        when(mockRequest.getParameter("email")).thenReturn("sasindu@example.com");
        when(mockRequest.getParameter("password")).thenReturn("password123");
        when(mockRequest.getParameter("phone")).thenReturn("0771234567");

        // Simulate failed registration via service
        when(mockRegistrationService.registerCustomer(anyString(), anyString(), anyString(), anyString(), anyString()))
            .thenReturn(false);

        // Run servlet doPost()
        registerServlet.doPost(mockRequest, mockResponse);

        // Verify redirect for failure
        verify(mockResponse).sendRedirect("pages/register.jsp?message=Registration failed. Try again.");
    }
}
