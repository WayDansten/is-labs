package app.service;

import java.util.List;

import app.mapper.LocationMapper;
import app.dto.location.LocationResponseDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;
import app.repository.LocationRepository;

@ApplicationScoped
@NoArgsConstructor
public class LocationService {
    private LocationMapper mapper;
    private LocationRepository repository;

    @Inject
    public LocationService(LocationMapper mapper, LocationRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    @Transactional
    public List<LocationResponseDTO> getAll() {
        return repository.getAll().stream().map(mapper::toDTO).toList();
    }

}
