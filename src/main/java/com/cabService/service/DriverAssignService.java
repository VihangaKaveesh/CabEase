/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cabService.service;

import com.cabService.dao.DriverAssignDAO;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class DriverAssignService {
    private DriverAssignDAO driverAssignDAO;

    public DriverAssignService() throws SQLException {
        this.driverAssignDAO = new DriverAssignDAO();
    }

    // Constructor for testing (allows injecting a mock DAO)
    public DriverAssignService(DriverAssignDAO driverAssignDAO) {
        this.driverAssignDAO = driverAssignDAO;
    }

    public boolean assignDriver(int bookingID, int driverID) throws SQLException {
        return driverAssignDAO.assignDriverToBooking(bookingID, driverID);
    }

    public List<Map<String, String>> getPendingBookings() throws SQLException {
        return driverAssignDAO.getPendingBookings();
    }
}