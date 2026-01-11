package config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import lombok.Getter;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.minio.MinioClient;

@Getter
@ApplicationScoped
public class MinIOConfig {

    @Inject
    @ConfigProperty(name = "minio.url", defaultValue = "http://localhost:9000")
    private String minioUrl;

    @Inject
    @ConfigProperty(name = "minio.accessKey", defaultValue = "minioadmin")
    private String accessKey;

    @Inject
    @ConfigProperty(name = "minio.secretKey", defaultValue = "minioadmin")
    private String secretKey;

    @Inject
    @ConfigProperty(name = "storage.importsBucket", defaultValue = "files")
    private String importsBucket;

    @Produces
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(minioUrl)
                .credentials(accessKey, secretKey)
                .build();
    }
}