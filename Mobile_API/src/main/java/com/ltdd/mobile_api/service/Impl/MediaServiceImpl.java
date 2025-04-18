package com.ltdd.mobile_api.service.Impl;

import com.ltdd.mobile_api.model.Media;
import com.ltdd.mobile_api.payload.MediaUploadRequest;
import com.ltdd.mobile_api.payload.MediaResponse;
import com.ltdd.mobile_api.repository.MediaRepository;
import com.ltdd.mobile_api.repository.UserRepository;
import com.ltdd.mobile_api.service.AzureStorageService;
import com.ltdd.mobile_api.service.MediaService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MediaServiceImpl implements MediaService {

    private final MediaRepository mediaRepository;
    private final UserRepository userRepository;
    private final AzureStorageService azureStorageService;

    public MediaServiceImpl(MediaRepository mediaRepository, UserRepository userRepository, AzureStorageService azureStorageService) {
        this.mediaRepository = mediaRepository;
        this.userRepository = userRepository;
        this.azureStorageService = azureStorageService;
    }

    @Override
    public String uploadMedia(String username, MultipartFile file, MediaUploadRequest uploadRequest) {
        // Verify that the user exists
        userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Upload file to Azure Blob Storage
        String fileUrl = azureStorageService.uploadMediaFile(file, uploadRequest.getType());

        // Save media metadata to database
        Media media = new Media();
        media.setTitle(uploadRequest.getTitle());
        media.setDescription(uploadRequest.getDescription());
        media.setType(uploadRequest.getType()); // e.g., MUSIC or VIDEO
        media.setUrl(fileUrl);
        media.setOwnerUsername(username);
        // Set whether media is public based on the upload request
        media.setPublic(uploadRequest.isPublic());
        mediaRepository.save(media);
        return fileUrl;
    }

    @Override
    public List<MediaResponse> getUserMedia(String username) {
        List<Media> mediaList = mediaRepository.findAllByOwnerUsername(username);
        return mediaList.stream()
                .map(media -> new MediaResponse(media.getId(), media.getTitle(), media.getDescription(), media.getType(), media.getUrl(), media.isPublic()))
                .collect(Collectors.toList());
    }

    @Override
    public MediaResponse getMediaDetails(String username, Long mediaId) {
        Media media = mediaRepository.findByIdAndOwnerUsername(mediaId, username)
                .orElseThrow(() -> new RuntimeException("Media not found or access denied"));
        return new MediaResponse(media.getId(), media.getTitle(), media.getDescription(), media.getType(), media.getUrl(), media.isPublic());
    }

    // Updated deleteMedia to pass media type
    @Override
    public void deleteMedia(String username, Long mediaId) {
        Media media = mediaRepository.findByIdAndOwnerUsername(mediaId, username)
                .orElseThrow(() -> new RuntimeException("Media not found or access denied"));
        // Pass media type to the azureStorageService so that the correct container is used
        azureStorageService.deleteMediaFile(media.getUrl(), media.getType());
        mediaRepository.delete(media);
    }

    @Override
    public List<MediaResponse> getAllPublicMedia() {
        List<Media> publicMediaList = mediaRepository.findByIsPublicTrue();
        return publicMediaList.stream()
                .map(media -> new MediaResponse(media.getId(), media.getTitle(), media.getDescription(), media.getType(), media.getUrl(), media.isPublic()))
                .collect(Collectors.toList());
    }
}