package com.hopkins.carsapi;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {    
    private static final String KEY_SERVER_PORT = "server.port";
    private static final String KEY_DB_USERNAME = "db.username";
    private static final String KEY_DB_PASSWORD = "db.password";
    private static final String KEY_DB_DATABASE = "db.database";
    private static final String KEY_DB_SERVER = "db.server";
    private static final String KEY_DB_PORT = "db.port";
    
    private int serverPort;
    private String dbUsername;
    private String dbPassword;
    private String dbDatabase;
    private String dbServer;
    private int dbPort;
    
    public void readProperties(File file) throws IOException {
        Properties props = new Properties();
        props.load(new FileInputStream(file));
        
        serverPort = Integer.parseInt(props.getProperty(KEY_SERVER_PORT));
        dbUsername = props.getProperty(KEY_DB_USERNAME);
        dbPassword = props.getProperty(KEY_DB_PASSWORD);
        dbDatabase = props.getProperty(KEY_DB_DATABASE);
        dbServer = props.getProperty(KEY_DB_SERVER);
        dbPort = Integer.parseInt(props.getProperty(KEY_DB_PORT));
    }
    
    public int getServerPort() {
        return serverPort;
    }
    
    public String getDbUsername() {
        return dbUsername;
    }
    
    public String getDbPassword() {
        return dbPassword;
    }
    
    public String getDbDatabase() {
        return dbDatabase;
    }
    
    public String getDbServer() {
        return dbServer;
    }
    
    public int getDbPort() {
        return dbPort;
    }
}
