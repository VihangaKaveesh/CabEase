<%@ page import="java.util.List, java.util.Map, com.cabService.dao.DriverAssignDAO" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%
    // Session validation
   // HttpSession session = request.getSession(false);
    if (session == null || session.getAttribute("userId") == null || !"management".equals(session.getAttribute("role"))) {
        response.sendRedirect("login.jsp?message=You must log in first");
        return;
    }

    // Fetch pending bookings using DAO
    List<Map<String, String>> pendingBookings = DriverAssignDAO.getPendingBookings();
%>

<!DOCTYPE html>
<html>
<head>
    <title>Assign Drivers</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 0; padding: 0; text-align: center; }
        .navbar { background-color: #333; overflow: hidden; display: flex; justify-content: center; padding: 10px 0; }
        .navbar a { color: white; padding: 14px 20px; text-decoration: none; text-align: center; }
        .navbar a:hover { background-color: #575757; border-radius: 5px; }
        .container { width: 80%; margin: auto; padding: 20px; }
        h2, h3 { text-align: center; }
        table { width: 100%; border-collapse: collapse; margin-top: 20px; }
        table, th, td { border: 1px solid black; padding: 10px; }
        form { display: flex; flex-direction: column; align-items: center; gap: 10px; 
                width: 100%;
               max-width: 500px; margin: auto; }
        form input { width: 100%; padding: 8px;   }

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

   <%-- Include the navigation bar component --%>
    <%@ include file="components/ManagementNavBar.jsp" %>

    <h2>Assign Drivers to Pending Bookings</h2>

    <table>
        <tr>
            <th>Booking ID</th>
            <th>Customer ID</th>
            <th>Pickup Location</th>
            <th>Dropoff Location</th>
            <th>Vehicle Type</th>
            <th>Available Drivers</th>
            <th>Action</th>
        </tr>
        <%
            if (pendingBookings.isEmpty()) {
        %>
        <tr>
            <td colspan="7">No pending bookings.</td>
        </tr>
        <%
            } else {
                for (Map<String, String> booking : pendingBookings) {
        %>
        <tr>
            <td><%= booking.get("BookingID") %></td>
            <td><%= booking.get("CustomerID") %></td>
            <td><%= booking.get("PickupLocation") %></td>
            <td><%= booking.get("DropoffLocation") %></td>
            <td><%= booking.get("VehicleType") %></td>
            <td>
                <form action="${pageContext.request.contextPath}/DriverAssignServlet" method="POST">
                    <input type="hidden" name="bookingID" value="<%= booking.get("BookingID") %>">
                    <select name="driverID" required>
                        <option value="">Select Driver</option>
                        <%
                            List<Map<String, String>> availableDrivers = DriverAssignDAO.getAvailableDrivers(booking.get("VehicleType"));
                            for (Map<String, String> driver : availableDrivers) {
                        %>
                        <option value="<%= driver.get("DriverID") %>">
                            <%= driver.get("Name") %>
                        </option>
                        <%
                            }
                        %>
                    </select>
                
                    
            </td>
            <td> <button type="submit">Assign</button></td>
            </form>
        </tr>
        <%
                }
            }
        %>
    </table>
</body>
</html>
