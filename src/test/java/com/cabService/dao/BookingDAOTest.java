package com.cabService.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookingDAOTest {
    private BookingDAO bookingDAO;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;

    @BeforeEach
    void setUp() throws SQLException {
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        bookingDAO = new BookingDAO(mockConnection);
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
}
