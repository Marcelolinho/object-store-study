package com.mpp.object_store.repository;

import com.mpp.object_store.model.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long> {
    Optional<FileEntity> findByObjectKeyAndBucket(String objectKey, String bucket);

    List<FileEntity> getAllByBucket(String bucket);

    void deleteAllByBucket(String bucket);

    long countByBucket(String bucket);
}
