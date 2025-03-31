package com.ltdd.mobile_api.service;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class AzureStorageImpl implements AzureStorage {

    private final BlobServiceClient blobServiceClient;

    @Value("${azure.storage.container.profiles}")
    private String profileContainer;

    @Value("${azure.storage.container.music}")
    private String musicContainer;

    @Value("${azure.storage.container.video}")
    private String videoContainer;

    public AzureStorageImpl(BlobServiceClient blobServiceClient) {
        this.blobServiceClient = blobServiceClient;
    }

    @PostConstruct
    public void initContainers() {
        // Ensure containers exist
        createContainerIfNotExists(profileContainer);
        createContainerIfNotExists(musicContainer);
        createContainerIfNotExists(videoContainer);
    }

    public void createContainerIfNotExists(String containerName) {
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
        if (!containerClient.exists()) {
            containerClient.create();
        }
    }

    public String uploadProfilePicture(MultipartFile file) {
        return uploadFile(file, profileContainer);
    }

    public String uploadMusicFile(MultipartFile file) {
        return uploadFile(file, musicContainer);
    }

    public String uploadVideoFile(MultipartFile file) {
        return uploadFile(file, videoContainer);
    }

    public String uploadFile(MultipartFile file, String containerName) {
        try {
            // Generate a unique file name
            String originalFileName = file.getOriginalFilename();
            String extension = originalFileName != null ?
                    originalFileName.substring(originalFileName.lastIndexOf('.')) : "";
            String blobName = UUID.randomUUID().toString() + extension;

            // Get a reference to the container
            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);

            // Upload the file
            BlobClient blobClient = containerClient.getBlobClient(blobName);
            blobClient.upload(file.getInputStream(), file.getSize(), true);

            // Return the URL to the blob
            return blobClient.getBlobUrl();
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file", e);
        }
    }

    public void deleteFile(String url, String containerName) {
        try {
            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
            String blobName = url.substring(url.lastIndexOf('/') + 1);
            BlobClient blobClient = containerClient.getBlobClient(blobName);
            blobClient.delete();
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete file", e);
        }
    }

    public void deleteProfilePicture(String url) {
        deleteFile(url, profileContainer);
    }
}