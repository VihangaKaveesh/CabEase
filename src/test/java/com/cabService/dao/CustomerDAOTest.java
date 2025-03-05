package com.cabService.dao;

import jakarta.servlet.http.HttpSession;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CustomerDAOTest {
    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @Mock
    private ResultSet mockResultSet;

    @Mock
    private HttpSession mockSession;

    private CustomerDAO customerDAO;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this); // Initialize mocks
        customerDAO = new CustomerDAO(mockConnection); // Inject the mocked connection
    }

    // Test customer registration (Success Case)
    @Test
    void testRegisterCustomer_Success() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1); // Simulate successful insertion

        boolean result = customerDAO.registerCustomer("2345667V", "sasindu", "sasindu@example.com", "password123", "0771234567");

        assertTrue(result, "Customer registration should be successful");

        verify(mockPreparedStatement, times(1)).executeUpdate();
        verify(mockPreparedStatement, times(1)).close();
    }

    // Test customer registration (Failure Case)
    @Test
    void testRegisterCustomer_Failure() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0); // Simulate failed insertion

        boolean result = customerDAO.registerCustomer("2345667V", "sasindu", "sasindu@example.com", "password123", "0771234567");

        assertFalse(result, "Customer registration should fail");

        verify(mockPreparedStatement, times(1)).executeUpdate();
        verify(mockPreparedStatement, times(1)).close();
    }

    //  Test customer validation (Successful Login)
    @Test
    void testValidateCustomer_Success() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true); // Simulate a successful login
        when(mockResultSet.getInt("CustomerID")).thenReturn(1);

        int customerId = customerDAO.validateCustomer("customer@example.com", "password123", mockSession);

        assertEquals(1, customerId, "Customer validation should return a valid ID");

        verify(mockConnection, times(1)).prepareStatement(anyString()); 
        verify(mockPreparedStatement, times(1)).setString(1, "customer@example.com");
        verify(mockPreparedStatement, times(1)).setString(2, "password123");
        verify(mockPreparedStatement, times(1)).executeQuery();
        verify(mockSession, times(1)).setAttribute("role", "customer");
        verify(mockResultSet, times(1)).close();
        verify(mockPreparedStatement, times(1)).close();
        
        
    }

    //  Test customer validation (Invalid Credentials)
    @Test
    void testValidateCustomer_Failure() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false); // Simulate login failure (wrong email/password)

        int customerId = customerDAO.validateCustomer("wrong@example.com", "wrongpass", mockSession);

        assertEquals(-1, customerId, "Customer validation should return -1 for invalid credentials");

        verify(mockConnection, times(1)).prepareStatement(anyString());
        verify(mockPreparedStatement, times(1)).setString(1, "wrong@example.com");
        verify(mockPreparedStatement, times(1)).setString(2, "wrongpass");
        verify(mockPreparedStatement, times(1)).executeQuery();
        verify(mockResultSet, times(1)).close();
        verify(mockPreparedStatement, times(1)).close();
    }

    //  Test exception handling in customer validation
    @Test
    void testValidateCustomer_ExceptionHandling() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        int customerId = customerDAO.validateCustomer("error@example.com", "errorpass", mockSession);

        assertEquals(-1, customerId, "Customer validation should return -1 when an exception occurs");

        verify(mockConnection, times(1)).prepareStatement(anyString()); // Ensure query was attempted
    }
}
