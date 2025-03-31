package com.ltdd.mobile_api.service;

import org.springframework.web.multipart.MultipartFile;

public interface AzureStorageService {


    String uploadMediaFile(MultipartFile file, String mediaType);

    void deleteMediaFile(String fileUrl, String mediaType);

    // Dedicated method for uploading profile pictures
    String uploadProfilePicture(MultipartFile file);
}
