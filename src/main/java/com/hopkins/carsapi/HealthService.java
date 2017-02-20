package com.hopkins.carsapi;

import java.sql.Connection;
import java.sql.SQLException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("health")
public class HealthService {
    
    @GET
    @Path("check")
    @Produces(MediaType.TEXT_PLAIN)
    public Response checkHealth() {
        try {
            Connection conn = DatabaseConnectionProvider.connect();
            conn.close();
            
            return Response.ok("Healthy").build();
        } catch (SQLException ex) {
            throw new ServerErrorException("Error connecting to databse", Status.INTERNAL_SERVER_ERROR, ex);
        }
    }
}
