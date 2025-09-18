package com.mpp.object_store.client;

import com.mpp.object_store.exceptions.ServerSideException;
import io.minio.*;
import io.minio.errors.MinioException;
import io.minio.messages.Bucket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Component
public class MinioBucketClient {

    private final MinioClient minioClient;
    private static final Logger log = LoggerFactory.getLogger(MinioBucketClient.class);

    public MinioBucketClient(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    public void createBucket(String bucketName) {
            if (!bucketExists(bucketName)) {
                try {
                    minioClient.makeBucket(MakeBucketArgs.builder()
                            .bucket(bucketName)
                            .build());

                    log.info("Bucket Created: {}", bucketName);
                } catch (MinioException | IOException | InvalidKeyException | NoSuchAlgorithmException e) {
                    log.error("Couldn't create bucket: {}", e.getMessage());
                }
            }
            log.info("Bucket Already Exists: {}", bucketName);
    }

    public boolean bucketExists(String bucket) {
        try {
            if (minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build())) {
                return true;
            }
        } catch (MinioException | IOException | InvalidKeyException | NoSuchAlgorithmException e) {
            log.error("Couldn't search for the bucket's existance: {}", e.getMessage());
        }

        return false;
    }

    public List<Bucket> listAllBuckets() {
        try {
            return minioClient.listBuckets();
        } catch (Exception e) {
            log.error("Couldn't execute operations on MINIO's API");
            throw new ServerSideException(e.getMessage());
        }
    }

    public void deleteBucket(String bucketName) {
        try {
            minioClient.removeBucket(RemoveBucketArgs.builder()
                    .bucket(bucketName)
                    .skipValidation(true)
                    .build());
        } catch (Exception e) {
            log.error("Couldn't execute operations on MINIO's API");
            throw new ServerSideException(e.getMessage());
        }
    }
}
