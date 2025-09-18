package com.mpp.object_store.service;

import com.mpp.object_store.dto.FileDto;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Repository
public interface IFileSaveService {

    FileDto saveFile(MultipartFile file, String name, String bucketName);

    FileDto getFileByName(String name, String bucketName);

    void deleteFileByName(String fileName, String bucketName);

    void deleteFileById(UUID id);

    FileDto getFileById(UUID id);
}
