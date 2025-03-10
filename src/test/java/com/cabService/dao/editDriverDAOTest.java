package com.cabService.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class editDriverDAOTest {
    private DriverDAO driverDAO;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;

    @BeforeEach
    void setUp() throws SQLException {
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        driverDAO = new DriverDAO(mockConnection);
    }


    @Test
    void testUpdateDriver_Success() throws SQLException {
        driverDAO.updateDriver(1, "John Doe Updated", "john_updated@example.com", "0712345679", "Honda Civic", "Active");

        verify(mockPreparedStatement, times(1)).setString(1, "John Doe Updated");
        verify(mockPreparedStatement, times(1)).setString(2, "john_updated@example.com");
        verify(mockPreparedStatement, times(1)).setString(3, "0712345679");
        verify(mockPreparedStatement, times(1)).setString(4, "Honda Civic");
        verify(mockPreparedStatement, times(1)).setString(5, "Active");
        verify(mockPreparedStatement, times(1)).setInt(6, 1);
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test
    void testGetDriverByID_Success() throws SQLException {
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("DriverID")).thenReturn(1);
        when(mockResultSet.getString("NIC")).thenReturn("123456789V");
        when(mockResultSet.getString("Name")).thenReturn("John Doe");
        when(mockResultSet.getString("Email")).thenReturn("john@example.com");
        when(mockResultSet.getString("Phone")).thenReturn("0712345678");
        when(mockResultSet.getString("LicenseNumber")).thenReturn("B123456");
        when(mockResultSet.getString("VehicleType")).thenReturn("Car");
        when(mockResultSet.getString("VehicleModel")).thenReturn("Toyota Prius");
        when(mockResultSet.getString("Status")).thenReturn("Active");

        String[] driver = driverDAO.getDriverByID(1);

        assertNotNull(driver);
        assertEquals("1", driver[0]);
        assertEquals("123456789V", driver[1]);
        assertEquals("John Doe", driver[2]);
        assertEquals("john@example.com", driver[3]);
        assertEquals("0712345678", driver[4]);
        assertEquals("B123456", driver[5]);
        assertEquals("Car", driver[6]);
        assertEquals("Toyota Prius", driver[7]);
        assertEquals("Active", driver[8]);

        verify(mockPreparedStatement, times(1)).setInt(1, 1);
        verify(mockPreparedStatement, times(1)).executeQuery();
    }

}
