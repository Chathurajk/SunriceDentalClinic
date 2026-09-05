package com.sunrisedental.servlet;

import com.sunrisedental.dao.TreatmentDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/admin/treatments")
public class TreatmentServlet extends HttpServlet {
    private final TreatmentDAO treatmentDAO = new TreatmentDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("treatments", treatmentDAO.findAll());
            request.getRequestDispatcher("/WEB-INF/views/treatments.jsp")
                    .forward(request, response);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String action = request.getParameter("action");

            if ("add".equals(action)) {
                String name = request.getParameter("name");
                String description = request.getParameter("description");
                double charge = Double.parseDouble(request.getParameter("charge"));
                validateCharge(charge);
                treatmentDAO.add(name, description, charge);
            } else if ("edit".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                String name = request.getParameter("name");
                String description = request.getParameter("description");
                double charge = Double.parseDouble(request.getParameter("charge"));
                validateCharge(charge);
                treatmentDAO.update(id, name, description, charge);
            } else if ("delete".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                treatmentDAO.deactivate(id);
            }

            response.sendRedirect(request.getContextPath() + "/admin/treatments");
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            doGet(request, response);
        }
    }

    private void validateCharge(double charge) {
        if (charge < 0) {
            throw new IllegalArgumentException("Treatment charge cannot be negative.");
        }
    }
}
