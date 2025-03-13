package com.cabService.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.cabService.service.CustomerService;

//@WebServlet("/CustomerServlet")
public class CustomerServlet extends HttpServlet {
    
    private CustomerService customerService;

    // Dependency Injection for testing purposes
    public CustomerServlet(CustomerService customerService) {
        this.customerService = customerService;
    }

    // Default constructor for normal servlet initialization
    public CustomerServlet() {
        super();
    }

    // Initialize Service
    @Override
    public void init() throws ServletException {
        if (customerService == null) {
            try {
                customerService = new CustomerService();
            } catch (SQLException ex) {
                Logger.getLogger(CustomerServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    // CRUD functions of the customer
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if ("delete".equals(action)) {
                deleteCustomer(request, response);
            } else {
                response.sendRedirect("pages/manageCustomers.jsp");
            }
        } catch (SQLException e) {
            throw new ServletException("Database operation failed", e);
        }
    }

    // Delete customer method using Facade
    private void deleteCustomer(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        int customerId = Integer.parseInt(request.getParameter("customerID"));
        boolean success = customerService.deleteCustomer(customerId);
        response.sendRedirect("pages/manageCustomers.jsp?message=" + (success ? "Customer deleted successfully" : "Failed to delete customer"));
    }
}
