package com.cabService.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.cabService.dao.DBConnection;
import jakarta.servlet.http.HttpSession;
import java.sql.ResultSet;

public class CustomerDAO {
    private Connection connection;

    // Constructor to inject a connection (for testing)
    public CustomerDAO(Connection connection) {
        this.connection = connection;
    }

    // Default constructor for real usage
    public CustomerDAO() throws SQLException {
        this.connection = DBConnection.getConnection(); // Keep real connection for non-test cases
    }

    // Registering the customer
    public boolean registerCustomer(String nic, String name, String email, String password, String phone) {
    boolean success = false;
    PreparedStatement preparedStatement = null;

    try {
        System.out.println("Attempting to insert into DB...");
        String sql = "INSERT INTO customers (NIC, Name, Email, Password, Phone) VALUES (?, ?, ?, ?, ?)";
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, nic);
        preparedStatement.setString(2, name);
        preparedStatement.setString(3, email);
        preparedStatement.setString(4, password);
        preparedStatement.setString(5, phone);

        int rowsInserted = preparedStatement.executeUpdate();
        if (rowsInserted > 0) {
            success = true;
            System.out.println("Customer registered successfully.");
        } else {
            System.out.println("No rows inserted. Registration failed.");
        }
    } catch (SQLException e) {
        System.out.println("Error in registerCustomer: " + e.getMessage());
        e.printStackTrace();
    } finally {
        try {
            if (preparedStatement != null) preparedStatement.close();
            if (connection != null) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    return success;
}
    
     // Validating the customer upon login
    public int validateCustomer(String email, String password, HttpSession session) {
        int customerId = -1;
        String query = "SELECT CustomerID FROM customers WHERE Email = ? AND Password = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    customerId = rs.getInt("CustomerID");
                    session.setAttribute("role", "customer");  // Store session role
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log exception properly
        }

        return customerId;
    }


}
