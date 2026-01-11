package controller;

import java.io.ByteArrayInputStream;
import java.util.List;

import dto.download.DownloadResponseDTO;
import dto.upload.UploadResponseDTO;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.inject.Inject;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import service.UploadService;

@Path("/upload")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
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

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response download(@PathParam("id") Integer id) throws Exception {
        DownloadResponseDTO dto = service.download(id);
        return Response.ok(new ByteArrayInputStream(dto.getContent()))
                .type(MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename=\"" + dto.getFileName() + "\"")
                .header("Content-Length", dto.getContent().length)
                .build();
    }
}
