package mapper;

import dto.upload.UploadResponseDTO;
import entity.Upload;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.NoArgsConstructor;

@ApplicationScoped
@NoArgsConstructor
public class UploadMapper {
    public UploadResponseDTO toDTO(Upload entity) {
        return new UploadResponseDTO(entity.getId(), entity.getStatus(), entity.getAddedObjectsCount());
    }
}
