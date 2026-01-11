package service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

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
    private LabWorkMapper mapper;
    private LabWorkRepository labWorkRepository;
    private DisciplineRepository disciplineRepository;
    private CoordinatesRepository coordinatesRepository;
    private PersonRepository personRepository;
    private LocationRepository locationRepository;
    private UploadService uploadService;
    private ChangeTrackerService trackerService;
    private EventPublisher eventPublisher;
    private JsonLabWorkParser parser;

    @Inject
    public LabWorkService(
            LabWorkMapper mapper, LabWorkRepository labWorkRepository,
            DisciplineRepository disciplineRepository, CoordinatesRepository coordinatesRepository,
            PersonRepository personRepository, LocationRepository locationRepository,
            UploadService uploadService, ChangeTrackerService trackerService,
            EventPublisher eventPublisher, JsonLabWorkParser parser) {
        this.mapper = mapper;
        this.labWorkRepository = labWorkRepository;
        this.disciplineRepository = disciplineRepository;
        this.coordinatesRepository = coordinatesRepository;
        this.personRepository = personRepository;
        this.locationRepository = locationRepository;
        this.uploadService = uploadService;
        this.trackerService = trackerService;
        this.eventPublisher = eventPublisher;
        this.parser = parser;
    }

    @Transactional
    public void add(LabWorkRequestDTO dto) {
        Set<WebSocketMessageType> changedTypes = trackerService.trackChanges(dto);
        LabWork entity = mapper.toEntity(dto);
        setExistingFields(entity);
        labWorkRepository.add(entity);
        eventPublisher.fireEvent(changedTypes);
    }

    @Transactional
    public void addBatch(MultipartFormDataInput input) {
        UploadRequestDTO uploadDTO = uploadService.from(input);
        byte[] labworksAsBytes;
        try {
            labworksAsBytes = uploadDTO.getFileStream().readAllBytes();
        } catch (IOException e) {
            throw new FileImportException();
        }
        List<LabWorkRequestDTO> dtos = parser.parse(new ByteArrayInputStream(labworksAsBytes));
        Upload upload = uploadService.add(uploadDTO.getFileName(), labworksAsBytes);
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
        setExistingFields(entity);
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

    private void setExistingFields(LabWork entity) {
        Discipline discipline = entity.getDiscipline();
        Coordinates coordinates = entity.getCoordinates();
        Person author = entity.getAuthor();
        Location location = author.getLocation();
        entity.setDiscipline(getDisciplineIfExists(discipline));
        entity.setCoordinates(getCoordinatesIfExists(coordinates));
        author.setLocation(getLocationIfExists(location));
        entity.setAuthor(getAuthorIfExists(author));
    }

    private Discipline getDisciplineIfExists(Discipline entity) {
        if (entity.getId() != null) {
            return disciplineRepository.getByKey(entity.getId()).orElseThrow(EntityNotFoundException::new);
        }
        Optional<Discipline> existing = disciplineRepository.getIfExists(entity);
        if (existing.isPresent()) {
            return existing.get();
        }
        return entity;
    }

    private Coordinates getCoordinatesIfExists(Coordinates entity) {
        if (entity.getId() != null) {
            return coordinatesRepository.getByKey(entity.getId()).orElseThrow(EntityNotFoundException::new);
        }
        Optional<Coordinates> existing = coordinatesRepository.getIfExists(entity);
        if (existing.isPresent()) {
            return existing.get();
        }
        return entity;
    }

    private Person getAuthorIfExists(Person entity) {
        if (entity.getId() != null) {
            return personRepository.getByKey(entity.getId()).orElseThrow(EntityNotFoundException::new);
        }
        Optional<Person> existing = personRepository.getIfExists(entity);
        if (existing.isPresent()) {
            return existing.get();
        }
        return entity;
    }

    private Location getLocationIfExists(Location entity) {
        if (entity.getId() != null) {
            return locationRepository.getByKey(entity.getId()).orElseThrow(EntityNotFoundException::new);
        }
        Optional<Location> existing = locationRepository.getIfExists(entity);
        if (existing.isPresent()) {
            return existing.get();
        }
        return entity;
    }
}
