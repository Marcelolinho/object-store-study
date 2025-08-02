package com.mpp.object_store.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "images")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Image extends BaseModel {

    @Column(name = "image_url", nullable = false)
    private String imageUrl;
}
