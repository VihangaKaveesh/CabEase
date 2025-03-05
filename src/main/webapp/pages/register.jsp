<%-- 
    Document   : register
    Created on : Mar 4, 2025, 3:37:05 PM
    Author     : vihan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Customer Registration</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f2f2f2;
            text-align: center;
        }
        form {
            background: white;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0px 0px 10px 0px gray;
            width: 300px;
            margin: auto;
            margin-top: 50px;
        }
        input {
            display: block;
            width: 90%;
            padding: 10px;
            margin: 10px 0;
        }
        button {
            background: blue;
            color: white;
            padding: 10px;
            border: none;
            cursor: pointer;
        }
        button:hover {
            background: darkblue;
        }
        .register-link {
                margin-top: 15px;
                display: block;
                font-size: 14px;
        }
        .register-link a {
                text-decoration: none;
                color: blue;
                font-weight: bold;
        }
        .register-link a:hover {
                text-decoration: underline;
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

    <form action="${pageContext.request.contextPath}/RegisterServlet" method="post">
        <h2>Register</h2>
        <input type="text" name="nic" placeholder="NIC" autocomplete="off" required>
        <input type="text" name="name" placeholder="Full Name" required autocomplete="name">
        <input type="email" name="email" placeholder="Email" required autocomplete="email">
        <input type="password" name="password" placeholder="Password" required autocomplete="new-password">
        <input type="text" name="phone" placeholder="Phone Number" required autocomplete="tel">
        <button type="submit">Register</button>
        
<!--         <p class="register-link">Already have an account? <a href="${pageContext.request.contextPath}/pages/login.jsp">Login here</a></p>-->

    </form>

    <%-- Show error message if registration fails --%>
    <%
        String error = request.getParameter("error");
        if (error != null) {
    %>
        <p style="color: red;"><%= error %></p>
    <%
        }
    %>
</html>