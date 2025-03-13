<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page session="true" %>

<%
    String activePage = request.getParameter("activePage");
    String role = (String) session.getAttribute("role");
%>

<style>
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
</style>

<div class="navbar">
        <a href="${pageContext.request.contextPath}/pages/managementDashboard.jsp"
           class="<%= "home".equals(activePage) ? "active" : "" %>">Home</a>
        <a href="${pageContext.request.contextPath}/pages/manageCustomers.jsp"
           class="<%= "customers".equals(activePage) ? "active" : "" %>">Customers</a>
        <a href="${pageContext.request.contextPath}/pages/manageDrivers.jsp"
           class="<%= "drivers".equals(activePage) ? "active" : "" %>">Drivers</a>
        <a href="${pageContext.request.contextPath}/pages/driverAssign.jsp"
           class="<%= "assign".equals(activePage) ? "active" : "" %>">Assign a Driver</a>
        <a href="${pageContext.request.contextPath}/pages/manageBookings.jsp"
           class="<%= "bookings".equals(activePage) ? "active" : "" %>">Bookings</a>
    <a href="${pageContext.request.contextPath}/LogoutServlet">Logout</a>
</div>
