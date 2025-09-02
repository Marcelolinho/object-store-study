package com.mpp.object_store.controller;

import com.mpp.object_store.dto.FileDto;
import com.mpp.object_store.dto.UploadRequestDto;
import com.mpp.object_store.service.FileSaveServiceImpl;
import com.mpp.object_store.service.IFileSaveService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/image")
public class ImageController {

    private final FileSaveServiceImpl fileSaveService;

    public ImageController(FileSaveServiceImpl fileSaveService) {
        this.fileSaveService = fileSaveService;
    }

    @GetMapping("/{id}")
    public FileDto getImage(@PathVariable UUID id) {
        return fileSaveService.getFileById(id);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public FileDto uploadImage(@RequestParam("file") MultipartFile file, @RequestParam("name") String name) {
        return fileSaveService.saveFile(file, name);
    }
}
