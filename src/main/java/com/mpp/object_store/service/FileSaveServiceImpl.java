package com.mpp.object_store.service;

import com.mpp.object_store.dto.FileDto;
import com.mpp.object_store.model.FileEntity;
import com.mpp.object_store.repository.FileRepository;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service("fileSaveService")
public class FileSaveServiceImpl implements IFileSaveService{

    private final MinioClient minioClient;
    private final FileRepository fileRepository;
    private final Logger log = LoggerFactory.getLogger(FileSaveServiceImpl.class);

    public FileSaveServiceImpl(MinioClient minioClient, FileRepository fileRepository) {
        this.minioClient = minioClient;
        this.fileRepository = fileRepository;
    }

    @Override
    public FileDto saveFile(MultipartFile file, String name) {
//        MultipartFile file = dto.getFile();
//        String name = dto.getFileName();

        try {

            InputStream fileStream = file.getInputStream();

            PutObjectArgs.Builder builder =  PutObjectArgs.builder()
                    .stream(fileStream, file.getSize(), -1)
                    .bucket("files")
                    .object(name)
                    .contentType(file.getContentType());

            minioClient.putObject(builder.build());

            String url = createUrl(name);

            FileEntity fileEntity = FileEntity.builder()
                    .name(name)
                    .url(url)
                    .sizeBytes(file.getSize())
                    .build();

            fileRepository.save(fileEntity);
            log.info("File saved {}", name);

            return FileDto.builder()
                    .name(name)
                    .url(url)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public FileDto getFileByName(String name) {
        return null;
    }

    @Override
    public void deleteFileByName(String fileName) {

    }

    @Override
    public void deleteFileById(UUID id) {

    }

    @Override
    public FileDto getFileById(UUID id) {
        return null;
    }

    public static String createUrl(String name) {

        return "https://localhost:8080/" +
                "object-store/" +
                name;
    }
}
