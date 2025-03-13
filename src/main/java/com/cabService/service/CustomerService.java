/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cabService.service;

import com.cabService.dao.CustomerDAO;
import java.sql.SQLException;

public class CustomerService {
    private CustomerDAO customerDAO;

    public CustomerService() throws SQLException {
        this.customerDAO = new CustomerDAO();
    }

    // Constructor for dependency injection (useful for testing)
    public CustomerService(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    public boolean deleteCustomer(int customerId) throws SQLException {
        return customerDAO.deleteCustomer(customerId);
    }
}