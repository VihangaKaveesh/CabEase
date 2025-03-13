<%-- 
    Document   : managementDashboard
    Created on : Mar 6, 2025, 2:56:54 AM
    Author     : vihan
--%>

<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.Connection"%>
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
        
          <%-- Include the navigation bar component --%>
    <%@ include file="components/ManagementNavBar.jsp" %>
        
        <h1>Hello Manager!</h1>
    </body>
</html>