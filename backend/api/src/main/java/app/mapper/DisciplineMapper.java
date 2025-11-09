package app.mapper;

import app.dto.discipline.DisciplineRequestDTO;
import app.dto.discipline.DisciplineResponseDTO;
import jakarta.enterprise.context.ApplicationScoped;
import app.entity.Discipline;

@ApplicationScoped
public class DisciplineMapper {
    public DisciplineResponseDTO toDTO(Discipline entity) {
        return new DisciplineResponseDTO(entity.getId(), entity.getName(), entity.getPracticeHours());
    }

    public Discipline toEntity(DisciplineRequestDTO dto) {
        Discipline entity = new Discipline();
        if (dto.getId() != null) {
            entity.setId(dto.getId());
        }
        entity.setName(dto.getName());
        entity.setPracticeHours(dto.getPracticeHours());
        return entity;
    }
}
