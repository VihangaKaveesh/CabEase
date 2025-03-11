package com.cabService.dao;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class) // Use JUnit 5 Mockito extension
public class ManageBookingTest {
    
    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @Mock
    private ResultSet mockResultSet;

    @InjectMocks
    private BookingDAO bookingDAO; // Ensure BookingDAO uses our mock connection

    @BeforeEach
    void setUp() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
    }

   @Test
void testUpdateAssignedBookingToCompleted() throws Exception {
    int bookingID = 57;  // Example booking ID
    String newStatus = "Completed";

    // Mock the first query to fetch DriverID
    when(mockConnection.prepareStatement("SELECT DriverID FROM Bookings WHERE BookingID = ?"))
            .thenReturn(mockPreparedStatement);
    when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
    when(mockResultSet.next()).thenReturn(true);
    when(mockResultSet.getInt("DriverID")).thenReturn(38);

    boolean result = bookingDAO.updateBookingStatus(bookingID, newStatus);

    // Assertions
    assertTrue(result, "The update should be successful");

    // Verify that executeUpdate() is called **twice** (once for Bookings, once for Drivers)
    verify(mockPreparedStatement, times(2)).executeUpdate();

    // Ensure transaction is committed
    verify(mockConnection, times(1)).commit();
}


    @Test
    void testUpdateBookingStatusRollbackOnFailure() throws Exception {
        int bookingID = 60;  // Example booking ID
        String newStatus = "Completed";

        // Simulate failure in `executeUpdate()`
        when(mockPreparedStatement.executeUpdate()).thenThrow(new SQLException("Database error"));

        boolean result = bookingDAO.updateBookingStatus(bookingID, newStatus);

        // Assertions
        assertFalse(result, "The update should fail due to exception");

        // Ensure rollback is called on failure
        verify(mockConnection, times(1)).rollback();
    }
}
