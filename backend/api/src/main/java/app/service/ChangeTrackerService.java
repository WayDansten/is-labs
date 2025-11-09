package app.service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import app.dto.coordinates.CoordinatesRequestDTO;
import app.dto.discipline.DisciplineRequestDTO;
import app.dto.labwork.LabWorkRequestDTO;
import app.dto.location.LocationRequestDTO;
import app.dto.person.PersonRequestDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import lombok.NoArgsConstructor;
import app.entity.Coordinates;
import app.entity.Discipline;
import app.entity.Location;
import app.entity.Person;
import app.repository.CoordinatesRepository;
import app.repository.DisciplineRepository;
import app.repository.LocationRepository;
import app.repository.PersonRepository;
import app.websocket.WebSocketMessageType;

@ApplicationScoped
@NoArgsConstructor
public class ChangeTrackerService {
    private CoordinatesRepository coordinatesRepository;
    private DisciplineRepository disciplineRepository;
    private PersonRepository personRepository;
    private LocationRepository locationRepository;

    @Inject
    public ChangeTrackerService(
            CoordinatesRepository coordinatesRepository,
            DisciplineRepository disciplineRepository,
            PersonRepository personRepository,
            LocationRepository locationRepository) {
        this.coordinatesRepository = coordinatesRepository;
        this.disciplineRepository = disciplineRepository;
        this.personRepository = personRepository;
        this.locationRepository = locationRepository;
    }

    public Set<WebSocketMessageType> trackChanges(LabWorkRequestDTO labWorkDTO) {
        Set<WebSocketMessageType> changedTypes = new HashSet<>();
        changedTypes.add(WebSocketMessageType.LABWORK);

        if (labWorkDTO.getCoordinates().getId() == null) {
            changedTypes.add(WebSocketMessageType.COORDINATES);
        } else {
            CoordinatesRequestDTO dto = labWorkDTO.getCoordinates();
            Coordinates entity = coordinatesRepository.getByKey(dto.getId()).orElseThrow(() -> new EntityNotFoundException());
            if (!(Objects.equals(dto.getX(), entity.getX())
                    && Objects.equals(dto.getY(), entity.getY()))) {
                changedTypes.add(WebSocketMessageType.COORDINATES);
            }
        }

        if (labWorkDTO.getDiscipline().getId() == null) {
            changedTypes.add(WebSocketMessageType.DISCIPLINE);
        } else {
            DisciplineRequestDTO dto = labWorkDTO.getDiscipline();
            Discipline entity = disciplineRepository.getByKey(dto.getId()).orElseThrow(() -> new EntityNotFoundException());
            if (!(Objects.equals(dto.getName(), entity.getName())
                    && Objects.equals(dto.getPracticeHours(), entity.getPracticeHours()))) {
                changedTypes.add(WebSocketMessageType.DISCIPLINE);
            }
        }

        if (labWorkDTO.getAuthor().getId() == null) {
            changedTypes.add(WebSocketMessageType.PERSON);
        } else {
            PersonRequestDTO dto = labWorkDTO.getAuthor();
            Person entity = personRepository.getByKey(dto.getId()).orElseThrow(() -> new EntityNotFoundException());
            if (!(Objects.equals(dto.getName(), entity.getName())
                    && Objects.equals(dto.getBirthday(), entity.getBirthday())
                    && Objects.equals(dto.getEyeColor(), entity.getEyeColor())
                    && Objects.equals(dto.getHairColor(), entity.getHairColor())
                    && Objects.equals(dto.getNationality(), entity.getNationality())
                    && Objects.equals(dto.getLocation().getId(), entity.getLocation().getId()))) {
                changedTypes.add(WebSocketMessageType.PERSON);
            }
        }

        if (labWorkDTO.getAuthor().getLocation().getId() == null) {
            changedTypes.add(WebSocketMessageType.LOCATION);
            changedTypes.add(WebSocketMessageType.PERSON);
        } else {
            LocationRequestDTO dto = labWorkDTO.getAuthor().getLocation();
            Location entity = locationRepository.getByKey(dto.getId()).orElseThrow(() -> new EntityNotFoundException());
            if (!(Objects.equals(dto.getName(), entity.getName())
                    && Objects.equals(dto.getX(), entity.getX())
                    && Objects.equals(dto.getY(), entity.getY())
                    && Objects.equals(dto.getZ(), entity.getZ()))) {
                changedTypes.add(WebSocketMessageType.LOCATION);
                changedTypes.add(WebSocketMessageType.PERSON);
            }
        }

        return changedTypes;
    }
}
