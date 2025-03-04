package com.cabService.dao;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
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

    private CustomerDAO customerDAO;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this); // Initialize mocks
        customerDAO = new CustomerDAO(mockConnection); // Inject the mocked connection
    }

    @Test
    void testRegisterCustomer_Success() throws Exception {
        // Mock DB connection and behavior
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1); // Simulate successful insertion

        // Call the method
        boolean result = customerDAO.registerCustomer("2345667V", "sasindu", "sasindu@example.com", "password123", "0771234567");

        // Verify the behavior and assert the expected result
        assertTrue(result, "Customer registration should be successful");

        verify(mockPreparedStatement, times(1)).executeUpdate();
        verify(mockPreparedStatement, times(1)).close();
        verify(mockConnection, times(1)).close();
    }

    @Test
    void testRegisterCustomer_Failure() throws Exception {
        // Mock DB connection and behavior
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0); // Simulate failed insertion

        // Call the method
        boolean result = customerDAO.registerCustomer("2345667V", "sasindu", "sasindu@example.com", "password123", "0771234567");

        // Verify the behavior and assert the expected result
        assertFalse(result, "Customer registration should fail");

        verify(mockPreparedStatement, times(1)).executeUpdate();
        verify(mockPreparedStatement, times(1)).close();
        verify(mockConnection, times(1)).close();
    }
}
