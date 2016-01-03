package com.mazeproject.servlets.support;

import com.mazeproject.database.UserEntity;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;

public class Security {
    
    public static boolean login(String name, String password, HttpSession session) {
        SessionFactory sessionFactory = new AnnotationConfiguration().configure()
                    .addAnnotatedClass(UserEntity.class)
                    .buildSessionFactory();
        Session databaseSession = null;
        Transaction transaction = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes("UTF-8"));
            String hashedPassword = new BigInteger(1, md.digest()).toString(16);
            databaseSession = sessionFactory.openSession();
            transaction = databaseSession.beginTransaction();
            String query = "FROM UserEntity u WHERE u.name=:name AND password=:password";
            List result = databaseSession.createQuery(query)
                    .setParameter("name", name)
                    .setParameter("password", hashedPassword)
                    .list();
            transaction.commit();
            if(result.size()==1) {
                session.setAttribute("loginData", UserSessionStorage.getInstance(name, password));
                return true;
            } else {
                session.removeAttribute("loginData");
                return false;
            }
        } catch(NullPointerException | NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            if(transaction!=null) {
                transaction.rollback();
            }
            return false;
        } finally {
            if(databaseSession!=null) {
                databaseSession.close();
            }
        }
    }
    
    
    public static boolean login(HttpSession session) {
        try {
            UserSessionStorage uss = (UserSessionStorage)session.getAttribute("loginData");
            String name = uss.getName();
            String password = uss.getPassword();
            return Security.login(name, password, session);
        } catch(NullPointerException e) {
            return false;
        }
    }
    
    public static void logout(HttpSession session) {
        session.removeAttribute("loginData");
        UserSessionStorage.getInstance().setName(null);
        UserSessionStorage.getInstance().setPassword(null);
    }
    
}
