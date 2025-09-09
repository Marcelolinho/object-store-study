package com.mpp.object_store.mappers;

import com.mpp.object_store.dto.BucketDto;
import io.minio.messages.Bucket;

public class BucketMapper {

    public static BucketDto toDto(Bucket b) {
        return new BucketDto(b.name(), b.creationDate().toLocalDateTime());
    }
}
