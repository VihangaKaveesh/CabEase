<%-- 
    Document   : login
    Created on : Mar 6, 2025, 2:46:43 AM
    Author     : vihan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
      <title>Login</title>
        <style>
            body { 
                font-family: Arial, sans-serif; 
                text-align: center; 
                background-color: #f2f2f2;
            }
            .login-container { 
                width: 300px; 
                margin: 100px auto; 
                padding: 20px; 
                background: white;
                border: 1px solid #ccc; 
                border-radius: 5px; 
                box-shadow: 0px 0px 10px 0px gray;
            }
            input { 
                width: 90%; 
                padding: 8px; 
                margin: 10px 0; 
            }
            button { 
                padding: 10px 15px; 
                cursor: pointer; 
                background: blue; 
                color: white; 
                border: none;
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
               
<!-- to handle responses from the register page-->
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
      <div class="login-container">
            <h2>Login</h2>
            <form action="${pageContext.request.contextPath}/LoginServlet" method="post">
                <label for="email">Email:</label>
                <input type="email" name="email" required>

                <label for="password">Password:</label>
                <input type="password" name="password" required>

                <button type="submit">Login</button>
            </form>

            <% if(request.getParameter("error") != null) { %>
                <p style="color:red;"><%= request.getParameter("error") %></p>
            <% } %>

            <!-- Register Link -->
          <p class="register-link">Don't have an account? <a href="${pageContext.request.contextPath}/pages/register.jsp">Register here</a></p>

        </div>
    </body>
</html>
