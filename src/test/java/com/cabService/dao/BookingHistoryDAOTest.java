/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cabService.dao;

import com.cabService.dao.DBConnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.sql.*;
import java.util.List;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class BookingHistoryDAOTest {
     private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private BookingDAO bookingDAO;

    @BeforeEach
    void setUp() throws Exception {
        connection = mock(Connection.class);
        preparedStatement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);

        // Injecting the mocked connection into BookingDAO
        bookingDAO = new BookingDAO(connection); // Using the constructor that accepts the mocked connection

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
    }

    @Test
    void testGetCustomerBookings_ValidCustomer_ReturnsList() throws SQLException {
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getInt("BookingID")).thenReturn(1);
        when(resultSet.getString("Status")).thenReturn("Completed");

        List<HashMap<String, String>> bookings = bookingDAO.getCustomerBookings(1);

        assertEquals(1, bookings.size());
        assertEquals("Completed", bookings.get(0).get("Status"));
    }
}