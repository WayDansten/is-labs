package app.controller;

import java.util.List;

import app.service.PersonService;
import app.dto.person.PersonResponseDTO;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/person")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PersonController {
    private PersonService service;
    
    public PersonController() {}

    @Inject
    public PersonController(PersonService service) {
        this.service = service;
    }

    @GET
    public Response getAll() {
        List<PersonResponseDTO> persons = service.getAll();
        return Response.ok(persons).build();
    }
}
