package com.hopkins.carsapi;

import java.sql.Connection;
import java.sql.SQLException;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

public final class App {
    private static final int PORT = 2222;
    
    public static void main(String[] args) throws Exception {
        ResourceConfig config = new ResourceConfig();
        config.packages("com.hopkins.carsapi");
        config.register(JacksonFeature.class);
        ServletHolder servlet = new ServletHolder(new ServletContainer(config));

        Server server = new Server(PORT);
        ServletContextHandler context = new ServletContextHandler(server, "/*");
        context.addServlet(servlet, "/*");
        
        warmUp();
        
        try {
            server.start();
            server.join();
        } finally {
            server.destroy();
        }
    }
    
    private static void warmUp() {
        long startTime = System.currentTimeMillis();
        try (Connection conn = DatabaseConnectionProvider.connect()) {
            // noop
        } catch (SQLException ex) {
            throw new RuntimeException("Error establishing initial connection", ex);
        }
        long duration = System.currentTimeMillis() - startTime;
        System.err.println("Warm up complete, connection took: " + duration + "ms");
    }
}
