package com.hopkins.carsapi;

import java.net.URI;
import java.util.List;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("make")
public class MakeService {
    
    public static final MakeDao makeDao = new MakeDao();
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<Make> list = makeDao.getAll();
        return Response.ok(new GenericEntity<List<Make>>(list) {}).build();
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{makeId}")
    public Response get(@PathParam("makeId") int makeId) {
        Make make = makeDao.getById(makeId);
        if (make == null) {
            throw new NotFoundException("Make not found with id: " + makeId);
        }
        return Response.ok().entity(make).build();
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{makeId}/model")
    public Response getModels(@PathParam("makeId") int makeId) {
        Make make = makeDao.getById(makeId);
        if (make == null) {
            throw new NotFoundException("Make not found with id: " + makeId);
        }
        
        List<Model> list = ModelService.modelDao.getByMake(makeId);
        return Response.ok(new GenericEntity<List<Model>>(list) {}).build();
    }
    
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{makeId}")
    public Response delete(@PathParam("makeId") int makeId) {
        boolean isDeleted = makeDao.delete(makeId);
        if (isDeleted) {
            return Response.ok().build();
        } else {
            throw new NotFoundException("Make not found with id: " + makeId);
        }
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response add(@FormParam("name") String name, @FormParam("countryCode") String countryCode) {
        if (name == null || name.trim().length() == 0) {
            throw new BadRequestException("Make requires non-emtpy name");
        }
        if (countryCode == null || countryCode.trim().length() != 2) {
            throw new BadRequestException("Make requires 2 character countryCode");
        }
        Make make = makeDao.insert(name.trim(), countryCode.trim().toUpperCase());
        URI location = URI.create("/make/" + make.getId());
        return Response.created(location).build();
    }
}
