package repository;

import entity.Upload;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UploadRepository extends AbstractRepository<Upload, Integer> {
    @Inject
    public UploadRepository() {
        super(Upload.class);
    }
}