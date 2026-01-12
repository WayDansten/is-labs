package repository;

import java.util.Optional;

import entity.Coordinates;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

@ApplicationScoped
public class CoordinatesRepository extends AbstractRepository<Coordinates, Integer> {
    @Inject
    public CoordinatesRepository() {
        super(Coordinates.class);
    }

    public Optional<Coordinates> get(Coordinates entity) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Coordinates> cq = cb.createQuery(Coordinates.class);
        Root<Coordinates> root = cq.from(Coordinates.class);

        cq.select(root).where(
            cb.and(
                cb.equal(root.get("x"), entity.getX()),
                cb.equal(root.get("y"), entity.getY())
            ));

        try {
            Coordinates coordinates = em.createQuery(cq).getSingleResult();
            return Optional.of(coordinates);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
