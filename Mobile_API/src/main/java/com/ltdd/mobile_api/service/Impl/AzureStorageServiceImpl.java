package com.ltdd.mobile_api.service.Impl;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.ltdd.mobile_api.service.AzureStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;
import java.util.UUID;

@Service
public class AzureStorageServiceImpl implements AzureStorageService {

    private final BlobServiceClient blobServiceClient;
    private final String musicContainer;
    private final String videoContainer;
    private final String profilesContainer;

    public AzureStorageServiceImpl(
            @Value("${azure.storage.connection-string}") String connectionString,
            @Value("${azure.storage.container.music}") String musicContainer,
            @Value("${azure.storage.container.video}") String videoContainer,
            @Value("${azure.storage.container.profiles}") String profilesContainer) {
        this.blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();
        this.musicContainer = musicContainer;
        this.videoContainer = videoContainer;
        this.profilesContainer = profilesContainer;
    }

    @Override
    public String uploadMediaFile(MultipartFile file, String mediaType) {
        try {
            String containerName = mediaType.equalsIgnoreCase("MUSIC") ? musicContainer : videoContainer;
            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);

            String fileName = "media-" + UUID.randomUUID() + "-" + file.getOriginalFilename();
            InputStream fileStream = file.getInputStream();
            containerClient.getBlobClient(fileName).upload(fileStream, file.getSize(), true);
            return containerClient.getBlobClient(fileName).getBlobUrl();
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload media file", e);
        }
    }

    @Override
    public String uploadProfilePicture(MultipartFile file) {
        try {
            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(profilesContainer);
            String fileName = "profile-" + UUID.randomUUID() + "-" + file.getOriginalFilename();
            InputStream fileStream = file.getInputStream();
            containerClient.getBlobClient(fileName).upload(fileStream, file.getSize(), true);
            return containerClient.getBlobClient(fileName).getBlobUrl();
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload profile picture", e);
        }
    }

    @Override
    public void deleteMediaFile(String fileUrl, String mediaType) {
        try {
            String containerName = mediaType.equalsIgnoreCase("MUSIC") ? musicContainer : videoContainer;
            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);

            String[] tokens = fileUrl.split("/");
            String fileName = tokens[tokens.length - 1];
            containerClient.getBlobClient(fileName).delete();
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete media file", e);
        }
    }
}