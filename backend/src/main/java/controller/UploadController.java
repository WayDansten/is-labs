package controller;

import java.util.List;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import dto.misc.UploadResponseDTO;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import service.UploadService;

@Path("/upload")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UploadController {
    private UploadService service;
    
    public UploadController() {}

    @Inject
    public UploadController(UploadService service) {
        this.service = service;
    }

    @GET
    public Response getAll() {
        List<UploadResponseDTO> upload = service.getAll();
        return Response.ok(upload).build();
    }
}
