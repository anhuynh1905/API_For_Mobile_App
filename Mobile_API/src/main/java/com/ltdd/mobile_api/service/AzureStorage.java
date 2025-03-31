package com.ltdd.mobile_api.service;

import org.springframework.web.multipart.MultipartFile;

public interface AzureStorage {
    void createContainerIfNotExists(String containerName);

    String uploadProfilePicture(MultipartFile file);

    String uploadMusicFile(MultipartFile file);

    String uploadVideoFile(MultipartFile file);

    String uploadFile(MultipartFile file, String containerName);

    void deleteFile(String url, String containerName);

    void deleteProfilePicture(String url);
}
