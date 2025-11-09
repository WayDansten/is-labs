package app.service;

import java.util.List;

import app.mapper.CoordinatesMapper;
import app.dto.coordinates.CoordinatesResponseDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;
import app.repository.CoordinatesRepository;

@ApplicationScoped
@NoArgsConstructor
public class CoordinatesService {
    private CoordinatesMapper mapper;
    private CoordinatesRepository repository;

    @Inject
    public CoordinatesService(CoordinatesMapper mapper, CoordinatesRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    @Transactional
    public List<CoordinatesResponseDTO> getAll() {
        return repository.getAll().stream().map(mapper::toDTO).toList();
    }
}
