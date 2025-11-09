package app.mapper;

import app.dto.coordinates.CoordinatesRequestDTO;
import app.dto.coordinates.CoordinatesResponseDTO;
import jakarta.enterprise.context.ApplicationScoped;
import app.entity.Coordinates;

@ApplicationScoped
public class CoordinatesMapper {
    public CoordinatesResponseDTO toDTO(Coordinates entity) {
        return new CoordinatesResponseDTO(entity.getId(), entity.getX(), entity.getY());
    }

    public Coordinates toEntity(CoordinatesRequestDTO dto) {
        Coordinates entity = new Coordinates();
        if (dto.getId() != null) {
            entity.setId(dto.getId());
        }
        entity.setX(dto.getX());
        entity.setY(dto.getY());
        return entity;
    }
}
