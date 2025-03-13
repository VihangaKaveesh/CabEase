<%@ page import="java.sql.*, com.cabService.dao.DBConnection" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>


    <%
    //HttpSession session = request.getSession(false); // Get the current session, do not create a new one

    // Check if session exists and if user is logged in
    if (session == null || session.getAttribute("userId") == null || !"management".equals(session.getAttribute("role"))) {
        response.sendRedirect("login.jsp?message=You must log in first");
        return; // Stop the execution of the page
    }
    
    //int customerId = (int) session.getAttribute("userId"); // Get the logged-in user's customer ID
        
%>
<%
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
%>
<!DOCTYPE html>
<html>
<head>
    <title>Manage Bookings</title>
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
            width: 90%;
            margin: auto;
            padding: 20px;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }

        table, th, td {
            border: 1px solid black;
        }

        th, td {
            padding: 10px;
            text-align: center;
        }

        .status-pending { color: orange; }
        .status-assigned { color: blue; }
        .status-completed { color: green; }
        .status-rejected { color: red; }

        button {
            padding: 5px 10px;
            margin: 5px;
            border: none;
            cursor: pointer;
        }

        .btn-complete { background-color: green; color: white; }
        .btn-reject { background-color: red; color: white; }
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

    <h2>Manage Bookings</h2>

    <div class="container">
        <table>
            <tr>
                <th>Booking ID</th>
                <th>Customer ID</th>
                <th>Pickup Location</th>
                <th>Dropoff Location</th>
                <th>Driver ID</th>
                <th>Package Name</th>
                <th>Vehicle Type</th>
                <th>Price (LKR)</th>
                <th>Status</th>
                <th>Actions</th>
            </tr>

            <%
                try {
                    conn = DBConnection.getConnection();
                    String sql = "SELECT b.BookingID, b.CustomerID, b.PickupLocation, b.DropoffLocation, b.DriverID, " +
                                 "p.PackageName, p.VehicleType, p.Price, b.Status " +
                                 "FROM bookings b " +
                                 "JOIN ridepackages p ON b.PackageID = p.PackageID";
                    
                    ps = conn.prepareStatement(sql);
                    rs = ps.executeQuery();

                    while (rs.next()) {
                        int bookingID = rs.getInt("BookingID");
                        int customerID = rs.getInt("CustomerID");
                        String pickupLocation = rs.getString("PickupLocation");
                        String dropoffLocation = rs.getString("DropoffLocation");
                        int driverID = rs.getInt("DriverID");
                        String packageName = rs.getString("PackageName");
                        String vehicleType = rs.getString("VehicleType");
                        double price = rs.getDouble("Price");
                        String status = rs.getString("Status");
            %>
            <tr>
                <td><%= bookingID %></td>
                <td><%= customerID %></td>
                <td><%= pickupLocation %></td>
                <td><%= dropoffLocation %></td>
                <td><%= (driverID != 0 ? driverID : "Not Assigned") %></td>
                <td><%= packageName %></td>
                <td><%= vehicleType %></td>
                <td><%= price %></td>
                <td class="status-<%= status.toLowerCase() %>"><%= status %></td>
               <td>
    <% if (!"Completed".equals(status) && !"Rejected".equals(status)) { %>
        <form action="${pageContext.request.contextPath}/ManageBookingServlet" method="POST">
            <input type="hidden" name="bookingID" value="<%= bookingID %>">
            <button type="submit" name="action" value="Complete" class="btn-complete">Mark Complete</button>
            <button type="submit" name="action" value="Reject" class="btn-reject">Reject</button>
        </form>
    <% } %>
</td>

            </tr>
            <%
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (rs != null) rs.close();
                    if (ps != null) ps.close();
                    if (conn != null) conn.close();
                }
            %>
        </table>
    </div>

</body>
</html>

