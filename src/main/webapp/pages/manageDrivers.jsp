<%-- 
    Document   : manageDrivers
    Created on : Feb 23, 2025, 2:05:46 PM
    Author     : vihan
--%>
<%@page import="java.util.*"%>
<%@ page import="java.sql.Connection, 
         java.sql.SQLException, 
         java.util.List, 
         com.cabService.dao.DBConnection, 
         com.cabService.dao.DriverDAO" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Manage Drivers</title>
        
        <style>
      body {
    font-family: Arial, sans-serif;
    margin: 0;
    padding: 0;
    text-align: center;
}

.navbar {
    background-color: #333;
    overflow: hidden;
    display: flex;
    justify-content: center;
    padding: 10px 0;
}

.navbar a {
    color: white;
    padding: 14px 20px;
    text-decoration: none;
    text-align: center;
}

.navbar a:hover {
    background-color: #575757;
    border-radius: 5px;
}

.container {
    width: 60%;
    margin: auto;
    padding: 20px;
}

h2, h3 {
    text-align: center;
}

form {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 10px;
    background: #f8f8f8;
    padding: 20px;
    border-radius: 10px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
    width: 100%;
    max-width: 500px;
    margin: auto;
}

form input, form select{
    width: 100%;
    padding: 8px;
    border: 1px solid #ccc;
    border-radius: 5px;
}

/*form button {
    background-color: #333;
    color: white;
    cursor: pointer;
}

form button:hover {
    background-color: #575757;
}*/

.deletebtn{
    padding: 0px;
    border: 0px;
    color: red;
    cursor: pointer;
}

table {
    width: 80%;
    margin: 20px auto;
    border-collapse: collapse;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

th, td {
    border: 2px solid black;
    padding: 10px;
    text-align: center;
}

th {
    background-color: #f2f2f2;
}

    </style>
    
    
    <script>
            window.onload = function() {
                const urlParams = new URLSearchParams(window.location.search);
                if (urlParams.has('message')) {
                    alert(urlParams.get('message'));
                }
            };
              </script>
    
    
    </head>
    <body>
     <!-- Navigation bar -->
        <div class="navbar">
            <a href="${pageContext.request.contextPath}/pages/managementDashboard.jsp">Home</a>
            <a href="${pageContext.request.contextPath}/pages/manageCustomers.jsp">Customers</a>
            <a href="${pageContext.request.contextPath}/pages/manageDrivers.jsp">Drivers</a>
             <a href="${pageContext.request.contextPath}/pages/driverAssign.jsp">Assign a driver</a>
             <a href="${pageContext.request.contextPath}/pages/manageBookings.jsp">Bookings</a>
            <a href="${pageContext.request.contextPath}/pages/login.jsp">Logout</a>
        </div>
        
        
         <h2>Driver Management</h2>

    <h3>Add a New Driver</h3>
    <form action="${pageContext.request.contextPath}/DriverServlet" method="post">
        <input type="hidden" name="action" value="add">
        NIC: <input type="text" name="nic" required><br>
        Name: <input type="text" name="name" required><br>
        Email: <input type="email" name="email" required><br>
        Phone: <input type="text" name="phone" required><br>
        License Number: <input type="text" name="licenseNumber" required><br>
        Vehicle Type: 
        <select name="vehicleType" required>
            <option value="Car">Car</option>
            <option value="Bike">Bike</option>
            <option value="Van">Van</option>
            <option value="Tuk">Tuk</option>
        </select><br>
        Vehicle Model: <input type="text" name="vehicleModel" required><br>
        <button type="submit">Add Driver</button>
    </form>

    <h3>Current Drivers</h3>
    <table>
        <tr>
            <th>Driver ID</th>
            <th>NIC</th>
            <th>Name</th>
            <th>Email</th>
            <th>Phone</th>
            <th>License Number</th>
            <th>Vehicle Type</th>
            <th>Vehicle Model</th>
            <th>Status</th>
            <th>Actions</th>
        </tr>

        <%
            Connection conn = null;
            try {
                conn = DBConnection.getConnection();
                DriverDAO driverDAO = new DriverDAO(conn);
                List<String[]> drivers = driverDAO.getDrivers();

                for (String[] driver : drivers) {
        %>
            <tr>
                <td><%= driver[0] %></td>
                <td><%= driver[1] %></td>
                <td><%= driver[2] %></td>
                <td><%= driver[3] %></td>
                <td><%= driver[4] %></td>
                <td><%= driver[5] %></td>
                <td><%= driver[6] %></td>
                <td><%= driver[7] %></td>
               <td><%= driver[8] %></td> 
<td>
    <a href="editDrivers.jsp?driverID=<%= driver[0] %>">Edit</a> |
                    <form  class="deletebtn" action="${pageContext.request.contextPath}/DriverServlet" method="post" style="display:inline;">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="driverID" value="<%= driver[0] %>">
                        <button type="submit" onclick="return confirm('Are you sure you want to delete this driver?');">Delete</button>
                    </form> 
                </td>
            </tr>
        <%
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
            }
        %>
    </table>

      
</html>