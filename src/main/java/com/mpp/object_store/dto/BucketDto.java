package com.mpp.object_store.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BucketDto {
    private String name;
    private LocalDateTime dhCreation;
}
