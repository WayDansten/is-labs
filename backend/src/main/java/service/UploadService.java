package service;

import java.util.List;
import java.util.UUID;

import dto.download.DownloadResponseDTO;
import dto.upload.UploadResponseDTO;
import entity.Upload;
import exception.FileImportException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;
import mapper.UploadMapper;
import repository.UploadRepository;

@ApplicationScoped
@NoArgsConstructor
public class UploadService {
    @Inject
    private UploadMapper mapper;
    @Inject
    private UploadRepository repository;
    @Inject
    private MinIOService minIOService;

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public Upload add(String fileName, byte[] content) {
        String key = UUID.randomUUID() + "_" + fileName;
        try {
            minIOService.save(key, content);
        } catch (Exception e) {
            throw new FileImportException();
        }
        Upload upload = new Upload();
        upload.setUuid(key);
        upload.setFileName(fileName);
        upload.setStatus(false);
        return repository.add(upload);
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void setUploadSuccess(int id, int objectsAdded) {
        Upload upload = repository.getByKey(id).orElseThrow(EntityNotFoundException::new);
        upload.setStatus(true);
        upload.setObjectsAdded(objectsAdded);
        repository.update(upload);
    }

    @Transactional
    public List<UploadResponseDTO> getAll() {
        return repository.getAll().stream().map(mapper::toDTO).toList();
    }

    public DownloadResponseDTO download(Integer id) throws Exception {
        Upload upload = repository.getByKey(id).orElseThrow(EntityNotFoundException::new);
        byte[] content = minIOService.get(upload.getUuid());
        String fileName = upload.getFileName();
        return new DownloadResponseDTO(fileName, content);
    }
}
