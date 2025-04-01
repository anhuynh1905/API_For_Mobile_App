package com.ltdd.mobile_api.service;

import com.ltdd.mobile_api.payload.MediaResponse;
import com.ltdd.mobile_api.payload.MediaUploadRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MediaService {
    String uploadMedia(String username, MultipartFile file, MediaUploadRequest uploadRequest);

    List<MediaResponse> getUserMedia(String username);

    MediaResponse getMediaDetails(String username, Long mediaId);

    void deleteMedia(String username, Long mediaId);

    // New method to get all public media
    List<MediaResponse> getAllPublicMedia();
}
