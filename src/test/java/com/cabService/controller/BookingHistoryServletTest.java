package com.cabService.controller;

import com.cabService.service.BookingServiceFacade;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookingHistoryServletTest {

    private BookingHistoryServlet servlet;
    private BookingServiceFacade bookingServiceFacade;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private RequestDispatcher dispatcher;

    @BeforeEach
    void setUp() {
        bookingServiceFacade = mock(BookingServiceFacade.class);
        servlet = new BookingHistoryServlet(bookingServiceFacade);

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        dispatcher = mock(RequestDispatcher.class);

        when(request.getSession(false)).thenReturn(session);
    }

    @Test
    void testDoGet_NoSession_RedirectsToLogin() throws IOException, ServletException {
        when(request.getSession(false)).thenReturn(null);

        servlet.doGet(request, response);

        verify(response).sendRedirect("login.jsp?message=You must log in first");
    }

    @Test
    void testDoGet_NoUserId_RedirectsToLogin() throws IOException, ServletException {
        when(session.getAttribute("userId")).thenReturn(null);

        servlet.doGet(request, response);

        verify(response).sendRedirect("login.jsp?message=You must log in first");
    }

    @Test
    void testDoGet_NonCustomer_RedirectsToLogin() throws IOException, ServletException {
        when(session.getAttribute("userId")).thenReturn(1);
        when(session.getAttribute("role")).thenReturn("driver");

        servlet.doGet(request, response);

        verify(response).sendRedirect("login.jsp?message=You must log in first");
    }

    @Test
    void testDoGet_ValidCustomer_ForwardsToJSP() throws IOException, ServletException, SQLException {
        int customerId = 1;
        when(session.getAttribute("userId")).thenReturn(customerId);
        when(session.getAttribute("role")).thenReturn("customer");
        when(request.getRequestDispatcher("pages/bookingHistory.jsp")).thenReturn(dispatcher);

        List<HashMap<String, String>> mockBookings = new ArrayList<>();
        HashMap<String, String> booking = new HashMap<>();
        booking.put("BookingID", "1");
        booking.put("Status", "Completed");
        mockBookings.add(booking);

        when(bookingServiceFacade.getCustomerBookings(customerId)).thenReturn(mockBookings);

        servlet.doGet(request, response);

        ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);
        verify(request).setAttribute(eq("bookings"), captor.capture());
        assertEquals(1, captor.getValue().size());

        verify(dispatcher).forward(request, response);
    }
}
