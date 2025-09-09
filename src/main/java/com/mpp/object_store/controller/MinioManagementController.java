package com.mpp.object_store.controller;

import com.mpp.object_store.dto.BucketDto;
import com.mpp.object_store.service.IMinioManagementService;
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

    @DeleteMapping("/deleteBucket")
    public void deleteBucket(@PathParam("name") String bucketName) {
        minioManagementService.deleteBucketByName(bucketName);
    }
}
