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

@Path("model")
public class ModelService {
    public static final ModelDao modelDao = new ModelDao();
    
    @GET
    @Path("{modelId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("modelId") int modelId) {
        Model model = modelDao.getById(modelId);
        if (model == null) {
            throw new NotFoundException("Model not found.  modelId: " + modelId);
        }
        return Response.ok(model).build();
    }
    
    @DELETE
    @Path("{modelId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("modelId") int modelId) {
        boolean isDeleted = modelDao.delete(modelId);
        if (!isDeleted) {
            throw new NotFoundException("Model not found. modelId: " + modelId);
        }
        return Response.ok().build();
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insert(@FormParam("makeId") int makeId, @FormParam("name") String name, @FormParam("type") String type) {
        if (type == null || type.trim().length() == 0) {
            throw new BadRequestException("most provide a valid type");
        }
        ModelType modelType = ModelType.valueOf(type.trim().toUpperCase());
        Model model = modelDao.insert(makeId, name, modelType);
        URI location = URI.create("/model/" + model.getId());
        return Response.created(location).build();
    }
    
    @GET
    @Path("type/{type}")
    public Response getByType(@PathParam("type") String type) {
        if (type == null || type.trim().length() == 0) {
            throw new BadRequestException("Must provide valid type.  type: " + type);
        }
        ModelType modelType = ModelType.valueOf(type.trim().toUpperCase());
        if (modelType == null) {
            throw new NotFoundException("Model type not found.  type: " + type);
        }
        
        List<Model> list = modelDao.getByType(modelType);
        return Response.ok(new GenericEntity<List<Model>>(list) {}).build();
    }
}
