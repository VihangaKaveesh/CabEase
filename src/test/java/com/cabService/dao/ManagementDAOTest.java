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

public class ManagementDAOTest {
    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @Mock
    private ResultSet mockResultSet;

    @Mock
    private HttpSession mockSession;

    private ManagementDAO managementDAO;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this); // Initialize mocks
        managementDAO = new ManagementDAO(mockConnection); // Inject the mocked connection
    }

    //  Test management validation (Successful Login)
    @Test
    void testValidateManagement_Success() throws Exception {
        // Mock DB behavior
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true); // Simulate successful login
        when(mockResultSet.getInt("ManagementID")).thenReturn(1);

        // Call the method
        int managementId = managementDAO.validateManagement("management@example.com", "password123", mockSession);

        // Verify the behavior and assert the expected result
        assertEquals(1, managementId, "Management validation should return a valid ID");

        // Verify interaction with mocks
        verify(mockConnection, times(1)).prepareStatement(anyString());
        verify(mockPreparedStatement, times(1)).setString(1, "management@example.com");
        verify(mockPreparedStatement, times(1)).setString(2, "password123");
        verify(mockPreparedStatement, times(1)).executeQuery();
        verify(mockSession, times(1)).setAttribute("role", "management");
        verify(mockResultSet, times(1)).close();
        verify(mockPreparedStatement, times(1)).close();
    }

    //  Test management validation (Invalid Credentials)
    @Test
    void testValidateManagement_Failure() throws Exception {
        // Mock DB behavior
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false); // Simulate invalid login (wrong email/password)

        // Call the method
        int managementId = managementDAO.validateManagement("wrong@example.com", "wrongpass", mockSession);

        // Verify the behavior and assert the expected result
        assertEquals(-1, managementId, "Management validation should return -1 for invalid credentials");

        // Verify interaction with mocks
        verify(mockConnection, times(1)).prepareStatement(anyString());
        verify(mockPreparedStatement, times(1)).setString(1, "wrong@example.com");
        verify(mockPreparedStatement, times(1)).setString(2, "wrongpass");
        verify(mockPreparedStatement, times(1)).executeQuery();
        verify(mockResultSet, times(1)).close();
        verify(mockPreparedStatement, times(1)).close();
    }

    //  Test exception handling in management validation
    @Test
    void testValidateManagement_ExceptionHandling() throws Exception {
        // Simulate SQLException
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        // Call the method
        int managementId = managementDAO.validateManagement("error@example.com", "errorpass", mockSession);

        // Verify the behavior and assert the expected result
        assertEquals(-1, managementId, "Management validation should return -1 when an exception occurs");

        // Ensure that the query was attempted
        verify(mockConnection, times(1)).prepareStatement(anyString());
    }
}
