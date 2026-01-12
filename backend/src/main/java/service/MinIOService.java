package service;

import java.io.ByteArrayInputStream;

import config.MinIOConfig;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;

@ApplicationScoped
public class MinIOService {
    private static final long PART_SIZE = 10 * 1024 * (long) 1024;

    @Inject
    private MinIOConfig config;

    @Inject
    private MinioClient client;

    public void save(String objectKey, byte[] content) throws Exception {
        client.putObject(
                PutObjectArgs.builder()
                        .bucket(config.getImportsBucket())
                        .object(objectKey)
                        .stream(new ByteArrayInputStream(content), -1, PART_SIZE)
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .build()
        );
    }

    public byte[] get(String objectKey) throws Exception {
        return client.getObject(
                GetObjectArgs.builder()
                        .bucket(config.getImportsBucket())
                        .object(objectKey)
                        .build()
        ).readAllBytes();
    }
}
