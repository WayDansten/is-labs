package config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import lombok.Getter;

import java.net.URI;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.minio.MinioClient;

@Getter
@ApplicationScoped
public class MinIOConfig {

    @Inject
    @ConfigProperty(name = "minio.url")
    private URI minioUrl;

    @Inject
    @ConfigProperty(name = "minio.accessKey")
    private String accessKey;

    @Inject
    @ConfigProperty(name = "minio.secretKey")
    private String secretKey;

    @Inject
    @ConfigProperty(name = "storage.importsBucket")
    private String importsBucket;

    @Produces
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(minioUrl.toString())
                .credentials(accessKey, secretKey)
                .build();
    }
}