/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cabService.service;

import com.cabService.dao.CustomerDAO;
import java.sql.SQLException;

public class RegistrationService {

    private CustomerDAO customerDAO;

    public RegistrationService(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    public boolean registerCustomer(String nic, String name, String email, String password, String phone) throws SQLException {
        // Perform any additional logic (like validation or encryption) if needed
        return customerDAO.registerCustomer(nic, name, email, password, phone);
    }
}