package com.mpp.object_store.controller;

import com.mpp.object_store.dto.FileDto;
import com.mpp.object_store.service.IFileSaveService;
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

//    @GetMapping("/{id}")
//    public FileDto getImageById(@PathVariable UUID id) {
//        return fileSaveService.getFileById(id);
//    }

    @GetMapping("/{objectKey}")
    public FileDto getImageByObjectKey(@PathVariable String objectKey) {
        return fileSaveService.getFileByName(objectKey);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public FileDto uploadImage(@RequestParam("file") MultipartFile file, @RequestParam("name") String name) {
        return fileSaveService.saveFile(file, name);
    }
}
