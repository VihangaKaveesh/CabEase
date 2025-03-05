/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cabService.dao;

import jakarta.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author vihan
 */
public class ManagementDAO {
    private final Connection connection; 

    // Constructor for injecting connection (for testing purposes)
    public ManagementDAO(Connection connection) {
        this.connection = connection; 
    }
    
    public ManagementDAO() throws SQLException {
        this.connection = DBConnection.getConnection(); // Keep real connection for non-test cases
    }

    public int validateManagement(String email, String password, HttpSession session) {
        int managementId = -1;
        String query = "SELECT ManagementID FROM management WHERE Email = ? AND Password = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    managementId = rs.getInt("ManagementID");
                    session.setAttribute("role", "management");  // Store session role
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log exception properly
        }

        return managementId;
    }
}
