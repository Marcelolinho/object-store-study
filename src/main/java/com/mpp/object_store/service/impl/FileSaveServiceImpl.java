package com.mpp.object_store.service.impl;

import com.mpp.object_store.client.MinioBucketClient;
import com.mpp.object_store.client.MinioFileClient;
import com.mpp.object_store.dto.FileDto;
import com.mpp.object_store.exceptions.BucketDoesNotExistsException;
import com.mpp.object_store.exceptions.CouldntPersistFileException;
import com.mpp.object_store.exceptions.FileAlreadyExistsBucketException;
import com.mpp.object_store.exceptions.ResourceNotFoundException;
import com.mpp.object_store.mappers.FileMapper;
import com.mpp.object_store.model.FileEntity;
import com.mpp.object_store.repository.FileRepository;
import com.mpp.object_store.service.IFileSaveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.FileAlreadyExistsException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Service("fileSaveService")
public class FileSaveServiceImpl implements IFileSaveService {

    private final FileRepository fileRepository;
    private final Logger log = LoggerFactory.getLogger(FileSaveServiceImpl.class);
    private final MinioFileClient minioFileClient;
    private final MinioBucketClient minioBucketClient;


    public FileSaveServiceImpl(FileRepository fileRepository, MinioFileClient minioFileClient, MinioBucketClient minioBucketClient) {
        this.fileRepository = fileRepository;
        this.minioFileClient = minioFileClient;
        this.minioBucketClient = minioBucketClient;
    }

    @Override
    public FileDto saveFile(MultipartFile file, String name, String bucketName) {

        if (Stream.of(file, name, bucketName).anyMatch(Objects::isNull)) {
            log.error("It cannot save with a null param");
            throw new IllegalArgumentException("There all null arguments in the request.");
        }

        if (!minioBucketClient.bucketExists(bucketName)) {
            log.error("Bucket does not exists with name {}", bucketName);
            throw new BucketDoesNotExistsException("Bucket does not exists, try the createBucket endpoint to create it.");
        }

        if (fileRepository.findByObjectKeyAndBucket(name, bucketName).isPresent()) {
            log.error("FIle already exists with this name in the bucket \n Name: {} \n Bucket: {}", name, bucketName);
            throw new FileAlreadyExistsBucketException("File already exists in bucket with name");
        }

        String url = minioFileClient.saveFile(file, name, bucketName);

        FileEntity fileEntity = FileEntity.builder()
                .url(url) // TODO: Melhorar implementação de URL com método estático
                .objectKey(name)
                .bucket(bucketName)
                .sizeBytes(file.getSize())
                .build();

        fileRepository.save(fileEntity);
        log.info("File saved {}", name);

        return FileMapper.toDto(fileEntity);
    }

    @Override
    public FileDto getFileByName(String objectKey, String bucketName) {

        Optional<FileEntity> file = fileRepository.findByObjectKeyAndBucket(objectKey, bucketName);

        if (file.isEmpty()) { // Feito assim para logar no log system TODO: Logar no DataDog ou Prometheus
            log.error("Entity not found: {}",objectKey);
            throw new CouldntPersistFileException("Entity not found by object key");
        }

        String url = minioFileClient.getFileUrl(objectKey, bucketName);

        return new FileDto(url, objectKey);
    }

    @Override
    public void deleteFileByName(String fileName, String bucketName) {
//        TODO: Deletar do Banco e da Object Store
        if (Stream.of(fileName, bucketName).anyMatch(Objects::isNull)) {
            log.error("There are null files in the request");
            throw new IllegalArgumentException("There are null arguments in the request.");
        }

        FileEntity file = fileRepository.findByObjectKeyAndBucket(fileName, bucketName).orElseThrow(() -> new ResourceNotFoundException("File not found by name and bucket"));

        minioFileClient.deleteFile(file.getObjectKey(), file.getBucket());

        fileRepository.delete(file);
        log.info("File '{}' was deleted from minio and db", fileName);
    }

    @Override
    public void deleteFileById(UUID id) {
//        TODO: Deletar do banco e da Obejct Store
    }

    @Override
    public FileDto getFileById(UUID id) {
//        TODO: Buscar no banco e atualizar a url
        return null;
    }

    public static String createUrl(String bucket, String name) {

        return "http://localhost:9000/" + // TODO: Refazer lógica para buscar "getPresignedObjectUrl"
                bucket + "/" +
                name;
    }
}
