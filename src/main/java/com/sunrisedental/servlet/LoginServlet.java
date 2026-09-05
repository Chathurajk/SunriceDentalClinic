package com.sunrisedental.servlet;

import com.sunrisedental.model.User;
import com.sunrisedental.service.AuthService;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;

@WebServlet("/login") public class LoginServlet extends HttpServlet {
    private final AuthService service=new AuthService();
    protected void doGet(HttpServletRequest r,HttpServletResponse s)throws ServletException,IOException {
        r.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(r,s);
    }
    protected void doPost(HttpServletRequest r,HttpServletResponse s)throws ServletException,IOException {
        try {
            User u=service.login(r.getParameter("email"),r.getParameter("password"));
            if(u==null) {
                r.setAttribute("error","Invalid email or password.");
                doGet(r,s);
                return;
            }
            r.getSession().setAttribute("loggedUser",u);
            s.sendRedirect(r.getContextPath()+("ADMIN".equals(u.getRole())?"/admin/dashboard":"/staff/dashboard"));
        }
        catch(Exception e) {
            e.printStackTrace();
            r.setAttribute("error","Server error. Check database connection.");
            doGet(r,s);
        }
    }
}
