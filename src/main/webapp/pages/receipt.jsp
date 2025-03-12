<%@page import="com.cabService.dao.DBConnection"%>
<%@page import="java.sql.Connection"%>
<%@ page import="java.util.HashMap, com.cabService.dao.BookingDAO" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    // Get the current session
   // HttpSession session = request.getSession(false);

    // Check if session exists and if user is logged in
    if (session == null || session.getAttribute("userId") == null || !"customer".equals(String.valueOf(session.getAttribute("role")))) {
        response.sendRedirect("login.jsp?message=You must log in first");
        return;
    }

    // Get booking ID from request parameter with error handling
    int bookingID = 0;
    HashMap<String, String> receipt = new HashMap<>();

    try {
        String bookingIDParam = request.getParameter("bookingID");
        if (bookingIDParam != null && !bookingIDParam.trim().isEmpty()) {
            bookingID = Integer.parseInt(bookingIDParam);

            // Establish database connection
            Connection conn = DBConnection.getConnection();
            receipt = BookingDAO.getReceiptDetails(bookingID, conn);
            conn.close(); // Close connection after use
        }
    } catch (NumberFormatException e) {
        out.println("<p style='text-align: center; color: red;'>Invalid Booking ID format.</p>");
    } catch (Exception e) {
        out.println("<p style='text-align: center; color: red;'>An error occurred while retrieving the receipt.</p>");
        e.printStackTrace();
    }
%>

<!DOCTYPE html>
<html>
<head>
    <title>Booking Receipt</title>
    <style>
        table {
            width: 60%;
            margin: auto;
            border-collapse: collapse;
        }
        th, td {
            border: 1px solid black;
            padding: 10px;
            text-align: left;
        }
        th {
            background-color: #f2f2f2;
        }
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
            border-radius: 5px;
        }
    </style>
</head>
<body>
    
    <div class="navbar">
        <a href="${pageContext.request.contextPath}/pages/customerDashboard.jsp">Home</a>
        <a href="${pageContext.request.contextPath}/pages/rideRequest.jsp">Need a Ride</a>
        <a href="${pageContext.request.contextPath}/pages/bookingHistory.jsp">History</a>
         <a href="${pageContext.request.contextPath}/LogoutServelt">Logout</a>
    </div>

    <h2 style="text-align: center;">Booking Receipt</h2>

    <%
        if (receipt.isEmpty()) {
            out.println("<p style='text-align: center; color: red;'>Invalid Booking ID or Booking is not Assigned/Completed.</p>");
        } else {
    %>
        <table>
            <tr><th>Pickup Location</th><td><%= receipt.getOrDefault("PickupLocation", "N/A") %></td></tr>
            <tr><th>Dropoff Location</th><td><%= receipt.getOrDefault("DropoffLocation", "N/A") %></td></tr>
            <tr><th>Date</th><td><%= receipt.getOrDefault("Date", "N/A") %></td></tr>
            <tr><th>Vehicle Type</th><td><%= receipt.getOrDefault("VehicleType", "N/A") %></td></tr>
            <tr><th>Price(LKR)</th><td><%= receipt.getOrDefault("Price", "0.00") %></td></tr>
            <tr><th>Vehicle Model</th><td><%= receipt.getOrDefault("VehicleModel", "N/A") %></td></tr>
            <tr><th>License Number</th><td><%= receipt.getOrDefault("LicenseNumber", "N/A") %></td></tr>
            <tr><th>Driver Name</th><td><%= receipt.getOrDefault("DriverName", "N/A") %></td></tr>
            <tr><th>Driver Phone</th><td><%= receipt.getOrDefault("Phone", "N/A") %></td></tr>
        </table>
    <% } %>

</body>
</html>
