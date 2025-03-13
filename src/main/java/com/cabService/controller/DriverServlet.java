package com.cabService.controller;

import com.cabService.dao.DBConnection;
import com.cabService.dao.DriverDAO;
import com.cabService.service.DriverService;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.SQLException;

public class DriverServlet extends HttpServlet {
    private DriverService driverService;

    public DriverServlet() throws SQLException {
        this.driverService = new DriverService(new DriverDAO());
    }

    public DriverServlet(DriverService driverService) {
        this.driverService = driverService;
    }

    @Override
    public void init() throws ServletException {
        try {
            Connection conn = DBConnection.getConnection();
            DriverDAO driverDAO = new DriverDAO(conn);
            driverService = new DriverService(driverDAO);
        } catch (SQLException e) {
            throw new ServletException("Database connection error", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
                          throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if ("add".equals(action)) {
                addDriver(request, response);
            } else if ("update".equals(action)) {
                updateDriver(request, response);
            } else if ("delete".equals(action)) {
                deleteDriver(request, response);
            } else {
                response.sendRedirect("manageDrivers.jsp");
            }
        } catch (SQLException e) {
            throw new ServletException("Database operation failed", e);
        }
    }

    private void addDriver(HttpServletRequest request, HttpServletResponse response) 
                           throws SQLException, IOException {
        String nic = request.getParameter("nic");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String licenseNumber = request.getParameter("licenseNumber");
        String vehicleType = request.getParameter("vehicleType");
        String vehicleModel = request.getParameter("vehicleModel");

        driverService.addDriver(nic, name, email, phone, licenseNumber, vehicleType, vehicleModel);
        response.sendRedirect("pages/manageDrivers.jsp?message=Driver added successfully");
    }

    private void updateDriver(HttpServletRequest request, HttpServletResponse response) 
                              throws SQLException, IOException {
        int driverID = Integer.parseInt(request.getParameter("driverID"));
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String vehicleModel = request.getParameter("vehicleModel");
        String status = request.getParameter("status");

        driverService.updateDriver(driverID, name, email, phone, vehicleModel, status);
        response.sendRedirect("pages/manageDrivers.jsp?message=Driver edited successfully");
    }

    private void deleteDriver(HttpServletRequest request, HttpServletResponse response) 
                              throws SQLException, IOException {
        try {
            int driverID = Integer.parseInt(request.getParameter("driverID"));
            driverService.deleteDriver(driverID);
            response.sendRedirect("pages/manageDrivers.jsp?message=Driver deleted successfully");
        } catch (NumberFormatException e) {
            response.sendRedirect("pages/manageDrivers.jsp?message=Invalid driver ID");
        }
    }
}
