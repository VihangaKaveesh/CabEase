/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cabService.service;

import com.cabService.dao.BookingDAO;
import java.sql.SQLException;
import java.util.List;
import java.util.HashMap;

public class BookingServiceFacade {
    private final BookingDAO bookingDAO;

    public BookingServiceFacade(BookingDAO bookingDAO) {
        this.bookingDAO = bookingDAO;
    }

    public List<HashMap<String, String>> getCustomerBookings(int customerId) throws SQLException {
        return bookingDAO.getCustomerBookings(customerId);
    }
}