/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cabService.service;

import com.cabService.dao.CustomerDAO;
import com.cabService.dao.ManagementDAO;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;

public class LoginService {
    
    private final CustomerDAO customerDAO;
    private final ManagementDAO managementDAO;

    public LoginService(CustomerDAO customerDAO, ManagementDAO managementDAO) {
        this.customerDAO = customerDAO;
        this.managementDAO = managementDAO;
    }

    public int validateCustomerLogin(String email, String password, HttpSession session) throws SQLException {
        return customerDAO.validateCustomer(email, password, session);
    }

    public int validateManagementLogin(String email, String password, HttpSession session) throws SQLException {
        return managementDAO.validateManagement(email, password, session);
    }
}