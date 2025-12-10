package mapper;

import java.util.Optional;

import dto.coordinates.CoordinatesRequestDTO;
import dto.coordinates.CoordinatesResponseDTO;
import entity.Coordinates;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import lombok.NoArgsConstructor;
import repository.CoordinatesRepository;

@ApplicationScoped
@NoArgsConstructor
public class CoordinatesMapper {
    private CoordinatesRepository repository;

    @Inject
    public CoordinatesMapper(CoordinatesRepository repository) {
        this.repository = repository;
    }

    public CoordinatesResponseDTO toDTO(Coordinates entity) {
        return new CoordinatesResponseDTO(entity.getId(), entity.getX(), entity.getY());
    }

    public Coordinates toEntity(CoordinatesRequestDTO dto) {
        if (dto.getId() != null) {
            return repository.getByKey(dto.getId()).orElseThrow(EntityNotFoundException::new);
        }
        Coordinates entity = new Coordinates();
        entity.setX(dto.getX());
        entity.setY(dto.getY());
        Optional<Coordinates> existing = repository.getIfExists(entity);
        if (existing.isPresent()) {
            return existing.get();
        }
        return entity;
    }
}
