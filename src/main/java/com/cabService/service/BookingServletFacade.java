/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cabService.service;

import com.cabService.dao.BookingDAO;
import java.sql.SQLException;

//booking servlet
public class BookingServletFacade {
    private BookingDAO bookingDAO;

    public BookingServletFacade() throws SQLException {
        this.bookingDAO = new BookingDAO();
    }

    public BookingServletFacade(BookingDAO bookingDAO) {
        this.bookingDAO = bookingDAO;
    }

    public boolean handleBookingRequest(int customerId, String pickupLocation, String dropoffLocation, int packageId) {
        return bookingDAO.addBooking(customerId, pickupLocation, dropoffLocation, packageId);
    }
}