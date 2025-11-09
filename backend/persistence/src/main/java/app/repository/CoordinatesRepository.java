package app.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import app.entity.Coordinates;

@ApplicationScoped
public class CoordinatesRepository extends AbstractRepository<Coordinates, Integer> {
    @Inject
    public CoordinatesRepository() {
        super(Coordinates.class);
    }
}
