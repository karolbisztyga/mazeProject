package com.mazeproject.database;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

public class ModifyDatabase {
    
    public static void main(String[] args) {
        
        List<MazeEntity> mazes = getMazes();
        
        AnnotationConfiguration config = new AnnotationConfiguration();
        config.addAnnotatedClass(MazeEntity.class);
        config.addAnnotatedClass(UserEntity.class);
        config.addAnnotatedClass(PriceEntity.class);
        config.configure("hibernate.cfg.xml");
        new SchemaExport(config).create(true, true);
        
        insertTestUser();
        if(mazes != null && !mazes.isEmpty()) {
            insertMazes(mazes);
        }
    }
    
    public static void insertTestUser() {
        
        SessionFactory sf = new AnnotationConfiguration().configure()
                .addAnnotatedClass(UserEntity.class).buildSessionFactory();
        
        UserEntity user = new UserEntity(
                "asd", 
                "asd@asd.aa", 
                "5fd924625f6ab16a19cc9807c7c506ae1813490e4ba675f843d5a10e0baacdb8", 
                100);
        
        Session session = null;
        Transaction transaction = null;
        try {
            session = sf.openSession();
            transaction = session.beginTransaction();
            
            session.save(user);
            
            
            transaction.commit();
        } catch(HibernateException e) {
            if(transaction!=null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            if(session!=null) {
                session.close();
            }
        }
    }
    
    public static List<MazeEntity> getMazes() {
        List<MazeEntity> mazes = null;
        SessionFactory sf = new AnnotationConfiguration().configure()
                .addAnnotatedClass(MazeEntity.class).buildSessionFactory();
        Session session = null;
        Transaction transaction = null;
        try {
            session = sf.openSession();
            transaction = session.beginTransaction();
            String query = "FROM MazeEntity";
            mazes = session.createQuery(query).list();
            transaction.commit();
        } catch(HibernateException e) {
            if(transaction!=null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            if(session!=null) {
                session.close();
            }
        }
        return mazes;
    }
    
    public static void insertMazes(List<MazeEntity> mazes) {
        
        SessionFactory sf = new AnnotationConfiguration().configure()
                .addAnnotatedClass(MazeEntity.class).buildSessionFactory();
        Session session = null;
        Transaction transaction = null;
        try {
            session = sf.openSession();
            transaction = session.beginTransaction();
            for(MazeEntity maze : mazes) {
                session.save(maze);
            }
            transaction.commit();
        } catch(HibernateException e) {
            if(transaction!=null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            if(session!=null) {
                session.close();
            }
        }
    }
    
    public static void insertPrices() {
        Map<Class, Double> prices = new HashMap<>();
        //...
    }
}
