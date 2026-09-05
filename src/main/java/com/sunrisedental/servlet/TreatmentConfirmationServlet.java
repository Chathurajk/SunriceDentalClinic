package com.sunrisedental.servlet;

import com.sunrisedental.dao.AppointmentDAO;
import com.sunrisedental.dao.TreatmentDAO;
import com.sunrisedental.model.Appointment;
import com.sunrisedental.service.BillingService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/staff/confirm-treatment")
public class TreatmentConfirmationServlet extends HttpServlet {
    private final AppointmentDAO appointmentDAO = new AppointmentDAO();
    private final TreatmentDAO treatmentDAO = new TreatmentDAO();
    private final BillingService billingService = new BillingService();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Appointment appointment = appointmentDAO.findById(id);

            if (appointment == null) {
                throw new IllegalArgumentException("Appointment not found.");
            }

            request.setAttribute("appointment", appointment);
            request.setAttribute("treatments", treatmentDAO.findActive());
            request.getRequestDispatcher("/WEB-INF/views/confirm_treatment.jsp")
                    .forward(request, response);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int appointmentId = Integer.parseInt(request.getParameter("appointmentId"));
            int actualTreatmentId = Integer.parseInt(request.getParameter("actualTreatmentId"));
            double consultationFee = Double.parseDouble(request.getParameter("consultationFee"));

            billingService.completeTreatmentAndGenerateBill(
                    appointmentId,
                    actualTreatmentId,
                    consultationFee
            );

            response.sendRedirect(request.getContextPath()
                    + "/staff/bill?appointmentId=" + appointmentId);
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            doGet(request, response);
        }
    }
}
