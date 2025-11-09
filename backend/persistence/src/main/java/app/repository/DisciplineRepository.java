package app.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import app.entity.Discipline;

@ApplicationScoped
public class DisciplineRepository extends AbstractRepository<Discipline, Integer> {
    @Inject
    public DisciplineRepository() {
        super(Discipline.class);
    }
}
