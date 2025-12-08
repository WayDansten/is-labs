package mapper;

import dto.misc.UploadResponseDTO;
import entity.Upload;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.NoArgsConstructor;

@ApplicationScoped
@NoArgsConstructor
public class UploadMapper {
    public UploadResponseDTO toDTO(Upload entity) {
        return new UploadResponseDTO(entity.getId(), entity.getStatus(), entity.getObjectsAdded());
    }
}
