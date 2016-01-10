package com.mazeproject.servlets.ajax;

import com.mazeproject.exceptions.ImpossibleMazeException;
import com.mazeproject.exceptions.WrongMazeFormatException;
import com.mazeproject.objects.Maze;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;

public class CheckMaze extends HttpServlet {

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
        throw new UnsupportedOperationException("This operation is not supported");
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
        JSONObject ob = new JSONObject();
        response.setContentType("application/json");
        try {
            Maze maze = Maze.decode(request.getParameter("maze"));
            if(!Maze.goThroughMaze(maze)) {
                throw new ImpossibleMazeException("Maze seems to be impossible");
            }
            
            //check price with user in database and if all ok save maze in db...
        } catch (WrongMazeFormatException | ImpossibleMazeException e) {
            ob.put("type", "error");
            ob.put("message", e.getMessage());
            response.getWriter().print(ob.toString());
            return;
        } catch(InterruptedException e) {
            ob.put("type", "error");
            ob.put("message", "Could not check the maze, probably it is too complex");
            response.getWriter().print(ob.toString());
            return;
        }
        ob.put("type", "success");
        ob.put("message", "maze ok");
        response.getWriter().print(ob.toString());
    }
}
