package com.mpp.object_store.service;

import com.mpp.object_store.dto.FileDto;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Repository
public interface IFileSaveService {

    FileDto saveFile(MultipartFile file, String name);

    FileDto getFileByName(String name);

    void deleteFileByName(String fileName);

    void deleteFileById(UUID id);

    FileDto getFileById(UUID id);
}
