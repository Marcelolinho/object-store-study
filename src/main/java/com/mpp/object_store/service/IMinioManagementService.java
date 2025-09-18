package com.mpp.object_store.service;

import com.mpp.object_store.dto.BucketDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IMinioManagementService {

    List<BucketDto> listAllBuckets();

    void deleteBucketByName(String name);

    void emptyBucketByName(String name);

    BucketDto createBucket(String name);
}
