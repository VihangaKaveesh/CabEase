package com.cabService.controller;

import com.cabService.dao.CustomerDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;

public class RegisterServlet extends HttpServlet {
    private CustomerDAO customerDAO;

    // Default constructor (for production use)
    public RegisterServlet() throws SQLException {
        this.customerDAO = new CustomerDAO(); // Uses real DB connection
    }

    // Constructor for testing (injects a mock DAO)
    public RegisterServlet(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nic = request.getParameter("nic");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String phone = request.getParameter("phone");
        
        // Log the received data
    System.out.println("Received Registration Data:");
    System.out.println("NIC: " + nic);
    System.out.println("Name: " + name);
    System.out.println("Email: " + email);
    System.out.println("Phone: " + phone);

        boolean isRegistered = customerDAO.registerCustomer(nic, name, email, password, phone);

        if (isRegistered) {
            response.sendRedirect("pages/register.jsp?message=Registration successful! You can now log in.");
        } else {
            response.sendRedirect("pages/register.jsp?message=Registration failed. Try again.");
        }
    }
}
