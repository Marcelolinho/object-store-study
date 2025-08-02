package com.mpp.object_store.service;

import com.mpp.object_store.repository.ImageRepository;
import io.minio.MinioClient;
import org.springframework.stereotype.Service;

@Service("imageSaveService")
public class ImageSaveService {

    private final MinioClient minioClient;
    private final ImageRepository imageRepository;

    public ImageSaveService(MinioClient minioClient, ImageRepository imageRepository) {
        this.minioClient = minioClient;
        this.imageRepository = imageRepository;
    }
}
