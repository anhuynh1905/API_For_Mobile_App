package com.ltdd.mobile_api.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Title for the media file
    @Column(nullable = false)
    private String title;

    // Description for the media file
    private String description;

    // Type of media (for example, MUSIC or VIDEO)
    @Column(nullable = false)
    private String type;

    // URL stored after uploading to cloud storage (e.g., Azure Blob Storage)
    @Column(nullable = false)
    private String url;

    // Username representing the owner of this media file
    @Column(nullable = false)
    private String ownerUsername;
}