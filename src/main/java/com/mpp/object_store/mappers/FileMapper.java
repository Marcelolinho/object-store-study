package com.mpp.object_store.mappers;

import com.mpp.object_store.dto.FileDto;
import com.mpp.object_store.model.FileEntity;

public class FileMapper {

    public static FileDto toDto(FileEntity entity) {
        final FileDto dto = new FileDto();

        dto.setUrl(entity.getUrl());
        dto.setName(entity.getObjectKey());

        return dto;
    }
}
