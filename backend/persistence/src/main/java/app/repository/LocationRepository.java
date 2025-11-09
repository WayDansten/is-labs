package app.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import app.entity.Location;

@ApplicationScoped
public class LocationRepository extends AbstractRepository<Location, Integer> {
    @Inject
    public LocationRepository() {
        super(Location.class);
    }
}
