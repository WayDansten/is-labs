package repository;

import java.util.Optional;

import entity.Person;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

@ApplicationScoped
public class PersonRepository extends AbstractRepository<Person, Integer> {
    @Inject
    public PersonRepository() {
        super(Person.class);
    }

    public Optional<Person> get(Person entity) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Person> cq = cb.createQuery(Person.class);
        Root<Person> root = cq.from(Person.class);

        cq.select(root).where(
            cb.and(
                cb.equal(root.get("name"), entity.getName()),
                cb.equal(root.get("birthday"), entity.getBirthday())
            ));

        try {
            Person person = em.createQuery(cq).getSingleResult();
            return Optional.of(person);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
