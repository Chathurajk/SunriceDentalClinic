package com.sunrisedental.servlet;

import com.sunrisedental.model.Bill;
import com.sunrisedental.service.BillingService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/staff/bill")
public class BillServlet extends HttpServlet {
    private final BillingService billingService = new BillingService();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int appointmentId = Integer.parseInt(request.getParameter("appointmentId"));
            Bill bill = billingService.findByAppointmentId(appointmentId);

            if (bill == null) {
                throw new IllegalArgumentException("Bill has not been generated yet.");
            }

            request.setAttribute("bill", bill);
            request.getRequestDispatcher("/WEB-INF/views/bill.jsp")
                    .forward(request, response);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
