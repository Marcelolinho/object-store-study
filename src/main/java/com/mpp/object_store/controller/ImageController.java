package com.mpp.object_store.controller;

import com.mpp.object_store.dto.FileDto;
import com.mpp.object_store.service.IFileSaveService;
import jakarta.websocket.server.PathParam;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/image")
public class ImageController {

    private final IFileSaveService fileSaveService;

    public ImageController(IFileSaveService fileSaveService) {
        this.fileSaveService = fileSaveService;
    }

    @GetMapping("/{id}")
    public FileDto getImageById(@PathParam("id") UUID id) {
        return fileSaveService.getFileById(id);
    }

    @GetMapping
    public FileDto getImageByObjectKey(@RequestParam("name") String objectKey, String bucketName) {
        return fileSaveService.getFileByName(objectKey, bucketName);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public FileDto uploadImage(@RequestParam("file") MultipartFile file, @RequestParam("name") String name, @RequestParam("bucket") String bucketName) {
        return fileSaveService.saveFile(file, name, bucketName);
    }

    @DeleteMapping("/deleteFile")
    public void deleteFile(@PathParam("name") String name, @PathParam("bucket") String bucket) {
        fileSaveService.deleteFileByName(name, bucket);
    }
}
