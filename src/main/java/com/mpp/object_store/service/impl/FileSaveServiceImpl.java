package com.mpp.object_store.service.impl;

import com.mpp.object_store.client.MinioFileClient;
import com.mpp.object_store.dto.FileDto;
import com.mpp.object_store.mappers.FileMapper;
import com.mpp.object_store.model.FileEntity;
import com.mpp.object_store.repository.FileRepository;
import com.mpp.object_store.service.IFileSaveService;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.http.Method;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service("fileSaveService")
public class FileSaveServiceImpl implements IFileSaveService {

    private final MinioClient minioClient;
    private final FileRepository fileRepository;
    private final Logger log = LoggerFactory.getLogger(FileSaveServiceImpl.class);
    private final MinioFileClient minioFileClient;

    public FileSaveServiceImpl(MinioClient minioClient, FileRepository fileRepository, MinioFileClient minioFileClient) {
        this.minioClient = minioClient;
        this.fileRepository = fileRepository;
        this.minioFileClient = minioFileClient;
    }

    @Override
    public FileDto saveFile(MultipartFile file, String name, String bucketName) {

        String url = minioFileClient.saveFile(file, name, bucketName);

        FileEntity fileEntity = FileEntity.builder()
                .url(url)
                .objectKey(name)
                .bucket("files")
                .sizeBytes(file.getSize())
                .build();

        fileRepository.save(fileEntity);
        log.info("File saved {}", name);

        return FileMapper.toDto(fileEntity);
    }

    @Override
    public FileDto getFileByName(String objectKey, String bucketName) {

        try {
            Optional<FileEntity> file = fileRepository.findByObjectKeyAndBucket(objectKey, bucketName);

            if (file.isEmpty()) { // Feito assim para logar no log system TODO: Logar no DataDog ou Prometheus
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
    public void deleteFileByName(String fileName, String bucketName) {

    }

    @Override
    public void deleteFileById(UUID id) {

    }

    @Override
    public FileDto getFileById(UUID id) {
        return null;
    }

    public static String createUrl(String bucket, String name) {

        return "http://localhost:9000/" + // TODO: Refazer lógica para buscar "getPresignedObjectUrl"
                bucket + "/" +
                name;
    }
}
