package com.mazeproject.servlets.ajax;

import com.mazeproject.database.MazeEntity;
import com.mazeproject.database.UserEntity;
import com.mazeproject.exceptions.WrongMazeFormatException;
import com.mazeproject.servlets.support.Security;
import com.mazeproject.servlets.support.UserSessionStorage;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.json.JSONObject;
import com.mazeproject.objects.Maze;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GetMazes extends HttpServlet {
    
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
        try {
            if(!Security.login(request.getSession())) {
                throw new SecurityException("You are not logged in");
            }
            String scope = request.getParameter("scope");
            switch(scope) {
                case "all": {
                    Integer from = (request.getParameter("from")==null)
                            ? 0
                            : Integer.parseInt(request.getParameter("from"));
                    Integer to = (request.getParameter("to")==null)
                            ? 0
                            : Integer.parseInt(request.getParameter("to"));
                    ob = this.getAllMazes(from, to);
                    break;
                }
                case "currentUser": {
                    getUsersMazes(UserSessionStorage.getInstance().getUserEntity());
                    break;
                }
                case "user": {
                    //...
                    break;
                }
            }
        } catch(Exception e) {
            ob.put("type", "error");
            ob.put("message", e.getMessage());
        }
        response.setContentType("application/json");
        response.getWriter().print(ob.toString());
    }
    
    private JSONObject getAllMazes(int from, int to) {
        JSONObject resultObject = new JSONObject();
        Session databaseSession = null;
        Transaction transaction = null;
        try {
            
            //initiating database...
            SessionFactory sessionFactory = new AnnotationConfiguration().configure()
                    .addAnnotatedClass(UserEntity.class)
                    .addAnnotatedClass(MazeEntity.class)
                    .buildSessionFactory();
            databaseSession = sessionFactory.openSession();
            transaction = databaseSession.beginTransaction();
            String query = "FROM MazeEntity";
            List mazes = databaseSession.createQuery(query)
                    .setFirstResult(from)
                    .setMaxResults(to+from)
                    .list();
            transaction.commit();
            Iterator iterator = mazes.iterator();
            int counter = 0;
            while(iterator.hasNext()) {
                MazeEntity mazeEntity = (MazeEntity)iterator.next();
                Maze maze = Maze.decode(mazeEntity.getCode());
                JSONObject ob = new JSONObject();
                UserEntity author = mazeEntity.getAuthor();
                ob.put("id", mazeEntity.getId());
                ob.put("width", maze.getWidth());
                ob.put("height", maze.getHeight());
                ob.put("expires", mazeEntity.getExpirationDate());
                ob.put("author", author.getName());
                resultObject.put((counter++)+"", ob);
            }
            resultObject.put("type", "success");
        } catch (SecurityException | WrongMazeFormatException e) {
            resultObject = new JSONObject();
            resultObject.put("type", "error");
            resultObject.put("message", e.getMessage());
        } catch(HibernateException e) {
            e.printStackTrace();
            resultObject = new JSONObject();
            resultObject.put("type", "error");
            resultObject.put("message", "Sorry, database error occured. Please, contact the administrator.");
            if(transaction!=null) {
                transaction.rollback();
            }
        } finally {
            if(databaseSession!=null) {
                databaseSession.close();
            }
        }
        return resultObject;
    }
    
    private JSONObject getUsersMazes(UserEntity user) {
        JSONObject resultObject = new JSONObject();
        Session databaseSession = null;
        Transaction transaction = null;
        try {
            
            //initiating database...
            SessionFactory sessionFactory = new AnnotationConfiguration().configure()
                    .addAnnotatedClass(UserEntity.class)
                    .addAnnotatedClass(MazeEntity.class)
                    .buildSessionFactory();
            databaseSession = sessionFactory.openSession();
            transaction = databaseSession.beginTransaction();
            String query = "FROM MazeEntity WHERE author=:author";
            List mazes = databaseSession.createQuery(query)
                    .setParameter("author", user)
                    .list();
            transaction.commit();
            Iterator iterator = mazes.iterator();
            int counter = 0;
            while(iterator.hasNext()) {
                MazeEntity mazeEntity = (MazeEntity)iterator.next();
                Maze maze = Maze.decode(mazeEntity.getCode());
                JSONObject ob = new JSONObject();
                UserEntity author = mazeEntity.getAuthor();
                ob.put("id", mazeEntity.getId());
                ob.put("width", maze.getWidth());
                ob.put("height", maze.getHeight());
                ob.put("expires", mazeEntity.getExpirationDate());
                ob.put("author", author.getName());
                resultObject.put((counter++)+"", ob);
            }
            resultObject.put("type", "success");
        } catch (SecurityException | WrongMazeFormatException e) {
            resultObject = new JSONObject();
            resultObject.put("type", "error");
            resultObject.put("message", e.getMessage());
        } catch(HibernateException e) {
            e.printStackTrace();
            resultObject = new JSONObject();
            resultObject.put("type", "error");
            resultObject.put("message", "Sorry, database error occured. Please, contact the administrator.");
            if(transaction!=null) {
                transaction.rollback();
            }
        } finally {
            if(databaseSession!=null) {
                databaseSession.close();
            }
        }
        return resultObject;
    }
    
}
