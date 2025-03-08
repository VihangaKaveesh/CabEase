package com.cabService.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DriverDAOTest {
    private DriverDAO driverDAO;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private Statement mockStatement;
    private ResultSet mockResultSet;

    @BeforeEach
    void setUp() throws SQLException {
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockStatement = mock(Statement.class);
        mockResultSet = mock(ResultSet.class);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        driverDAO = new DriverDAO(mockConnection);
    }

    @Test
    void testAddDriver_Success() throws SQLException {
        driverDAO.addDriver("123456789V", "John Doe", "john@example.com", "0712345678", "B123456", "Car", "Toyota Prius");

        verify(mockPreparedStatement, times(1)).setString(1, "123456789V");
        verify(mockPreparedStatement, times(1)).setString(2, "John Doe");
        verify(mockPreparedStatement, times(1)).setString(3, "john@example.com");
        verify(mockPreparedStatement, times(1)).setString(4, "0712345678");
        verify(mockPreparedStatement, times(1)).setString(5, "B123456");
        verify(mockPreparedStatement, times(1)).setString(6, "Car");
        verify(mockPreparedStatement, times(1)).setString(7, "Toyota Prius");
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test
    void testGetDrivers_Success() throws SQLException {
        when(mockStatement.executeQuery("SELECT * FROM Drivers")).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getInt("DriverID")).thenReturn(1);
        when(mockResultSet.getString("NIC")).thenReturn("123456789V");
        when(mockResultSet.getString("Name")).thenReturn("John Doe");
        when(mockResultSet.getString("Email")).thenReturn("john@example.com");
        when(mockResultSet.getString("Phone")).thenReturn("0712345678");
        when(mockResultSet.getString("LicenseNumber")).thenReturn("B123456");
        when(mockResultSet.getString("VehicleType")).thenReturn("Car");
        when(mockResultSet.getString("VehicleModel")).thenReturn("Toyota Prius");
        when(mockResultSet.getString("Status")).thenReturn("Active");

        List<String[]> drivers = driverDAO.getDrivers();

        assertEquals(1, drivers.size());
        assertEquals("123456789V", drivers.get(0)[1]);
        assertEquals("John Doe", drivers.get(0)[2]);
        assertEquals("john@example.com", drivers.get(0)[3]);
    }

    @Test
    void testDeleteDriver_Success() throws SQLException {
        driverDAO.deleteDriver(1);

        verify(mockPreparedStatement, times(1)).setInt(1, 1);
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }
}
