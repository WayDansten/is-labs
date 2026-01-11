package service;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import dto.download.DownloadResponseDTO;
import dto.upload.UploadRequestDTO;
import dto.upload.UploadResponseDTO;
import entity.Upload;
import exception.FileImportException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.MultivaluedMap;
import lombok.NoArgsConstructor;
import mapper.UploadMapper;
import repository.UploadRepository;
import util.ImportFileFormat;

@ApplicationScoped
@NoArgsConstructor
public class UploadService {
    private UploadMapper mapper;
    private UploadRepository repository;
    private MinIOService minIOService;

    @Inject
    public UploadService(UploadMapper mapper, UploadRepository repository, MinIOService minIOService) {
        this.mapper = mapper;
        this.repository = repository;
        this.minIOService = minIOService;
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public Upload add(String fileName, byte[] content) {
        String key = UUID.randomUUID() + "_" + fileName;
        try {
            minIOService.save(key, content);
        } catch (Exception e) {
            throw new FileImportException();
        }
        Upload upload = new Upload();
        upload.setUuid(key);
        upload.setFileName(fileName);
        upload.setStatus(false);
        return repository.add(upload);
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void setUploadSuccess(int id, int objectsAdded) {
        Upload upload = repository.getByKey(id).orElseThrow(EntityNotFoundException::new);
        upload.setStatus(true);
        upload.setObjectsAdded(objectsAdded);
        repository.update(upload);
    }

    @Transactional
    public List<UploadResponseDTO> getAll() {
        return repository.getAll().stream().map(mapper::toDTO).toList();
    }

    public DownloadResponseDTO download(Integer id) throws Exception {
        Upload upload = repository.getByKey(id).orElseThrow(EntityNotFoundException::new);
        byte[] content = minIOService.get(upload.getUuid());
        String fileName = upload.getFileName();
        return new DownloadResponseDTO(fileName, content);
    }

    public UploadRequestDTO from(MultipartFormDataInput input) {
        InputPart filePart = getInputPart(input);
        String fileName = getFileName(filePart);
        ImportFileFormat format = getFormat(fileName);
        InputStream fileStream = getInputStream(filePart);

        UploadRequestDTO dto = new UploadRequestDTO();
        dto.setFileName(fileName);
        dto.setFileStream(fileStream);
        dto.setFormat(format);

        return dto;
    }

    private static InputStream getInputStream(InputPart filePart) {
        InputStream fileStream;
        try {
            fileStream = filePart.getBody(InputStream.class, null);
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not extract file input stream", e);
        }
        return fileStream;
    }

    private InputPart getInputPart(MultipartFormDataInput input) {
        Map<String, List<InputPart>> formParts = input.getFormDataMap();

        List<InputPart> fileParts = formParts.get("file");
        if (fileParts == null || fileParts.isEmpty()) {
            throw new IllegalArgumentException("File was not provided (expected form-data param 'file')");
        }
        if (fileParts.size() > 1) {
            throw new IllegalArgumentException(String.format("Expected only one file, got: %d", fileParts.size()));
        }
        return fileParts.get(0);
    }

    private ImportFileFormat getFormat(@NotNull String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex < 0 || lastDotIndex == fileName.length() - 1) {
            throw new IllegalArgumentException(
                    String.format("File does not have extension: %s", fileName)
            );
        }

        String ext = fileName.substring(lastDotIndex + 1).toUpperCase();

        try {
            return ImportFileFormat.valueOf(ext);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(
                    String.format("Unknown file format: %s (extension: %s)", fileName, ext)
            );
        }
    }

    private String getFileName(InputPart part) {
        MultivaluedMap<String, String> headers = part.getHeaders();
        String contentDisposition = headers.getFirst("Content-Disposition");

        if (contentDisposition == null) {
            throw new IllegalArgumentException("Missing 'Content-Disposition' header");
        }

        String fileNameParam = Arrays.stream(contentDisposition.split(";"))
                .map(String::trim)
                .filter(s -> s.contains("filename="))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Missing 'filename' param"));

        return fileNameParam
                .substring("filename=".length())
                .trim()
                .replace("\"", "");
    }
}
