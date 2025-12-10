package mapper;

import java.util.Optional;

import dto.discipline.DisciplineRequestDTO;
import dto.discipline.DisciplineResponseDTO;
import entity.Discipline;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import lombok.NoArgsConstructor;
import repository.DisciplineRepository;

@ApplicationScoped
@NoArgsConstructor
public class DisciplineMapper {
    private DisciplineRepository repository;

    @Inject
    public DisciplineMapper(DisciplineRepository repository) {
        this.repository = repository;
    }

    public DisciplineResponseDTO toDTO(Discipline entity) {
        return new DisciplineResponseDTO(entity.getId(), entity.getName(), entity.getPracticeHours());
    }

    public Discipline toEntity(DisciplineRequestDTO dto) {
        if (dto.getId() != null) {
            return repository.getByKey(dto.getId()).orElseThrow(EntityNotFoundException::new);
        }
        Discipline entity = new Discipline();
        entity.setName(dto.getName());
        entity.setPracticeHours(dto.getPracticeHours());
        Optional<Discipline> existing = repository.getIfExists(entity);
        if (existing.isPresent()) {
            return existing.get();
        }
        return entity;   
    }
}
