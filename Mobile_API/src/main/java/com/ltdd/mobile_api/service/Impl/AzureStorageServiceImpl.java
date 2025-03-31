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

    private final BlobContainerClient containerClient;

    public AzureStorageServiceImpl(@Value("${azure.storage.connection-string}") String connectionString,
                               @Value("${azure.storage.container.media}") String containerName) {
        BlobServiceClient serviceClient = new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();
        this.containerClient = serviceClient.getBlobContainerClient(containerName);
    }

    @Override
    public String uploadMediaFile(MultipartFile file) {
        try {
            String fileName = "media-" + UUID.randomUUID() + "-" + file.getOriginalFilename();
            InputStream fileStream = file.getInputStream();
            containerClient.getBlobClient(fileName).upload(fileStream, file.getSize(), true);
            return containerClient.getBlobClient(fileName).getBlobUrl();
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload media file", e);
        }
    }

    @Override
    public void deleteMediaFile(String fileUrl) {
        try {
            // Extract file name from the URL (assuming the URL format is consistent)
            String[] tokens = fileUrl.split("/");
            String fileName = tokens[tokens.length - 1];
            containerClient.getBlobClient(fileName).delete();
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete media file", e);
        }
    }
}