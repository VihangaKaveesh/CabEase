<%@ page import="java.sql.*" %>
<%@ page import="com.cabService.dao.DBConnection" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    HttpSession sessionObj = session;
    if (sessionObj == null || sessionObj.getAttribute("userId") == null || !"customer".equals(sessionObj.getAttribute("role"))) {
        response.sendRedirect("login.jsp?message=You must log in first");
        return;
    }
    
    int userId = (int) sessionObj.getAttribute("userId");

    Connection conn = DBConnection.getConnection();
    PreparedStatement stmt = conn.prepareStatement("SELECT PackageID, VehicleType,PackageName, Price FROM RidePackages");
    ResultSet rs = stmt.executeQuery();
%>

<!DOCTYPE html>
<html>
<head>
    <title>Request a Ride</title>
    
    <style>body { font-family: Arial, sans-serif; margin: 0; padding: 0; }
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
               <a href="${pageContext.request.contextPath}/pages/customerDashboard.jsp">Home</a>
        <a href="${pageContext.request.contextPath}/pages/rideRequest.jsp">Need a Ride</a>
        <a href="${pageContext.request.contextPath}/pages/bookingHistory.jsp">History</a>
        <a href="${pageContext.request.contextPath}/pages/login.jsp">Logout</a>
        
        </div>
        
        
        
    <h2>Request a Ride</h2>
    <form action="${pageContext.request.contextPath}/BookingServlet" method="POST">
        <input type="hidden" name="customerId" value="<%= userId %>">

        <label>Pickup Location:</label>
        <input type="text" name="pickupLocation" required><br>

        <label>Dropoff Location:</label>
        <input type="text" name="dropoffLocation" required><br>

        <label>Select Package:</label>
        <select name="packageId" required>
            <% while (rs.next()) { %>
                <option value="<%= rs.getInt("PackageID") %>" 
                        data-vehicle="<%= rs.getString("VehicleType") %>" 
                        data-PackageName="<%= rs.getString("PackageName") %>" 
                        data-price="<%= rs.getDouble("Price") %>">
                    <%= rs.getString("VehicleType") %> - <%= rs.getString("PackageName")%>km - <%= rs.getDouble("Price") %>LKR
                </option>
            <% } %>
        </select><br>

         <button type="submit">Add Request</button>
    </form>
</body>
</html>

<% 
    rs.close();
    stmt.close();
    conn.close();
%>
