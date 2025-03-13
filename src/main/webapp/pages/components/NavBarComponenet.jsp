<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page session="true" %>

<%
    String activePage = request.getParameter("activePage");
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
    <a href="${pageContext.request.contextPath}/pages/customerDashboard.jsp"
       class="<%= "home".equals(activePage) ? "active" : "" %>">Home</a>
    <a href="${pageContext.request.contextPath}/pages/rideRequest.jsp"
       class="<%= "ride".equals(activePage) ? "active" : "" %>">Need a Ride</a>
    <a href="${pageContext.request.contextPath}/pages/bookingHistory.jsp"
       class="<%= "history".equals(activePage) ? "active" : "" %>">History</a>
    <a href="${pageContext.request.contextPath}/LogoutServlet">Logout</a>
</div>
