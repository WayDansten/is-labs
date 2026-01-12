package service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import dto.labwork.LabWorkRequestDTO;
import dto.labwork.LabWorkResponseDTO;
import dto.misc.DifficultyRequestDTO;
import dto.upload.UploadRequestDTO;
import entity.Coordinates;
import entity.Discipline;
import entity.LabWork;
import entity.Location;
import entity.Person;
import entity.Upload;
import entity.types.Difficulty;
import event.EventPublisher;
import exception.DifficultyException;
import exception.FileImportException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;
import mapper.LabWorkMapper;
import parser.JsonLabWorkParser;
import repository.LabWorkRepository;
import repository.DisciplineRepository;
import repository.CoordinatesRepository;
import repository.PersonRepository;
import repository.LocationRepository;
import websocket.WebSocketMessageType;

@ApplicationScoped
@NoArgsConstructor
public class LabWorkService {
    @Inject
    private LabWorkMapper mapper;
    @Inject
    private LabWorkRepository labWorkRepository;
    @Inject
    private DisciplineRepository disciplineRepository;
    @Inject
    private CoordinatesRepository coordinatesRepository;
    @Inject
    private PersonRepository personRepository;
    @Inject
    private LocationRepository locationRepository;
    @Inject
    private UploadService uploadService;
    @Inject
    private ChangeTrackerService trackerService;
    @Inject
    private EventPublisher eventPublisher;
    @Inject
    private JsonLabWorkParser parser;

    @Transactional
    public void add(LabWorkRequestDTO dto) {
        Set<WebSocketMessageType> changedTypes = trackerService.trackChanges(dto);
        LabWork entity = mapper.toEntity(dto);
        setExistingFields(entity);
        labWorkRepository.add(entity);
        eventPublisher.fireEvent(changedTypes);
    }

    @Transactional
    public void addBatch(UploadRequestDTO uploadDTO) {
        byte[] labworksAsBytes;
        try {
            labworksAsBytes = uploadDTO.getFileStream().readAllBytes();
        } catch (IOException e) {
            throw new FileImportException(e);
        }
        List<LabWorkRequestDTO> dtos = parser.parse(new ByteArrayInputStream(labworksAsBytes));
        Upload upload = uploadService.add(uploadDTO.getFileName(), labworksAsBytes);
        try {
            for (LabWorkRequestDTO dto : dtos) {
                add(dto);
            }
            uploadService.setUploadSuccess(upload.getId(), dtos.size());
        } finally {
            eventPublisher.fireEvent(Set.of(WebSocketMessageType.UPLOAD));
        }
    }

    @Transactional
    public void update(LabWorkRequestDTO dto) {
        Set<WebSocketMessageType> changedTypes = trackerService.trackChanges(dto);
        LabWork entity = mapper.toEntity(dto);
        setExistingFields(entity);
        labWorkRepository.update(entity);
        eventPublisher.fireEvent(changedTypes);
    }

    @Transactional
    public void lowerDifficulty(DifficultyRequestDTO dto) {
        Integer id = dto.getId();
        Integer steps = dto.getSteps();
        LabWork entity = labWorkRepository.getByKey(id).orElseThrow(EntityNotFoundException::new);
        if (entity.getDifficulty().getValue() - steps <= 0) {
            throw new DifficultyException();
        }
        Difficulty newDifficulty = Difficulty.getByValue(entity.getDifficulty().getValue() - steps);
        entity.setDifficulty(newDifficulty);
        labWorkRepository.update(entity);
        eventPublisher.fireEvent(Set.of(WebSocketMessageType.LABWORK));
    }

    @Transactional
    public void delete(Integer id) {
        labWorkRepository.deleteByKey(id);
        eventPublisher.fireEvent(Set.of(WebSocketMessageType.LABWORK));
    }

    @Transactional
    public void deleteByAuthor(String author) {
        List<LabWork> labWorks = labWorkRepository.getByAuthor(author);
        if (labWorks.isEmpty()) {
            throw new EntityNotFoundException();
        }
        labWorkRepository.delete(labWorks.get(0));
        eventPublisher.fireEvent(Set.of(WebSocketMessageType.LABWORK));
    }

    @Transactional
    public List<LabWorkResponseDTO> getAll() {
        return labWorkRepository.getAll().stream()
        .filter(Objects::nonNull)
        .map(mapper::toDTO)
        .toList();
    }

    @Transactional
    public Long countGreaterThanAveragePoint(Float averagePoint) {
        return labWorkRepository.countGreaterThanAveragePoint(averagePoint);
    }

    @Transactional
    public List<String> getByDescription(String prefix) {
        return labWorkRepository.getByDescription(prefix);
    }

    private void setExistingFields(LabWork entity) {
        entity.setDiscipline(getDisciplineIfExists(entity.getDiscipline()));
        entity.setCoordinates(getCoordinatesIfExists(entity.getCoordinates()));
        entity.getAuthor().setLocation(getLocationIfExists(entity.getAuthor().getLocation()));
        entity.setAuthor(getAuthorIfExists(entity.getAuthor()));
    }

    private Discipline getDisciplineIfExists(Discipline entity) {
        if (entity.getId() != null) {
            return disciplineRepository.getByKey(entity.getId()).orElseThrow(EntityNotFoundException::new);
        }
        Optional<Discipline> existing = disciplineRepository.get(entity);
        if (existing.isPresent()) {
            return existing.get();
        }
        return entity;
    }

    private Coordinates getCoordinatesIfExists(Coordinates entity) {
        if (entity.getId() != null) {
            return coordinatesRepository.getByKey(entity.getId()).orElseThrow(EntityNotFoundException::new);
        }
        Optional<Coordinates> existing = coordinatesRepository.get(entity);
        if (existing.isPresent()) {
            return existing.get();
        }
        return entity;
    }

    private Person getAuthorIfExists(Person entity) {
        if (entity.getId() != null) {
            return personRepository.getByKey(entity.getId()).orElseThrow(EntityNotFoundException::new);
        }
        Optional<Person> existing = personRepository.get(entity);
        if (existing.isPresent()) {
            return existing.get();
        }
        return entity;
    }

    private Location getLocationIfExists(Location entity) {
        if (entity.getId() != null) {
            return locationRepository.getByKey(entity.getId()).orElseThrow(EntityNotFoundException::new);
        }
        Optional<Location> existing = locationRepository.get(entity);
        if (existing.isPresent()) {
            return existing.get();
        }
        return entity;
    }
}
