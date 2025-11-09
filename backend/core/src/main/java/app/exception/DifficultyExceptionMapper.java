package app.exception;

import app.dto.misc.StringResponseDTO;
import app.util.MessageConstants;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class DifficultyExceptionMapper implements ExceptionMapper<DifficultyException> {
    @Override
    public Response toResponse(DifficultyException exception) {
        return Response.status(Response.Status.BAD_REQUEST)
        .entity(new StringResponseDTO(MessageConstants.ERR_DIFFICULTY.getMessage()))
        .build();
    }
}
