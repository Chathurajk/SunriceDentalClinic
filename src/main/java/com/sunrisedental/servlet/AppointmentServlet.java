package com.sunrisedental.servlet;

import com.sunrisedental.dao.*;
import com.sunrisedental.model.*;
import com.sunrisedental.service.*;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;

@WebServlet("/staff/appointment") public class AppointmentServlet extends HttpServlet {
    private final AppointmentDAO dao=new AppointmentDAO();
    private final DentistDAO dentistDAO=new DentistDAO();
    private final TreatmentDAO treatmentDAO=new TreatmentDAO();
    private final AppointmentService service=new AppointmentService(dao);
    private final EmailService email=new EmailService();
    protected void doGet(HttpServletRequest r,HttpServletResponse s)throws ServletException,IOException {
        try {
            String action=r.getParameter("action");
            if("add".equals(action)||"edit".equals(action)) {
                if("edit".equals(action))r.setAttribute("appointment",dao.findById(Integer.parseInt(r.getParameter("id"))));
                load(r);
                r.getRequestDispatcher("/WEB-INF/views/appointment_form.jsp").forward(r,s);
            }
            else if("search".equals(action)) {
                r.setAttribute("appointment",dao.findByNumber(r.getParameter("number")));
                r.getRequestDispatcher("/WEB-INF/views/search.jsp").forward(r,s);
            }
            else {
                r.setAttribute("appointments",dao.findAll());
                r.getRequestDispatcher("/WEB-INF/views/appointments.jsp").forward(r,s);
            }
        }
        catch(Exception e) {
            throw new ServletException(e);
        }
    }
    protected void doPost(HttpServletRequest r,HttpServletResponse s)throws ServletException,IOException {
        try {
            String action=r.getParameter("action");
            if("cancel".equals(action)) {
                dao.cancel(Integer.parseInt(r.getParameter("id")));
                s.sendRedirect(r.getContextPath()+"/staff/appointment");
                return;
            }
            if("complete".equals(action)) {
                dao.complete(Integer.parseInt(r.getParameter("id")),Integer.parseInt(r.getParameter("actualTreatmentId")));
                s.sendRedirect(r.getContextPath()+"/staff/appointment");
                return;
            }
            Appointment a=new Appointment();
            String id=r.getParameter("id");
            if(id!=null&&!id.isBlank())a.setId(Integer.parseInt(id));
            a.setAppointmentNumber(r.getParameter("appointmentNumber"));
            a.setPatientName(r.getParameter("patientName"));
            a.setPatientEmail(r.getParameter("patientEmail"));
            a.setAddress(r.getParameter("address"));
            a.setContactNumber(r.getParameter("contactNumber"));
            a.setDentistId(Integer.parseInt(r.getParameter("dentistId")));
            a.setRequestedTreatmentId(Integer.parseInt(r.getParameter("requestedTreatmentId")));
            a.setAppointmentDate(r.getParameter("appointmentDate"));
            a.setAppointmentTime(r.getParameter("appointmentTime"));
            a.setNotes(r.getParameter("notes"));
            if(a.getId()>0)service.update(a);
            else {
                service.create(a);
                Appointment saved=dao.findById(a.getId());
                if(saved!=null)email.sendAppointmentConfirmation(saved);
            }
            s.sendRedirect(r.getContextPath()+"/staff/appointment");
        }
        catch(Exception e) {
            r.setAttribute("error",e.getMessage());
            try {
                load(r);
            }
            catch(Exception ignored) {
            }
            r.getRequestDispatcher("/WEB-INF/views/appointment_form.jsp").forward(r,s);
        }
    }
    private void load(HttpServletRequest r)throws Exception {
        r.setAttribute("dentists",dentistDAO.findActive());
        r.setAttribute("treatments",treatmentDAO.findActive());
    }


}
