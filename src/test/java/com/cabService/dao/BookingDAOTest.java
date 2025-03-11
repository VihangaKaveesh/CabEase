package com.cabService.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookingDAOTest {
    private BookingDAO bookingDAO;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
     private ResultSet mockResultSet;

    @BeforeEach
    void setUp() throws SQLException {
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        bookingDAO = new BookingDAO(mockConnection);
        // Ensure the mock ResultSet is returned when executeQuery() is called
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
    }

    @Test
    void testAddBooking_Success() throws SQLException {
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        boolean result = bookingDAO.addBooking(1, "Pickup", "Dropoff", 2);

        assertTrue(result);
        verify(mockPreparedStatement).setInt(1, 1);
        verify(mockPreparedStatement).setString(2, "Pickup");
        verify(mockPreparedStatement).setString(3, "Dropoff");
        verify(mockPreparedStatement).setInt(4, 2);
        verify(mockPreparedStatement).executeUpdate();
    }

    @Test
    void testAddBooking_Failure() throws SQLException {
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);

        boolean result = bookingDAO.addBooking(1, "Pickup", "Dropoff", 2);

        assertFalse(result);
    }

    @Test
    void testAddBooking_SQLException() throws SQLException {
        when(mockPreparedStatement.executeUpdate()).thenThrow(new SQLException("Database error"));

        boolean result = bookingDAO.addBooking(1, "Pickup", "Dropoff", 2);

        assertFalse(result);
    }
    
      @Test
    void testGetReceiptDetails_Success() throws SQLException {
        when(mockResultSet.next()).thenReturn(true); // Simulate one record found
        when(mockResultSet.getInt("BookingID")).thenReturn(1);
        when(mockResultSet.getString("PickupLocation")).thenReturn("Location A");
        when(mockResultSet.getString("DropoffLocation")).thenReturn("Location B");
        when(mockResultSet.getString("BookingDate")).thenReturn("2025-03-11");
        when(mockResultSet.getString("VehicleType")).thenReturn("Sedan");
        when(mockResultSet.getDouble("Price")).thenReturn(20.5);
        when(mockResultSet.getString("VehicleModel")).thenReturn("Toyota Camry");
        when(mockResultSet.getString("LicenseNumber")).thenReturn("ABC123");
        when(mockResultSet.getString("Name")).thenReturn("John Doe");
        when(mockResultSet.getString("Phone")).thenReturn("1234567890");

        HashMap<String, String> receipt = BookingDAO.getReceiptDetails(1, mockConnection);

        assertNotNull(receipt);
        assertEquals("1", receipt.get("BookingID"));
        assertEquals("Location A", receipt.get("PickupLocation"));
        assertEquals("Location B", receipt.get("DropoffLocation"));
        assertEquals("2025-03-11", receipt.get("Date"));
        assertEquals("Sedan", receipt.get("VehicleType"));
        assertEquals("20.5", receipt.get("Price"));
        assertEquals("Toyota Camry", receipt.get("VehicleModel"));
        assertEquals("ABC123", receipt.get("LicenseNumber"));
        assertEquals("John Doe", receipt.get("DriverName"));
        assertEquals("1234567890", receipt.get("Phone"));

        verify(mockPreparedStatement).setInt(1, 1);
        verify(mockPreparedStatement).executeQuery();
    }


   @Test
    void testGetReceiptDetails_SQLException() throws SQLException {
        when(mockPreparedStatement.executeQuery()).thenThrow(new SQLException("Database error"));

        HashMap<String, String> receipt = BookingDAO.getReceiptDetails(1, mockConnection);

        assertNotNull(receipt);
        assertTrue(receipt.isEmpty());

        verify(mockPreparedStatement).setInt(1, 1);
        verify(mockPreparedStatement).executeQuery();
    }
}
