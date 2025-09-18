package com.mpp.object_store.controller;

import com.mpp.object_store.dto.BucketDto;
import com.mpp.object_store.service.IMinioManagementService;
import io.minio.messages.Bucket;
import jakarta.websocket.server.PathParam;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/minio")
public class MinioManagementController {

    private final IMinioManagementService minioManagementService;

    public MinioManagementController(IMinioManagementService minioManagementService) {
        this.minioManagementService = minioManagementService;
    }

    @GetMapping("/listBuckets")
    public List<BucketDto> getAllBuckets() {
        return minioManagementService.listAllBuckets();
    }

    @DeleteMapping("/deleteBucket/{name}")
    public void deleteBucket(@PathVariable("name") String bucketName) {
        minioManagementService.deleteBucketByName(bucketName);
    }

    @DeleteMapping("emptyBucket/{name}")
    public void emptyBucket(@PathVariable("name") String bucketName) {
        minioManagementService.emptyBucketByName(bucketName);
    }

    @PostMapping("/createBucket")
    public BucketDto createBucket(@RequestBody BucketDto dto) {
        return minioManagementService.createBucket(dto.getName());
    }
}
