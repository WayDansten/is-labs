package app.service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import app.mapper.LabWorkMapper;
import app.dto.labwork.LabWorkRequestDTO;
import app.dto.labwork.LabWorkResponseDTO;
import app.dto.misc.DifficultyRequestDTO;
import app.event.EventPublisher;
import app.exception.DifficultyException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;
import app.entity.LabWork;
import app.entity.types.Difficulty;
import app.repository.LabWorkRepository;
import app.websocket.WebSocketMessageType;

@ApplicationScoped
@NoArgsConstructor
public class LabWorkService {
    private LabWorkMapper mapper;
    private LabWorkRepository repository;
    private ChangeTrackerService trackerService;
    private EventPublisher eventPublisher;

    @Inject
    public LabWorkService(LabWorkMapper mapper, LabWorkRepository repository, ChangeTrackerService trackerService, EventPublisher eventPublisher) {
        this.mapper = mapper;
        this.repository = repository;
        this.trackerService = trackerService;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public void add(LabWorkRequestDTO dto) {
        Set<WebSocketMessageType> changedTypes = trackerService.trackChanges(dto);
        LabWork entity = mapper.toEntity(dto);
        repository.add(entity);
        eventPublisher.fireEvent(changedTypes);
    }

    @Transactional
    public void update(LabWorkRequestDTO dto) {
        Set<WebSocketMessageType> changedTypes = trackerService.trackChanges(dto);
        LabWork entity = mapper.toEntity(dto);
        repository.update(entity);
        eventPublisher.fireEvent(changedTypes);
    }

    @Transactional
    public void lowerDifficulty(DifficultyRequestDTO dto) {
        Integer id = dto.getId();
        Integer steps = dto.getSteps();
        Set<WebSocketMessageType> changedTypes = new HashSet<>();
        changedTypes.add(WebSocketMessageType.LABWORK);
        LabWork entity = repository.getByKey(id).orElseThrow(() -> new EntityNotFoundException());
        if (entity.getDifficulty().getValue() - steps <= 0) {
            throw new DifficultyException();
        }
        Difficulty newDifficulty = Difficulty.getByValue(entity.getDifficulty().getValue() - steps);
        entity.setDifficulty(newDifficulty);
        repository.update(entity);
        eventPublisher.fireEvent(changedTypes);
    }

    @Transactional
    public void delete(Integer id) {
        Set<WebSocketMessageType> changedTypes = new HashSet<>();
        changedTypes.add(WebSocketMessageType.LABWORK);
        repository.deleteByKey(id);
        eventPublisher.fireEvent(changedTypes);
    }

    @Transactional
    public void deleteByAuthor(String author) {
        Set<WebSocketMessageType> changedTypes = new HashSet<>();
        changedTypes.add(WebSocketMessageType.LABWORK);
        List<LabWork> labWorks = repository.getByAuthor(author);
        if (labWorks.isEmpty()) {
            throw new EntityNotFoundException();
        }
        repository.delete(labWorks.get(0));
        eventPublisher.fireEvent(changedTypes);
    }

    @Transactional
    public List<LabWorkResponseDTO> getAll() {
        return repository.getAll().stream().filter(Objects::nonNull).map(mapper::toDTO).toList();
    }

    @Transactional
    public Long countGreaterThanAveragePoint(Float averagePoint) {
        return repository.countGreaterThanAveragePoint(averagePoint);
    }

    @Transactional
    public List<String> getByDescription(String prefix) {
        return repository.getByDescription(prefix);
    }
}
