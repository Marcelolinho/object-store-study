package com.mpp.object_store.service.impl;

import com.mpp.object_store.client.MinioBucketClient;
import com.mpp.object_store.dto.BucketDto;
import com.mpp.object_store.dto.FileDto;
import com.mpp.object_store.exceptions.BucketNotEmptyException;
import com.mpp.object_store.mappers.BucketMapper;
import com.mpp.object_store.mappers.FileMapper;
import com.mpp.object_store.model.FileEntity;
import com.mpp.object_store.repository.FileRepository;
import com.mpp.object_store.service.IMinioManagementService;
import io.minio.messages.Bucket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("minioManagementService")
public class MinioManagementService implements IMinioManagementService {

    private static final Logger log = LoggerFactory.getLogger(MinioManagementService.class);
    private final MinioBucketClient minioBucketClient;
    private final FileRepository fileRepository;

    public MinioManagementService(MinioBucketClient minioBucketClient, FileRepository fileRepository) {
        this.minioBucketClient = minioBucketClient;
        this.fileRepository = fileRepository;
    }

    @Override
    public List<BucketDto> listAllBuckets() {

        List<Bucket> bucketList = minioBucketClient.listAllBuckets();

        if (bucketList.isEmpty()) {
            log.warn("There are no buckets created!");
            return null;
        }

        return bucketList.stream().map(BucketMapper::toDto).toList();
    }

    @Override
    public void deleteBucketByName(String name) {
        if (name.isEmpty()) {
            return;
        }

        List<FileEntity> listOfFiles = fileRepository.getAllByBucket(name);

        if (!listOfFiles.isEmpty()) {
            List<FileDto> listOfFileDto = listOfFiles.stream().map(FileMapper::toDto).toList();

            log.warn("There are still files saved on bucket: {}", name);
            throw new BucketNotEmptyException(String.format("There are %s files to delete before deleting bucket\nFiles to delete: %s", listOfFiles.size(), listOfFileDto));
        }

        minioBucketClient.deleteBucket(name);
    }
}
