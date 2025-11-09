package app.controller;

import java.util.List;

import app.service.CoordinatesService;
import app.dto.coordinates.CoordinatesResponseDTO;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/coordinates")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CoordinatesController {
    private CoordinatesService service;
    
    public CoordinatesController() {}

    @Inject
    public CoordinatesController(CoordinatesService service) {
        this.service = service;
    }

    @GET
    public Response getAll() {
        List<CoordinatesResponseDTO> coordinatess = service.getAll();
        return Response.ok(coordinatess).build();
    }
}
