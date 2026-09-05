package com.sunrisedental.filter;

import com.sunrisedental.model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.*;

@WebFilter( {
    "/admin/*","/staff/*"
}
) public class AuthFilter implements Filter {
    public void doFilter(ServletRequest req,ServletResponse res,FilterChain chain)throws IOException,ServletException {
        HttpServletRequest r=(HttpServletRequest)req;
        HttpServletResponse s=(HttpServletResponse)res;
        User u=(User)r.getSession().getAttribute("loggedUser");
        String path=r.getServletPath();
        if(u==null) {
            s.sendRedirect(r.getContextPath()+"/login");
            return;
        }
        if(path.startsWith("/admin")&&! "ADMIN".equals(u.getRole())) {
            s.sendError(403);
            return;
        }
        chain.doFilter(req,res);
    }
}
