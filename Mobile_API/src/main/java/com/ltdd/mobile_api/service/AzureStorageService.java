package com.ltdd.mobile_api.service;

import org.springframework.web.multipart.MultipartFile;

public interface AzureStorageService {

    String uploadMediaFile(MultipartFile file);

    void deleteMediaFile(String fileUrl);
}
