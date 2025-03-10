<%-- 
    Document   : editDriver
    Created on : Feb 25, 2025, 8:28:26 AM
    Author     : vihan
--%>

<%@page import="java.sql.Connection"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.sql.Connection, 
         java.sql.SQLException, 
         java.util.List, 
         com.cabService.dao.DBConnection, 
         com.cabService.dao.DriverDAO" %>

<!DOCTYPE html>
<html>
    <head>

        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Edit Driver</title>
        
         <style>
        body { font-family: Arial, sans-serif; margin: 0; padding: 0; }
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
             border-radius: 5px
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
    </style>
    
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
        
        <h2>Edit Driver</h2>

    <%
        String driverID = request.getParameter("driverID");
        Connection conn = null;
        String[] driver = null;

        if (driverID != null) {
            try {
                conn = DBConnection.getConnection();
                DriverDAO driverDAO = new DriverDAO(conn);
                driver = driverDAO.getDriverByID(Integer.parseInt(driverID));
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
            }
        }
        
        if (driver != null) {
    %>

    <form action="${pageContext.request.contextPath}/DriverServlet" method="post">
        <input type="hidden" name="action" value="update">
        <input type="hidden" name="driverID" value="<%= driver[0] %>">
        Name: <input type="text" name="name" value="<%= driver[2] %>" required><br>
        Email: <input type="email" name="email" value="<%= driver[3] %>" required><br>
        Phone: <input type="text" name="phone" value="<%= driver[4] %>" required><br>
        Vehicle Model: <input type="text" name="vehicleModel" value="<%= driver[7] %>" required><br>
        Status:
        <select name="status">
            <option value="Available" <%= driver[8].equals("Available") ? "selected" : "" %>>Available</option>
            <option value="Assigned" <%= driver[8].equals("Assigned") ? "selected" : "" %>>Assigned</option>
        </select><br>
        <button type="submit">Update Driver</button>
    </form>

    <%
        } else {
            out.println("<p>Driver not found.</p>");
        }
    %>

       
    </body>
</html>