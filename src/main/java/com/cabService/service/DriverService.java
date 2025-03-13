/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cabService.service;

import com.cabService.dao.DriverDAO;
import java.sql.SQLException;
import java.util.List;

public class DriverService {
    private final DriverDAO driverDAO;

    public DriverService(DriverDAO driverDAO) {
        this.driverDAO = driverDAO;
    }

    public void addDriver(String nic, String name, String email, String phone, 
                          String licenseNumber, String vehicleType, String vehicleModel) 
                          throws SQLException {
        driverDAO.addDriver(nic, name, email, phone, licenseNumber, vehicleType, vehicleModel);
    }

    public void updateDriver(int driverID, String name, String email, String phone, 
                             String vehicleModel, String status) throws SQLException {
        driverDAO.updateDriver(driverID, name, email, phone, vehicleModel, status);
    }

    public void deleteDriver(int driverID) throws SQLException {
        driverDAO.deleteDriver(driverID);
    }
}