package com.hopkins.carsapi;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

public final class App {
    
    public static void main(String[] args) throws Exception {
        ResourceConfig config = new ResourceConfig();
        config.packages("com.hopkins.carsapi");
        config.register(JacksonFeature.class);
        ServletHolder servlet = new ServletHolder(new ServletContainer(config));
        
        // Load the application configuration
        Config appConfig = new Config();
        appConfig.readProperties(new File("config.properties"));

        // Initialize the database connection
        initDatabase(appConfig);
        
        // Configure the Jetty server
        Server server = new Server(appConfig.getServerPort());
        ServletContextHandler context = new ServletContextHandler(server, "/*");
        context.addServlet(servlet, "/*");
        
        try {
            // Start the Jetty server
            server.start();
            server.join();
        } finally {
            server.destroy();
        }
    }
    
    private static void initDatabase(Config appConfig) {
        long startTime = System.currentTimeMillis();
        DatabaseConnectionProvider.initDataSource(appConfig);
        try (Connection conn = DatabaseConnectionProvider.connect()) {
            // noop
        } catch (SQLException ex) {
            throw new RuntimeException("Error establishing initial connection", ex);
        }
        long duration = System.currentTimeMillis() - startTime;
        System.err.println("Warm up complete, connection took: " + duration + "ms");
    }
}
