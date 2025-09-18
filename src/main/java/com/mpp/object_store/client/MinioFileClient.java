package com.mpp.object_store.client;

import com.mpp.object_store.exceptions.CouldntDeleteFileException;
import com.mpp.object_store.exceptions.CouldntPersistFileException;
import io.minio.*;
import io.minio.http.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

@Component
public class MinioFileClient {

    private static final Logger log = LoggerFactory.getLogger(MinioFileClient.class);
    private final MinioClient minioClient;
    private final MinioBucketClient minioBucketClient;

    public MinioFileClient(MinioClient minioClient, MinioBucketClient minioBucketClient) {
        this.minioClient = minioClient;
        this.minioBucketClient = minioBucketClient;
    }

    public String saveFile(MultipartFile file, String name, String bucketName) {
        if (bucketName.isEmpty()) {
            return null;
        }

        for (int i = 0; i < 4; i++) {
            try {
                minioBucketClient.createBucket(bucketName);

                InputStream fileStream = file.getInputStream();

                PutObjectArgs.Builder builder =  PutObjectArgs.builder()
                        .stream(fileStream, file.getSize(), -1)
                        .bucket(bucketName)
                        .object(name)
                        .contentType(file.getContentType());

                minioClient.putObject(builder.build());

                return name;
            } catch (Exception e) {
                log.error("Couldn't save file at {} try: {}", i, e.getMessage());
                if (i == 3) {
                    throw new CouldntPersistFileException(String.format("Couldn't persist file after 3 tries: %s", e.getMessage()));
                }
            }
        }
        return null;
    }

    public void deleteFile(String fileName, String bucketName) {
        if (Stream.of(fileName, bucketName).anyMatch(Objects::isNull)) {
            log.warn("Cannot delete file without the required args");
            return;
        }

        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .build());

            log.info("File '{}' was deleted from bucket '{}'", fileName, bucketName);
        } catch(Exception e) {
            log.error("Couldn't delete file: {}", fileName);
            throw new CouldntDeleteFileException(String.format("Couldn't delete file: %s", e.getMessage()));
        }
    }

    public String getFileUrl(String fileName, String bucketName) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .expiry(5, TimeUnit.HOURS)
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(fileName)
                            .build()
            );
        } catch (Exception e) {
            log.error("Couldn't get file url: {}", e.getMessage());
            throw new CouldntPersistFileException(String.format("Couldn't get file url: %s", e.getMessage()));
        }
    }
}
