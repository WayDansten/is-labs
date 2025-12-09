package service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import dto.labwork.LabWorkRequestDTO;
import dto.labwork.LabWorkResponseDTO;
import dto.misc.DifficultyRequestDTO;
import entity.LabWork;
import entity.Upload;
import entity.types.Difficulty;
import event.EventPublisher;
import exception.DifficultyException;
import jakarta.ejb.TransactionAttribute;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;
import mapper.LabWorkMapper;
import repository.LabWorkRepository;
import websocket.WebSocketMessageType;

@ApplicationScoped
@NoArgsConstructor
public class LabWorkService {
    private LabWorkMapper mapper;
    private LabWorkRepository labWorkRepository;
    private UploadService uploadService;
    private ChangeTrackerService trackerService;
    private EventPublisher eventPublisher;

    @Inject
    public LabWorkService(
            LabWorkMapper mapper, LabWorkRepository labWorkRepository,
            UploadService uploadService, ChangeTrackerService trackerService,
            EventPublisher eventPublisher) {
        this.mapper = mapper;
        this.labWorkRepository = labWorkRepository;
        this.uploadService = uploadService;
        this.trackerService = trackerService;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public void add(LabWorkRequestDTO dto) {
        Set<WebSocketMessageType> changedTypes = trackerService.trackChanges(dto);
        LabWork entity = mapper.toEntity(dto);
        labWorkRepository.add(entity);
        eventPublisher.fireEvent(changedTypes);
    }

    @Transactional
    public void addBatch(List<LabWorkRequestDTO> dtos) {
        Upload upload = uploadService.add();
        try {
            for (LabWorkRequestDTO dto : dtos) {
                add(dto);
            }
            uploadService.setUploadSuccess(upload.getId(), dtos.size());
        } finally {
            Set<WebSocketMessageType> changedTypes = new HashSet<>();
            changedTypes.add(WebSocketMessageType.UPLOAD);
            eventPublisher.fireEvent(changedTypes);
        }
    }

    @Transactional
    public void update(LabWorkRequestDTO dto) {
        Set<WebSocketMessageType> changedTypes = trackerService.trackChanges(dto);
        LabWork entity = mapper.toEntity(dto);
        labWorkRepository.update(entity);
        eventPublisher.fireEvent(changedTypes);
    }

    @Transactional
    public void lowerDifficulty(DifficultyRequestDTO dto) {
        Integer id = dto.getId();
        Integer steps = dto.getSteps();
        Set<WebSocketMessageType> changedTypes = new HashSet<>();
        changedTypes.add(WebSocketMessageType.LABWORK);
        LabWork entity = labWorkRepository.getByKey(id).orElseThrow(EntityNotFoundException::new);
        if (entity.getDifficulty().getValue() - steps <= 0) {
            throw new DifficultyException();
        }
        Difficulty newDifficulty = Difficulty.getByValue(entity.getDifficulty().getValue() - steps);
        entity.setDifficulty(newDifficulty);
        labWorkRepository.update(entity);
        eventPublisher.fireEvent(changedTypes);
    }

    @Transactional
    public void delete(Integer id) {
        Set<WebSocketMessageType> changedTypes = new HashSet<>();
        changedTypes.add(WebSocketMessageType.LABWORK);
        labWorkRepository.deleteByKey(id);
        eventPublisher.fireEvent(changedTypes);
    }

    @Transactional
    public void deleteByAuthor(String author) {
        Set<WebSocketMessageType> changedTypes = new HashSet<>();
        changedTypes.add(WebSocketMessageType.LABWORK);
        List<LabWork> labWorks = labWorkRepository.getByAuthor(author);
        if (labWorks.isEmpty()) {
            throw new EntityNotFoundException();
        }
        labWorkRepository.delete(labWorks.get(0));
        eventPublisher.fireEvent(changedTypes);
    }

    @Transactional
    public List<LabWorkResponseDTO> getAll() {
        return labWorkRepository.getAll().stream().filter(Objects::nonNull).map(mapper::toDTO).toList();
    }

    @Transactional
    public Long countGreaterThanAveragePoint(Float averagePoint) {
        return labWorkRepository.countGreaterThanAveragePoint(averagePoint);
    }

    @Transactional
    public List<String> getByDescription(String prefix) {
        return labWorkRepository.getByDescription(prefix);
    }
}
