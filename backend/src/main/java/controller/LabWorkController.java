package controller;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import dto.labwork.LabWorkRequestDTO;
import dto.labwork.LabWorkResponseDTO;
import dto.misc.DifficultyRequestDTO;
import dto.misc.LongResponseDTO;
import dto.string.StringRequestDTO;
import dto.string.StringResponseDTO;
import dto.upload.UploadRequestDTO;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;
import service.LabWorkService;
import util.ImportFileFormat;
import util.MessageConstants;

@Path("/labwork")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class LabWorkController {
    private LabWorkService service;

    public LabWorkController() {}

    @Inject
    public LabWorkController(LabWorkService service) {
        this.service = service;
    }

    @POST
    public Response add(LabWorkRequestDTO dto) {
        service.add(dto);
        return Response.status(Response.Status.CREATED)
                .entity(new StringResponseDTO(MessageConstants.OK.getMessage()))
                .build();
    }
    
    @POST
    @Path("/batch")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addBatch(MultipartFormDataInput input) {
        service.addBatch(from(input));
        return Response.status(Response.Status.CREATED)
                .entity(new StringResponseDTO(MessageConstants.OK.getMessage()))
                .build();
    }

    @PATCH
    public Response update(LabWorkRequestDTO dto) {
        service.update(dto);
        return Response.ok(new StringResponseDTO(MessageConstants.OK.getMessage()))
                .build();
    }

    @PATCH
    @Path("/difficulty")
    public Response lowerDifficulty(DifficultyRequestDTO dto) {
        service.lowerDifficulty(dto);
        return Response.ok(new StringResponseDTO(MessageConstants.OK.getMessage()))
                .build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Integer id) {
        service.delete(id);
        return Response.ok(new StringResponseDTO(MessageConstants.OK.getMessage()))
                .build();
    }

    @DELETE
    @Path("/author")
    public Response deleteByAuthor(StringRequestDTO dto) {
        service.deleteByAuthor(dto.getString());
        return Response.ok(new StringResponseDTO(MessageConstants.OK.getMessage()))
                .build();
    }

    @GET
    public Response getAll() {
        List<LabWorkResponseDTO> labWorks = service.getAll();
        return Response.ok(labWorks)
                .build();
    }

    @GET
    @Path("average_point")
    public Response countGreaterThanAveragePoint(@QueryParam("averagePoint") Float averagePoint) {
        Long count = service.countGreaterThanAveragePoint(averagePoint);
        return Response.ok(new LongResponseDTO(count))
                .build();
    }

    @GET
    @Path("description")
    public Response getByDescription(@QueryParam("prefix") String prefix) {
        List<String> data = service.getByDescription(prefix);
        if (data.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new StringResponseDTO(MessageConstants.ERR_NOT_FOUND.getMessage()))
                    .build();
        }
        String names = data.stream().collect(Collectors.joining(", "));
        String responseString = String.format("Found lab works: %s.", names);
        return Response.ok(new StringResponseDTO(responseString)).build();
    }

    

    public UploadRequestDTO from(MultipartFormDataInput input) {
        InputPart filePart = getInputPart(input);
        String fileName = getFileName(filePart);
        ImportFileFormat format = getFormat(fileName);
        InputStream fileStream = getInputStream(filePart);

        UploadRequestDTO dto = new UploadRequestDTO();
        dto.setFileName(fileName);
        dto.setFileStream(fileStream);
        dto.setFormat(format);

        return dto;
    }

    private static InputStream getInputStream(InputPart filePart) {
        InputStream fileStream;
        try {
            fileStream = filePart.getBody(InputStream.class, null);
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not extract file input stream", e);
        }
        return fileStream;
    }

    private InputPart getInputPart(MultipartFormDataInput input) {
        Map<String, List<InputPart>> formParts = input.getFormDataMap();

        List<InputPart> fileParts = formParts.get("file");
        if (fileParts == null || fileParts.isEmpty()) {
            throw new IllegalArgumentException("File was not provided (expected form-data param 'file')");
        }
        if (fileParts.size() > 1) {
            throw new IllegalArgumentException(String.format("Expected only one file, got: %d", fileParts.size()));
        }
        return fileParts.get(0);
    }

    private ImportFileFormat getFormat(@NotNull String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex < 0 || lastDotIndex == fileName.length() - 1) {
            throw new IllegalArgumentException(
                    String.format("File does not have extension: %s", fileName)
            );
        }

        String ext = fileName.substring(lastDotIndex + 1).toUpperCase();

        try {
            return ImportFileFormat.valueOf(ext);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(
                    String.format("Unknown file format: %s (extension: %s)", fileName, ext)
            );
        }
    }

    private String getFileName(InputPart part) {
        MultivaluedMap<String, String> headers = part.getHeaders();
        String contentDisposition = headers.getFirst("Content-Disposition");

        if (contentDisposition == null) {
            throw new IllegalArgumentException("Missing 'Content-Disposition' header");
        }

        String fileNameParam = Arrays.stream(contentDisposition.split(";"))
                .map(String::trim)
                .filter(s -> s.contains("filename="))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Missing 'filename' param"));

        return fileNameParam
                .substring("filename=".length())
                .trim()
                .replace("\"", "");
    }
}
