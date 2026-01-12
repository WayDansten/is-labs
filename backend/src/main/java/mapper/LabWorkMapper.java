package mapper;

import dto.labwork.LabWorkRequestDTO;
import dto.labwork.LabWorkResponseDTO;
import entity.LabWork;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import lombok.NoArgsConstructor;
import repository.LabWorkRepository;

@ApplicationScoped
@NoArgsConstructor
public class LabWorkMapper {
    @Inject
    private LabWorkRepository repository;
    @Inject
    private PersonMapper personMapper;
    @Inject
    private DisciplineMapper disciplineMapper;
    @Inject
    private CoordinatesMapper coordinatesMapper;

    public LabWorkResponseDTO toDTO(LabWork entity) {
        return new LabWorkResponseDTO(
                entity.getId(),
                entity.getName(),
                coordinatesMapper.toDTO(entity.getCoordinates()),
                entity.getCreationDate(),
                entity.getDescription(),
                entity.getDifficulty(),
                disciplineMapper.toDTO(entity.getDiscipline()),
                entity.getMinimalPoint(),
                entity.getAveragePoint(),
                personMapper.toDTO(entity.getAuthor()));
    }

    public LabWork toEntity(LabWorkRequestDTO dto) {
        LabWork entity = new LabWork();
        if (dto.getId() != null) {
            entity = repository.getByKey(dto.getId()).orElseThrow(EntityNotFoundException::new);
        }
        entity.setName(dto.getName());
        entity.setCoordinates(coordinatesMapper.toEntity(dto.getCoordinates()));
        entity.setDescription(dto.getDescription());
        entity.setDifficulty(dto.getDifficulty());
        entity.setDiscipline(disciplineMapper.toEntity(dto.getDiscipline()));
        entity.setMinimalPoint(dto.getMinimalPoint());
        entity.setAveragePoint(dto.getAveragePoint());
        entity.setAuthor(personMapper.toEntity(dto.getAuthor()));
        return entity;
    }
}
