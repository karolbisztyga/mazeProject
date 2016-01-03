package com.mazeproject.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.mazeproject.servlets.support.Security;

public class DefaultServlet extends org.apache.catalina.servlets.DefaultServlet {

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
        String[] arr = request.getRequestURI().split("\\.");
        if(arr[arr.length-1].equals("html")) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        try {
            String securityLevel = request.getAttribute("securityLevel").toString();
            if(securityLevel.equals("user")) {
                if(!Security.login(request.getSession())) {
                    response.sendRedirect("login");
                    return;
                }
            }
            response.setHeader("Cache-Control", "private, no-store, no-cache, must-revalidate");
            response.setHeader("Pragma", "no-cache");
        } catch(NullPointerException e) {
            
        }
        super.doGet(request, response);
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
        super.doPost(request, response);
    }

}
