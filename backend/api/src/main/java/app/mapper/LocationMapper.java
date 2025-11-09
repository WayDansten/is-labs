package app.mapper;

import app.dto.location.LocationRequestDTO;
import app.dto.location.LocationResponseDTO;
import jakarta.enterprise.context.ApplicationScoped;
import app.entity.Location;

@ApplicationScoped
public class LocationMapper {
    public LocationResponseDTO toDTO(Location entity) {
        return new LocationResponseDTO(entity.getId(), entity.getName(), entity.getX(), entity.getY(), entity.getZ());
    }

    public Location toEntity(LocationRequestDTO dto) {
        Location entity = new Location();
        if (dto.getId() != null) {
            entity.setId(dto.getId());
        }
        entity.setName(dto.getName());
        entity.setX(dto.getX());
        entity.setY(dto.getY());
        entity.setZ(dto.getZ());
        return entity;
    }
}
