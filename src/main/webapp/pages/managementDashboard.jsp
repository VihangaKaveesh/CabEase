<%-- 
    Document   : managementDashboard
    Created on : Mar 6, 2025, 2:56:54 AM
    Author     : vihan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
    </style>
    </head>
    <body>
        
        <div class="navbar">
            <a href="${pageContext.request.contextPath}/pages/customerDashboard.jsp">Home</a>
        <a href="${pageContext.request.contextPath}/pages/manageCustomers.jsp">Customers</a>
        <a href="${pageContext.request.contextPath}/pages/manageDrivers.jsp">Drivers</a>
         <a href="${pageContext.request.contextPath}/pages/driverAssign.jsp">Assign a driver</a>
         <a href="${pageContext.request.contextPath}/pages/manageBookings.jsp">Bookings</a>
        <a href="${pageContext.request.contextPath}/pages/login.jsp">Logout</a>
       
    </div>
        
        <h1>Hello Manager!</h1>
    </body>
</html>