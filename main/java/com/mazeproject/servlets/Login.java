package com.mazeproject.servlets;

import com.mazeproject.servlets.support.Security;
import com.mazeproject.servlets.support.UserSessionStorage;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONObject;

public class Login extends HttpServlet {

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher rd = getServletContext().getNamedDispatcher("default");
    	HttpServletRequest wrapped = new HttpServletRequestWrapper(request) {
            @Override
            public String getServletPath() {
                return "login.html";
            }
    	};
        wrapped.setAttribute("securityLevel", getServletConfig().getInitParameter("securityLevel"));
    	rd.forward(wrapped, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        if(     request.getParameter("requestType")!=null && 
                request.getParameter("requestType").equals("ajax")) {
            JSONObject ob = new JSONObject();
            if(session.getAttribute("loginData")!=null) {
                String name = ((UserSessionStorage)session.getAttribute("loginData")).getName();
                ob.put("name", name);
                ob.put("loggedIn",true);
            } else {
                ob.put("loggedIn",false);
            }
            response.setContentType("application/json");
            response.getWriter().print(ob.toString());
        } else {
            if(Security.login(
                    request.getParameter("name"),
                    request.getParameter("password"),
                    session)) {
                response.sendRedirect("home");
            } else {
                response.sendRedirect("login");
            }
        }
    }
    
    
    
}
