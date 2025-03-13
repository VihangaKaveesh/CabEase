<%-- 
    Document   : customerDashboard
    Created on : Mar 6, 2025, 2:56:37 AM
    Author     : vihan
--%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    //HttpSession session = request.getSession(false); // Get the current session, do not create a new one

    // Check if session exists and if user is logged in
    if (session == null || session.getAttribute("userId") == null || !"customer".equals(session.getAttribute("role"))) {
        response.sendRedirect("login.jsp?message=You must log in first");
        return; // Stop the execution of the page
    }
    
    //int customerId = (int) session.getAttribute("userId"); // Get the logged-in user's customer ID
        
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Home</title>
        
        <script>
            window.onload = function() {
                const urlParams = new URLSearchParams(window.location.search);
                if (urlParams.has('message')) {
                    alert(urlParams.get('message'));
                }
            };
              </script>
              
              <style>
       body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f4f4f4;
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
            margin: 0 10px;
            border-radius: 5px;
        }
        .navbar a:hover, .navbar a.active {
            background-color: #575757;
        }
        .dashboard-container {
            max-width: 1200px;
            margin: 20px auto;
            padding: 20px;
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
        .dashboard-header {
            text-align: center;
            margin-bottom: 20px;
        }
        .dashboard-header h1 {
            color: #333;
        }
        .dashboard-content {
            display: flex;
            flex-wrap: wrap;
            gap: 20px;
        }
        .dashboard-card {
            background-color: #f9f9f9;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 0 5px rgba(0, 0, 0, 0.1);
            flex: 1 1 calc(33.333% - 40px);
            text-align: center;
        }
        .dashboard-card h2 {
            color: #555;
        }
        .dashboard-card p {
            color: #777;
        }
    </style>
    </head>
    <body>
           <%-- Include the navigation bar component --%>
    <%@ include file="components/NavBarComponenet.jsp" %>

   <!-- Dashboard Content -->
    <div class="dashboard-container">
        <div class="dashboard-header">
            <h1>Welcome to Your Dashboard</h1>
        </div>
        <div class="dashboard-content">
            <div class="dashboard-card">
                <h2>Book a Ride</h2>
                <p>Request a ride to your desired destination quickly and easily.</p>
                <a href="${pageContext.request.contextPath}/pages/rideRequest.jsp" class="btn">Book Now</a>
            </div>
            <div class="dashboard-card">
                <h2>Booking History</h2>
                <p>View your past rides and manage your bookings.</p>
                <a href="${pageContext.request.contextPath}/pages/bookingHistory.jsp" class="btn">View History</a>
            </div>
   
        </div>
    </div>
    </body>
</html>