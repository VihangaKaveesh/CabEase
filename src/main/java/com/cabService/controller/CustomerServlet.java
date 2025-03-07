package com.cabService.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import com.cabService.dao.CustomerDAO;
import java.util.logging.Level;
import java.util.logging.Logger;

//@WebServlet("/CustomerServlet")
public class CustomerServlet extends HttpServlet {
    
    private CustomerDAO customerDAO;

    // Dependency Injection for testing purposes
    public CustomerServlet(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    // Default constructor for normal servlet initialization
    public CustomerServlet() {
        super();
    }

    // Initialize DAO
    @Override
    public void init() throws ServletException {
        if (customerDAO == null) {
            try {
                customerDAO = new CustomerDAO();
            } catch (SQLException ex) {
                Logger.getLogger(CustomerServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    // CRUD functions of the customer
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if ("add".equals(action)) {
//                addCustomer(request, response);
            } else if ("delete".equals(action)) {
                deleteCustomer(request, response);
            } else {
                response.sendRedirect("pages/manageCustomers.jsp");
            }
        } catch (SQLException e) {
            throw new ServletException("Database operation failed", e);
        }
    }


    // Delete customer part
    private void deleteCustomer(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        int customerId = Integer.parseInt(request.getParameter("customerID"));
        boolean success = customerDAO.deleteCustomer(customerId);
        response.sendRedirect("pages/manageCustomers.jsp?message=" + (success ? "Customer deleted successfully" : "Failed to delete customer"));
    }
}
