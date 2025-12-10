package mapper;

import java.util.Optional;

import dto.person.PersonRequestDTO;
import dto.person.PersonResponseDTO;
import entity.Person;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import lombok.NoArgsConstructor;
import repository.PersonRepository;

@ApplicationScoped
@NoArgsConstructor
public class PersonMapper {
    private PersonRepository repository;
    private LocationMapper locationMapper;

    @Inject
    public PersonMapper(PersonRepository repository, LocationMapper locationMapper) {
        this.repository = repository;
        this.locationMapper = locationMapper;
    }

    public PersonResponseDTO toDTO(Person entity) {
        return new PersonResponseDTO(entity.getId(), entity.getName(), entity.getEyeColor(), entity.getHairColor(),
                locationMapper.toDTO(entity.getLocation()), entity.getBirthday(), entity.getNationality());
    }

    public Person toEntity(PersonRequestDTO dto) {
        if (dto.getId() != null) {
            return repository.getByKey(dto.getId()).orElseThrow(EntityNotFoundException::new);
        }
        Person entity = new Person();
        entity.setName(dto.getName());
        entity.setEyeColor(dto.getEyeColor());
        entity.setHairColor(dto.getHairColor());
        entity.setLocation(locationMapper.toEntity(dto.getLocation()));
        entity.setBirthday(dto.getBirthday());
        entity.setNationality(dto.getNationality());
        Optional<Person> existing = repository.getIfExists(entity);
        if (existing.isPresent()) {
            return existing.get();
        }
        return entity;
    }
}
