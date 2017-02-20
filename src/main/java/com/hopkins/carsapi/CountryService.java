package com.hopkins.carsapi;

import static com.hopkins.carsapi.MakeService.makeDao;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("country")
public class CountryService {
    public static final CountryDao countryDao = new CountryDao();
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<Country> list = countryDao.getAll();
        return Response.ok(new GenericEntity<List<Country>>(list) {}).build();
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{countryCode}")
    public Response get(@PathParam("countryCode") String countryCode) {
        if (countryCode == null || countryCode.length() != 2) {
            throw new BadRequestException("Invalid country code: " + countryCode);
        }
        Country country = countryDao.getByCode(countryCode);
        if (country == null) {
            throw new NotFoundException("Country not found with code: " + countryCode);
        }
        return Response.ok(country).build();
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{countryCode}/make")
    public Response getByCountry(@PathParam("countryCode") String countryCode) {
        if (countryCode == null || countryCode.length() != 2) {
            throw new BadRequestException("Country code must be 2 characters");
        }
        Country country = countryDao.getByCode(countryCode);
        List<Make> makeList = makeDao.getByCountryCode(countryCode);
        
        Map<String, Object> map = new HashMap<>();
        map.put("country", country);
        map.put("makes", makeList);
        
        return Response.ok(map).build();
    }
}
