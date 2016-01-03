package com.mazeproject.database;

import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

public class ModifyDatabase {
    
    public static void main(String[] args) {
        AnnotationConfiguration config = new AnnotationConfiguration();
        config.addAnnotatedClass(MazeEntity.class);
        config.addAnnotatedClass(UserEntity.class);
        config.configure("hibernate.cfg.xml");
        new SchemaExport(config).create(true, true);
    }
}
