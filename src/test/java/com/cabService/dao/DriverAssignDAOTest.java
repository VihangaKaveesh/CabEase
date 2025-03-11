package com.cabService.dao;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class DriverAssignDAOTest {
    private Connection mockConnection;
    private PreparedStatement mockStatement;
    private ResultSet mockResultSet;
    private DriverAssignDAO dao;

    @BeforeEach
    public void setUp() throws SQLException {
        mockConnection = mock(Connection.class);
        mockStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);
        dao = new DriverAssignDAO(mockConnection);
    }

    // ✅ Test for adding a new booking
//    @Test
//    public void testAddBooking() throws SQLException {
//        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
//        when(mockStatement.executeUpdate()).thenReturn(1); // Simulate successful insertion
//
//        boolean result = BookingDAO.addBooking(1, "Location A", "Location B", 1);
//
//        assertTrue(result);
//        verify(mockStatement, times(1)).executeUpdate();
//    }

    // ✅ Test for retrieving pending bookings
    @Test
    public void testGetPendingBookings() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);

        when(mockResultSet.next()).thenReturn(true, false); // Simulating one row
        when(mockResultSet.getString("BookingID")).thenReturn("1");
        when(mockResultSet.getString("CustomerID")).thenReturn("101");
        when(mockResultSet.getString("PickupLocation")).thenReturn("Location A");
        when(mockResultSet.getString("DropoffLocation")).thenReturn("Location B");
        when(mockResultSet.getString("VehicleType")).thenReturn("Car");

        List<Map<String, String>> bookings = DriverAssignDAO.getPendingBookings();

        assertFalse(bookings.isEmpty());
        assertEquals("64", bookings.get(0).get("BookingID"));
        assertEquals("1", bookings.get(0).get("CustomerID"));
        assertEquals("123 Main Street", bookings.get(0).get("PickupLocation"));
        assertEquals("456 Elm Street", bookings.get(0).get("DropoffLocation"));
        assertEquals("Car", bookings.get(0).get("VehicleType"));
    }

    // ✅ Test for assigning a driver to a booking
    @Test
    public void testAssignDriverToBooking() throws SQLException {
        PreparedStatement mockStatement1 = mock(PreparedStatement.class);
        PreparedStatement mockStatement2 = mock(PreparedStatement.class);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement1, mockStatement2);
        when(mockStatement1.executeUpdate()).thenReturn(1); // Simulate booking update success
        when(mockStatement2.executeUpdate()).thenReturn(1); // Simulate driver update success

        boolean result = dao.assignDriverToBooking(34,56 );

        assertTrue(result);
        verify(mockStatement1, times(1)).executeUpdate();
        verify(mockStatement2, times(1)).executeUpdate();
    }

    // ✅ Test for retrieving available drivers
    @Test
    public void testGetAvailableDrivers() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);

        when(mockResultSet.next()).thenReturn(true, false); // Simulating one row
        when(mockResultSet.getString("DriverID")).thenReturn("5");
        when(mockResultSet.getString("Name")).thenReturn("John Doe");

        List<Map<String, String>> drivers = DriverAssignDAO.getAvailableDrivers("Car");

        assertFalse(drivers.isEmpty());
        assertEquals("39", drivers.get(0).get("DriverID"));
        assertEquals("wGEHHetTH", drivers.get(0).get("Name"));
    }

    // ✅ Test for failed assignment (simulating SQL error)
    @Test
    public void testAssignDriverToBooking_Failure() throws SQLException {
        PreparedStatement mockStatement1 = mock(PreparedStatement.class);
        PreparedStatement mockStatement2 = mock(PreparedStatement.class);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement1, mockStatement2);
        when(mockStatement1.executeUpdate()).thenThrow(new SQLException("Database Error"));

        boolean result = dao.assignDriverToBooking(1, 10);

        assertFalse(result); // Ensure rollback occurs
    }
}
