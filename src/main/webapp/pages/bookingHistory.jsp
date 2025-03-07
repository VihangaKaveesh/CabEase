<%@ page import="java.util.List, java.util.HashMap, com.cabService.dao.BookingDAO" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page session="true" %>
<%
    // Check if the user is logged in as a customer
    if (session.getAttribute("userId") == null || !"customer".equals(session.getAttribute("role"))) {
        response.sendRedirect("login.jsp?message=You must log in first");
        return;
    }

    Integer customerID = (Integer) session.getAttribute("userId");

    // Create an instance of BookingDAO to call the method
    BookingDAO bookingDAO = new BookingDAO();
    List<HashMap<String, String>> bookings = null;
    
    try {
        bookings = bookingDAO.getCustomerBookings(customerID); // Fetch customer bookings
    } catch (Exception e) {
        e.printStackTrace();
    }
%>

<!DOCTYPE html>
<html>
<head>
    <title>Booking History</title>
    <style>
        table {
            width: 100%;
            border-collapse: collapse;
        }
        th, td {
            border: 1px solid black;
            padding: 8px;
            text-align: center;
        }
        th {
            background-color: #f2f2f2;
        }
        .btn {
            padding: 5px 10px;
            border: none;
            cursor: pointer;
            color: white;
            background-color: blue;
            text-decoration: none;
            display: inline-block;
            border-radius: 5px;
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
        <a href="${pageContext.request.contextPath}/pages/login.jsp">Logout</a>
    </div>

    <h2>Your Booking History</h2>

    <table>
        <tr>
            <th>Pickup Location</th>
            <th>Dropoff Location</th>
            <th>Date</th>
            <th>Vehicle Type</th>
            <th>Price</th>
            <th>Status</th>
            <th>Receipt</th>
        </tr>

        <%
            if (bookings != null && !bookings.isEmpty()) {
                for (HashMap<String, String> booking : bookings) { 
        %>
            <tr>
                <td><%= booking.get("PickupLocation") %></td>
                <td><%= booking.get("DropoffLocation") %></td>
                <td><%= booking.get("Date") %></td>
                <td><%= booking.get("VehicleType") %></td>
                <td>$<%= booking.get("Price") %></td>
                <td><%= booking.get("Status") %></td>
                <td>
                    <% if ("Assigned".equals(booking.get("Status")) || "Completed".equals(booking.get("Status"))) { %>
                        <form action="receipt.jsp" method="get">
                            <input type="hidden" name="bookingID" value="<%= booking.get("BookingID") %>">
                            <button type="submit" class="btn">View Receipt</button>
                        </form>
                    <% } else { %>
                        <span>-</span>
                    <% } %>
                </td>
            </tr>
        <%
                }
            } else { 
        %>
            <tr>
                <td colspan="7">No booking history found.</td>
            </tr>
        <%
            } 
        %>
    </table>

</body>
</html>
