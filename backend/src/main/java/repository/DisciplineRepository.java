package repository;

import java.util.Optional;

import entity.Discipline;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

@ApplicationScoped
public class DisciplineRepository extends AbstractRepository<Discipline, Integer> {
    @Inject
    public DisciplineRepository() {
        super(Discipline.class);
    }

    public Optional<Discipline> get(Discipline entity) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Discipline> cq = cb.createQuery(Discipline.class);
        Root<Discipline> root = cq.from(Discipline.class);

        cq.select(root).where(
            cb.and(
                cb.equal(root.get("name"), entity.getName()),
                cb.equal(root.get("practiceHours"), entity.getPracticeHours())
            ));

        try {
            Discipline discipline = em.createQuery(cq).getSingleResult();
            return Optional.of(discipline);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
