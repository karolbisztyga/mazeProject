package com.mazeproject.servlets.ajax;

import com.mazeproject.database.MazeEntity;
import com.mazeproject.database.UserEntity;
import com.mazeproject.exceptions.ImpossibleMazeException;
import com.mazeproject.exceptions.UnableToFinishOrderedOperationsException;
import com.mazeproject.exceptions.WrongMazeFormatException;
import com.mazeproject.objects.Maze;
import com.mazeproject.servlets.support.Security;
import com.mazeproject.servlets.support.UserSessionStorage;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
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
        HttpSession session = request.getSession();
        JSONObject ob = new JSONObject();
        response.setContentType("application/json");
        Session databaseSession = null;
        Transaction transaction = null;
        try {
            Maze maze = Maze.decode(request.getParameter("maze"));
            if(!Maze.goThroughMaze(maze)) {
                throw new ImpossibleMazeException("Maze seems to be impossible");
            }
            if(!Security.login(request.getSession())) {
                throw new SecurityException("You are not logged in");
            }
            int price = maze.getPrice(false);
            
            //initiating database...
            SessionFactory sessionFactory = new AnnotationConfiguration().configure()
                    .addAnnotatedClass(UserEntity.class)
                    .addAnnotatedClass(MazeEntity.class)
                    .buildSessionFactory();
            databaseSession = sessionFactory.openSession();
            transaction = databaseSession.beginTransaction();
            //checking user's money
            String userName = ((UserSessionStorage)session.getAttribute("loginData")).getName();
            String query = "FROM UserEntity u WHERE name=:name";
            UserEntity user = (UserEntity)databaseSession.createQuery(query)
                    .setParameter("name", userName).uniqueResult();
            int money = user.getMoney();
            if(money-price <= 0) {
                throw new UnableToFinishOrderedOperationsException("Not enough money");
            }
            user.setMoney(money-price);
            databaseSession.update(user);
            //saving maze
            MazeEntity mazeEntity = new MazeEntity();
            mazeEntity.setCode(maze.encode());
            mazeEntity.setCreatedDate(System.currentTimeMillis());
            int id = (Integer)databaseSession.save(mazeEntity);
            transaction.commit();
            
        } catch (WrongMazeFormatException | 
                ImpossibleMazeException | 
                SecurityException |
                UnableToFinishOrderedOperationsException e) {
            ob.put("type", "error");
            ob.put("message", e.getMessage());
            response.getWriter().print(ob.toString());
            return;
        } catch(InterruptedException | ExecutionException e) {
            ob.put("type", "error");
            ob.put("message", "Could not check the maze, probably it is too complex");
            response.getWriter().print(ob.toString());
            return;
        } catch(HibernateException e) {
            e.printStackTrace();
            if(transaction!=null) {
                transaction.rollback();
            }
        } finally {
            if(databaseSession!=null) {
                databaseSession.close();
            }
        }
        ob.put("type", "success");
        ob.put("message", "maze ok");
        response.getWriter().print(ob.toString());
    }
}
