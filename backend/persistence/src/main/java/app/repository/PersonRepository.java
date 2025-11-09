package app.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import app.entity.Person;

@ApplicationScoped
public class PersonRepository extends AbstractRepository<Person, Integer> {
    @Inject
    public PersonRepository() {
        super(Person.class);
    }
}
