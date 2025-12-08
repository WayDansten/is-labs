package mapper;

import java.util.Optional;

import dto.location.LocationRequestDTO;
import dto.location.LocationResponseDTO;
import entity.Location;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import lombok.NoArgsConstructor;
import repository.LocationRepository;

@ApplicationScoped
@NoArgsConstructor
public class LocationMapper {
    private LocationRepository repository;

    @Inject
    public LocationMapper(LocationRepository repository) {
        this.repository = repository;
    }

    public LocationResponseDTO toDTO(Location entity) {
        return new LocationResponseDTO(entity.getId(), entity.getName(), entity.getX(), entity.getY(), entity.getZ());
    }

    public Location toEntity(LocationRequestDTO dto) {
        if (dto.getId() != null) {
            return repository.getByKey(dto.getId()).orElseThrow(EntityNotFoundException::new);
        } else {
            Location entity = new Location();
            entity.setName(dto.getName());
            entity.setX(dto.getX());
            entity.setY(dto.getY());
            entity.setZ(dto.getZ());
            Optional<Location> existing = repository.getIfExists(entity);
            if (existing.isPresent()) {
                return existing.get();
            }
            return entity;
        }
    }
}
