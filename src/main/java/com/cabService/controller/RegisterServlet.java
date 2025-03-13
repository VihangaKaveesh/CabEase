package com.cabService.controller;

import com.cabService.dao.CustomerDAO;
import com.cabService.service.RegistrationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;


public class RegisterServlet extends HttpServlet {

    private RegistrationService registrationService;

    // Default constructor (for production use)
    public RegisterServlet() throws SQLException {
        this.registrationService = new RegistrationService(new CustomerDAO()); // Uses real DB connection
    }

    // Constructor for testing (injects a mock service)
    public RegisterServlet(RegistrationService registrationService) {
        this.registrationService = registrationService;
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

        try {
            // Delegate the registration logic to the service
            boolean isRegistered = registrationService.registerCustomer(nic, name, email, password, phone);

            if (isRegistered) {
                response.sendRedirect("pages/register.jsp?message=Registration successful! You can now log in.");
            } else {
                response.sendRedirect("pages/register.jsp?message=Registration failed. Try again.");
            }
        } catch (SQLException e) {
            // Handle the exception (e.g., database connection issues)
            e.printStackTrace();
            response.sendRedirect("pages/register.jsp?message=An error occurred. Please try again later.");
        }
    }
}
