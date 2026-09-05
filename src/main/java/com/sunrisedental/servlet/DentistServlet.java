package com.sunrisedental.servlet;

import com.sunrisedental.dao.DentistDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/admin/dentists")
public class DentistServlet extends HttpServlet {
    private final DentistDAO dentistDAO = new DentistDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("dentists", dentistDAO.findAll());
            request.getRequestDispatcher("/WEB-INF/views/dentists.jsp")
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
                dentistDAO.add(
                        request.getParameter("name"),
                        request.getParameter("specialization"),
                        request.getParameter("contactNumber")
                );
            } else if ("delete".equals(action)) {
                dentistDAO.deactivate(Integer.parseInt(request.getParameter("id")));
            }

            response.sendRedirect(request.getContextPath() + "/admin/dentists");
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            doGet(request, response);
        }
    }
}
