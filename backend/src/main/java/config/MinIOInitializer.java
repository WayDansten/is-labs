package config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
@Startup
public class MinIOInitializer {

    @Inject
    MinioClient minioClient;

    @Inject
    MinIOConfig minioConfig;

    @PostConstruct
    public void init() {
        String bucketName = minioConfig.getImportsBucket();
        try {
            boolean exists = minioClient.bucketExists(
                    BucketExistsArgs.builder()
                            .bucket(bucketName)
                            .build()
            );

            if (!exists) {
                log.info("Bucket '{}' not found. Creating...", bucketName);
                minioClient.makeBucket(
                        MakeBucketArgs.builder()
                                .bucket(bucketName)
                                .build()
                );
                log.info("Bucket '{}' created", bucketName);
            } else {
                log.info("Bucket '{}' already exists", bucketName);
            }
        } catch (Exception e) {
            throw new RuntimeException(String.format("Error while initializing MinIO bucket '%s'", bucketName));
        }
    }

}