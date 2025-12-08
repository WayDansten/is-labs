package service;

import java.util.List;

import dto.misc.UploadResponseDTO;
import entity.Upload;
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
    private UploadMapper mapper;
    private UploadRepository repository;

    @Inject
    public UploadService(UploadMapper mapper, UploadRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public Upload add() {
        Upload upload = new Upload();
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
}
