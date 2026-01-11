package service;

import java.io.ByteArrayInputStream;

import config.MinIOConfig;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class MinIOService {

    @Inject
    private MinIOConfig config;

    @Inject
    private MinioClient client;

    public void save(String objectKey, byte[] content) throws Exception {
        client.putObject(
                PutObjectArgs.builder()
                        .bucket("files")
                        .object(objectKey)
                        .stream(new ByteArrayInputStream(content), -1, 10 * 1024 * 1024)
                        .contentType("application/octet-stream")
                        .build()
        );
    }

    public byte[] get(String objectKey) throws Exception {
        return client.getObject(
                GetObjectArgs.builder()
                        .bucket("files")
                        .object(objectKey)
                        .build()
        ).readAllBytes();
    }
}
