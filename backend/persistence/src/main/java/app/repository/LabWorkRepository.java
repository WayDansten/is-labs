package app.repository;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import app.entity.LabWork;
import app.entity.Person;

@ApplicationScoped
public class LabWorkRepository extends AbstractRepository<LabWork, Integer> {
    @Inject
    public LabWorkRepository() {
        super(LabWork.class);
    }

    public List<LabWork> getByAuthor(String author) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<LabWork> cq = cb.createQuery(LabWork.class);
        Root<LabWork> root = cq.from(LabWork.class);
        Join<LabWork, Person> join = root.join("author");

        cq.where(cb.equal(join.get("name"), author));
        return em.createQuery(cq).getResultList();
    }

    public Long countGreaterThanAveragePoint(Float averagePoint) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<LabWork> root = cq.from(LabWork.class);

        cq.select(cb.count(root));
        cq.where(cb.greaterThan(root.get("averagePoint"), averagePoint));

        return em.createQuery(cq).getSingleResult();
    }

    public List<String> getByDescription(String prefix) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<String> cq = cb.createQuery(String.class);
        Root<LabWork> root = cq.from(LabWork.class);

        cq.select(root.get("name"));
        
        cq.where(cb.like(
            cb.lower(root.get("description")),
            prefix.toLowerCase().trim() + "%"
        ));

        cq.orderBy(cb.asc(root.get("name")));

        return em.createQuery(cq).getResultList();
    }
}
