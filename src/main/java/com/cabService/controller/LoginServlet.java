package com.cabService.controller;

import com.cabService.dao.CustomerDAO;
import com.cabService.dao.ManagementDAO;
import com.cabService.service.LoginService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

public class LoginServlet extends HttpServlet {

    private LoginService loginService;

    // Default constructor for production use
    public LoginServlet() throws SQLException {
        this.loginService = new LoginService(new CustomerDAO(), new ManagementDAO());
    }

    // Constructor for testing (injects mock service)
    public LoginServlet(LoginService loginService) {
        this.loginService = loginService;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        HttpSession session = request.getSession(); // Get the session object

        try {
            // Check if customer login is valid
            int customerId = loginService.validateCustomerLogin(email, password, session);
            if (customerId > 0) {
                session.setAttribute("userId", customerId);
                session.setAttribute("role", "customer"); // Store role
                response.sendRedirect("pages/customerDashboard.jsp?message=Welcome Customer!");
                return;
            }

            // Check if management login is valid
            int managementId = loginService.validateManagementLogin(email, password, session);
            if (managementId > 0) {
                session.setAttribute("userId", managementId);
                session.setAttribute("role", "management"); // Store role
                response.sendRedirect("pages/managementDashboard.jsp?message=Welcome Manager!");
                return;
            }

            // If login fails
            response.sendRedirect("pages/login.jsp?message=Invalid credentials");

        } catch (Exception e) {
            e.printStackTrace(); // Log error details
            response.sendRedirect("pages/login.jsp?message=An error occurred during login. Please try again.");
        }
    }
}
