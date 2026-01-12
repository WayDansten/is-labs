package mapper;

import dto.location.LocationRequestDTO;
import dto.location.LocationResponseDTO;
import entity.Location;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.NoArgsConstructor;

@ApplicationScoped
@NoArgsConstructor
public class LocationMapper {
    public LocationResponseDTO toDTO(Location entity) {
        return new LocationResponseDTO(entity.getId(), entity.getName(), entity.getX(), entity.getY(), entity.getZ());
    }

    public Location toEntity(LocationRequestDTO dto) {
        Location entity = new Location();
        entity.setName(dto.getName());
        entity.setX(dto.getX());
        entity.setY(dto.getY());
        entity.setZ(dto.getZ());
        return entity;
    }
}
