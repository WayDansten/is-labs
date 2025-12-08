package repository;

import java.util.Optional;

import entity.Location;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

@ApplicationScoped
public class LocationRepository extends AbstractRepository<Location, Integer> {
    @Inject
    public LocationRepository() {
        super(Location.class);
    }

    public Optional<Location> getIfExists(Location entity) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Location> cq = cb.createQuery(Location.class);
        Root<Location> root = cq.from(Location.class);

        cq.select(root).where(
            cb.and(
                cb.equal(root.get("x"), entity.getX()),
                cb.equal(root.get("y"), entity.getY()),
                cb.equal(root.get("z"), entity.getY())
            ));

        try {
            Location location = em.createQuery(cq).getSingleResult();
            return Optional.of(location);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
