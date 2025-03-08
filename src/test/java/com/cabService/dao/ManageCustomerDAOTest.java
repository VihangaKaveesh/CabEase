package com.cabService.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ManageCustomerDAOTest {

    private CustomerDAO customerDAO;
    private Connection connection;

    @BeforeEach
    void setUp() throws SQLException {
        // Initialize connection to the test database
        connection = DBConnection.getConnection();  // Ensure this points to your test database
        customerDAO = new CustomerDAO(connection);
    }

    @Test
    void testGetAllCustomers_Success() throws SQLException {
        // Ensure there's test data or insert one before testing
        insertTestCustomer("123", "Test User", "testuser@example.com", "password", "1234567890");

        // Now get the customers
        List<String[]> customers = customerDAO.getAllCustomers();
        
        // Verify that we got the expected results and there is at least one customer
        assertNotNull(customers);
        assertTrue(customers.size() > 0, "No customers found in the database.");
    }

    @Test
    void testDeleteCustomer_Success() throws SQLException {
        // Insert a customer to delete
        int customerId = insertTestCustomer("456", "Delete Me", "delete@example.com", "password", "0987654321");

        // Now delete the customer
        boolean result = customerDAO.deleteCustomer(customerId);

        // Verify that the deletion was successful
        assertTrue(result);

        // Verify that the customer was indeed deleted from the database
        String query = "SELECT * FROM customers WHERE CustomerID = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setInt(1, customerId);
        ResultSet rs = stmt.executeQuery();
        
        assertFalse(rs.next(), "Customer was not deleted.");
    }

    @Test
    void testDeleteCustomer_Failure() throws SQLException {
        // Try deleting a non-existing customer (CustomerID = 999)
        boolean result = customerDAO.deleteCustomer(999);

        // Verify failure since the customer doesn't exist
        assertFalse(result);
    }

    @AfterEach
    void tearDown() throws SQLException {
        // Clean up: Optionally, delete the test data after tests are run
        // For example, delete the test customer with CustomerID = 123
        String deleteQuery = "DELETE FROM customers WHERE NIC = '123'";
        PreparedStatement stmt = connection.prepareStatement(deleteQuery);
        stmt.executeUpdate();
    }

    // Helper method to insert a test customer and return their CustomerID
    private int insertTestCustomer(String nic, String name, String email, String password, String phone) throws SQLException {
        String insertQuery = "INSERT INTO customers (NIC, Name, Email, Password, Phone) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS);
        stmt.setString(1, nic);
        stmt.setString(2, name);
        stmt.setString(3, email);
        stmt.setString(4, password);
        stmt.setString(5, phone);

        int rowsInserted = stmt.executeUpdate();

        if (rowsInserted > 0) {
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);  // Return the generated CustomerID
            }
        }
        return -1;  // Return -1 if insertion fails
    }
}
