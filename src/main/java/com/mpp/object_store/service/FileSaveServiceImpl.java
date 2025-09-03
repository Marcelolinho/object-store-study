package com.mpp.object_store.service;

import com.mpp.object_store.dto.FileDto;
import com.mpp.object_store.mappers.FileMapper;
import com.mpp.object_store.model.FileEntity;
import com.mpp.object_store.repository.FileRepository;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
import io.minio.http.Method;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service("fileSaveService")
public class FileSaveServiceImpl implements IFileSaveService{

    private final MinioClient minioClient;
    private final FileRepository fileRepository;
    private final Logger log = LoggerFactory.getLogger(FileSaveServiceImpl.class);

    public FileSaveServiceImpl(MinioClient minioClient, FileRepository fileRepository) {
        this.minioClient = minioClient;
        this.fileRepository = fileRepository;
    }

    @Override
    public FileDto saveFile(MultipartFile file, String name) {
        try {

            InputStream fileStream = file.getInputStream();

            PutObjectArgs.Builder builder =  PutObjectArgs.builder()
                    .stream(fileStream, file.getSize(), -1)
                    .bucket("files") // TODO: Chamar dinamicamente o bucket
                    .object(name)
                    .contentType(file.getContentType());

            minioClient.putObject(builder.build());

            String url = createUrl("files", name);

            FileEntity fileEntity = FileEntity.builder()
                    .url(url)
                    .objectKey(name)
                    .bucket("files")
                    .sizeBytes(file.getSize())
                    .build();

            fileRepository.save(fileEntity);
            log.info("File saved {}", name);

            return FileMapper.toDto(fileEntity);
        } catch (Exception e) {
            log.error("An error has ocurred on saving the file: {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public FileDto getFileByName(String objectKey) {

        try {
            Optional<FileEntity> file = fileRepository.findByObjectKey(objectKey);

            if (file.isEmpty()) { // Done this way to log on console TODO: Logar no DataDog ou Prometheus
                log.error("Entity not found by Object Key: {}",objectKey);
                throw new EntityNotFoundException("Entity not found by object key");
            }

            String url = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .expiry(5, TimeUnit.HOURS)
                            .method(Method.GET)
                            .bucket("files")
                            .object(objectKey)
                            .build()
            );

            return new FileDto(url, objectKey);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void deleteFileByName(String fileName) {

    }

    @Override
    public void deleteFileById(UUID id) {

    }

    @Override
    public FileDto getFileById(UUID id) {
        return null;
    }

    public static String createUrl(String bucket, String name) {

        return "http://localhost:9000/" + // TODO: Dinamicamente adicionar a URL, de acordo com o CDN
                bucket + "/" +
                name;
    }
}
