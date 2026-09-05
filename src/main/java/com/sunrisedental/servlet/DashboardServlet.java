package com.sunrisedental.servlet;

import com.sunrisedental.dao.AppointmentDAO;
import com.sunrisedental.dao.BillDAO;
import com.sunrisedental.dao.DentistDAO;
import com.sunrisedental.dao.ReportDAO;
import com.sunrisedental.dao.TreatmentDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet({
        "/admin/dashboard",
        "/staff/dashboard"
})
public class DashboardServlet extends HttpServlet {
    private final AppointmentDAO appointmentDAO = new AppointmentDAO();
    private final DentistDAO dentistDAO = new DentistDAO();
    private final TreatmentDAO treatmentDAO = new TreatmentDAO();
    private final BillDAO billDAO = new BillDAO();
    private final ReportDAO reportDAO = new ReportDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("totalAppointments", appointmentDAO.countAll());
            request.setAttribute("todayAppointments", appointmentDAO.countToday());
            request.setAttribute("bookedAppointments", appointmentDAO.count("BOOKED"));
            request.setAttribute("completedAppointments", appointmentDAO.count("COMPLETED"));
            request.setAttribute("cancelledAppointments", appointmentDAO.count("CANCELLED"));
            request.setAttribute("activeDentists", dentistDAO.countActive());
            request.setAttribute("treatments", treatmentDAO.findActive());
            request.setAttribute("appointments", appointmentDAO.findAll());
            request.setAttribute("recentBills", billDAO.findRecent(8));
            request.setAttribute("totalRevenue", reportDAO.getTotalRevenue());
            request.setAttribute("todayRevenue", reportDAO.getTodayRevenue());

            if (request.getServletPath().startsWith("/admin")) {
                request.setAttribute("treatmentReports", reportDAO.getTreatmentReport());
                request.setAttribute("dentistReports", reportDAO.getDentistReport());
            }

            String view = request.getServletPath().startsWith("/admin")
                    ? "/WEB-INF/views/admin_dashboard.jsp"
                    : "/WEB-INF/views/staff_dashboard.jsp";

            request.getRequestDispatcher(view).forward(request, response);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
