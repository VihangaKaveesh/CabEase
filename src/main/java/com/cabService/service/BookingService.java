/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cabService.service;

import com.cabService.dao.BookingDAO;
import java.sql.SQLException;

public class BookingService {
    
    private BookingDAO bookingDAO;

    public BookingService(BookingDAO bookingDAO) {
        this.bookingDAO = bookingDAO;
    }

    public boolean updateBookingStatus(int bookingID, String action) throws SQLException {
        String status = "";
        
        if ("Complete".equals(action)) {
            status = "Completed";
        } else if ("Reject".equals(action)) {
            status = "Rejected";
        } else {
            return false; // Invalid action
        }

        return bookingDAO.updateBookingStatus(bookingID, status);
    }
}