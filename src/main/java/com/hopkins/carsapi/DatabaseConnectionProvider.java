package com.hopkins.carsapi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionProvider {
    
    private static final String MYSQL_DRIVER_CLASS_NAME = 
            "com.mysql.jdbc.Driver";
    
    private static Class<?> driverClass;
    
    private static synchronized void initDriverClass() {
        if (driverClass == null) {
            try {
                driverClass = Class.forName(MYSQL_DRIVER_CLASS_NAME);
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException("Error loading MySQL driver", ex);
            }
        }
    }
    
    public static Connection connect() {
        initDriverClass();
        
        String database = System.getProperty("carsapi.db.database");
        String user = System.getProperty("carsapi.db.user");
        String pass = System.getProperty("carsapi.db.pass");
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/" + database, user, pass);
        } catch (SQLException ex) {
            throw new RuntimeException("Error connecting to MySQL", ex);
        }
    }
    
}
