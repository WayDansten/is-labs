package repository;

import java.util.Optional;

import entity.Upload;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

@ApplicationScoped
public class UploadRepository extends AbstractRepository<Upload, Integer> {
    @Inject
    public UploadRepository() {
        super(Upload.class);
    }

    @Override
    public Optional<Upload> get(Upload entity) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Upload> cq = cb.createQuery(Upload.class);
        Root<Upload> root = cq.from(Upload.class);

        cq.select(root).where(
            cb.and(
                cb.equal(root.get("uuid"), entity.getUuid())
            ));

        try {
            Upload upload = em.createQuery(cq).getSingleResult();
            return Optional.of(upload);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}