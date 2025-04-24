package com.ltdd.mobile_api.service.Impl;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.sas.BlobSasPermission;
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues;
import com.ltdd.mobile_api.service.AzureStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.OffsetDateTime;
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
        this.musicContainer   = musicContainer;
        this.videoContainer   = videoContainer;
        this.profilesContainer = profilesContainer;
    }

    @Override
    public String uploadMediaFile(MultipartFile file, String mediaType) {
        try {
            String containerName = mediaType.equalsIgnoreCase("MUSIC")
                    ? musicContainer
                    : videoContainer;
            BlobContainerClient container = blobServiceClient
                    .getBlobContainerClient(containerName);

            // make sure container exists & is at least Blob‐public (optional)
            // container.createIfNotExists();
            // container.setAccessPolicy(PublicAccessType.BLOB, null);

            String fileName = "media-" + UUID.randomUUID() + "-" + file.getOriginalFilename();
            InputStream stream = file.getInputStream();
            container.getBlobClient(fileName)
                     .upload(stream, file.getSize(), true);

            // Generate a read‐only SAS valid for 1 hour
            BlobClient blobClient = container.getBlobClient(fileName);
            BlobSasPermission perms = new BlobSasPermission()
                    .setReadPermission(true);
            OffsetDateTime expiry = OffsetDateTime.now().plusHours(1);
            BlobServiceSasSignatureValues values = new BlobServiceSasSignatureValues(expiry, perms);
            String sasToken = blobClient.generateSas(values);

            // Return the full URL including SAS
            return blobClient.getBlobUrl() + "?" + sasToken;
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload media file", e);
        }
    }

    @Override
    public String uploadProfilePicture(MultipartFile file) {
        try {
            BlobContainerClient container = blobServiceClient
                    .getBlobContainerClient(profilesContainer);

            String fileName = "profile-" + UUID.randomUUID() + "-" + file.getOriginalFilename();
            InputStream stream = file.getInputStream();
            container.getBlobClient(fileName)
                     .upload(stream, file.getSize(), true);

            // Generate SAS for profile pictures as well
            BlobClient blobClient = container.getBlobClient(fileName);
            BlobSasPermission perms = new BlobSasPermission().setReadPermission(true);
            OffsetDateTime expiry = OffsetDateTime.now().plusHours(1);
            BlobServiceSasSignatureValues values = new BlobServiceSasSignatureValues(expiry, perms);
            String sasToken = blobClient.generateSas(values);

            return blobClient.getBlobUrl() + "?" + sasToken;
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload profile picture", e);
        }
    }

    @Override
    public void deleteMediaFile(String fileUrl, String mediaType) {
        try {
            String containerName = mediaType.equalsIgnoreCase("MUSIC")
                    ? musicContainer
                    : videoContainer;
            BlobContainerClient container = blobServiceClient
                    .getBlobContainerClient(containerName);

            String fileName = fileUrl.substring(fileUrl.lastIndexOf('/') + 1)
                                 .split("\\?")[0]; // strip off any SAS
            container.getBlobClient(fileName).delete();
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete media file", e);
        }
    }
}
