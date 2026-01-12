package exception;

import dto.string.StringResponseDTO;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import util.MessageConstants;

public class FileImportExceptionMapper implements ExceptionMapper<FileImportException>{
    @Override
    public Response toResponse(FileImportException exception) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
        .entity(new StringResponseDTO(MessageConstants.ERR_NOT_FOUND.getMessage()))
        .build();
    }
}
