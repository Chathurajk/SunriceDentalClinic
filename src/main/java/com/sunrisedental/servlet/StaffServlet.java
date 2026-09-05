package com.sunrisedental.servlet;

import com.sunrisedental.util.DbConnection;
import com.sunrisedental.util.PasswordUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/admin/staff")
public class StaffServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try (Connection connection = DbConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM users WHERE role='STAFF' ORDER BY active DESC, full_name");
             ResultSet resultSet = statement.executeQuery()) {

            List<Map<String, Object>> staffList = new ArrayList<>();

            while (resultSet.next()) {
                Map<String, Object> staff = new HashMap<>();
                staff.put("id", resultSet.getInt("id"));
                staff.put("name", resultSet.getString("full_name"));
                staff.put("email", resultSet.getString("email"));
                staff.put("contact", resultSet.getString("contact_number"));
                staff.put("active", resultSet.getBoolean("active"));
                staffList.add(staff);
            }

            request.setAttribute("staffList", staffList);
            request.getRequestDispatcher("/WEB-INF/views/staff.jsp")
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
                try (Connection connection = DbConnection.getInstance().getConnection();
                     PreparedStatement statement = connection.prepareStatement(
                             "INSERT INTO users(full_name,email,password_hash,role,contact_number) "
                                     + "VALUES(?,?,?,'STAFF',?)")) {
                    statement.setString(1, request.getParameter("fullName"));
                    statement.setString(2, request.getParameter("email"));
                    statement.setString(3, PasswordUtil.sha256(request.getParameter("password")));
                    statement.setString(4, request.getParameter("contactNumber"));
                    statement.executeUpdate();
                }
            } else if ("delete".equals(action)) {
                try (Connection connection = DbConnection.getInstance().getConnection();
                     PreparedStatement statement = connection.prepareStatement(
                             "UPDATE users SET active=FALSE WHERE id=? AND role='STAFF'")) {
                    statement.setInt(1, Integer.parseInt(request.getParameter("id")));
                    statement.executeUpdate();
                }
            }

            response.sendRedirect(request.getContextPath() + "/admin/staff");
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            doGet(request, response);
        }
    }
}
