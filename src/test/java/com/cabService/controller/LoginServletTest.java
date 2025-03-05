package com.cabService.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.cabService.dao.CustomerDAO;
import com.cabService.dao.ManagementDAO;

public class LoginServletTest {

    @Mock
    private HttpServletRequest mockRequest;
    
    @Mock
    private HttpServletResponse mockResponse;

    @Mock
    private HttpSession mockSession;

    @Mock
    private CustomerDAO mockCustomerDAO;

    @Mock
    private ManagementDAO mockManagementDAO;

    private LoginServlet loginServlet;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        loginServlet = new LoginServlet(mockCustomerDAO, mockManagementDAO); // Inject mocks
        when(mockRequest.getSession()).thenReturn(mockSession); // Mock session
    }

    @Test
    void testDoPost_CustomerLoginSuccess() throws ServletException, IOException {
        // Simulate form input
        when(mockRequest.getParameter("email")).thenReturn("customer@example.com");
        when(mockRequest.getParameter("password")).thenReturn("password123");

        // Simulate customer validation success
        when(mockCustomerDAO.validateCustomer(anyString(), anyString(), any())).thenReturn(1);

        // Run servlet doPost()
        loginServlet.doPost(mockRequest, mockResponse);

        // Verify session attribute and redirection
        verify(mockSession).setAttribute("userId", 1);
        verify(mockResponse).sendRedirect("pages/customerDashboard.jsp?message=Welcome Customer!");
    }

    @Test
    void testDoPost_ManagementLoginSuccess() throws ServletException, IOException {
        // Simulate form input
        when(mockRequest.getParameter("email")).thenReturn("manager@example.com");
        when(mockRequest.getParameter("password")).thenReturn("managerpass");

        // Simulate management validation success
        when(mockCustomerDAO.validateCustomer(anyString(), anyString(), any())).thenReturn(0);
        when(mockManagementDAO.validateManagement(anyString(), anyString(), any())).thenReturn(2);

        // Run servlet doPost()
        loginServlet.doPost(mockRequest, mockResponse);

        // Verify session attribute and redirection
        verify(mockSession).setAttribute("userId", 2);
        verify(mockResponse).sendRedirect("pages/managementDashboard.jsp?message=Welcome Manager!");
    }

    @Test
    void testDoPost_InvalidCredentials() throws ServletException, IOException {
        // Simulate form input
        when(mockRequest.getParameter("email")).thenReturn("invalid@example.com");
        when(mockRequest.getParameter("password")).thenReturn("wrongpass");

        // Simulate both customer and management validation failure
        when(mockCustomerDAO.validateCustomer(anyString(), anyString(), any())).thenReturn(0);
        when(mockManagementDAO.validateManagement(anyString(), anyString(), any())).thenReturn(0);

        // Run servlet doPost()
        loginServlet.doPost(mockRequest, mockResponse);

        // Verify redirection to login page with error message
        verify(mockResponse).sendRedirect("pages/login.jsp?message=Invalid credentials");
    }

    @Test
    void testDoPost_ExceptionHandling() throws ServletException, IOException {
        // Simulate form input
        when(mockRequest.getParameter("email")).thenReturn("error@example.com");
        when(mockRequest.getParameter("password")).thenReturn("errorpass");

        // Simulate an exception thrown during validation
        when(mockCustomerDAO.validateCustomer(anyString(), anyString(), any())).thenThrow(new RuntimeException("Database error"));

        // Run servlet doPost()
        loginServlet.doPost(mockRequest, mockResponse);

        // Verify redirection to login page with generic error message
        verify(mockResponse).sendRedirect("pages/login.jsp?message=An error occurred during login. Please try again.");
    }
}
