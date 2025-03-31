package com.ltdd.mobile_api.controller;

import com.ltdd.mobile_api.payload.MediaUploadRequest;
import com.ltdd.mobile_api.payload.MessageResponse;
import com.ltdd.mobile_api.payload.MediaResponse;
import com.ltdd.mobile_api.service.MediaService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/media")
public class MediaController {

    private final MediaService mediaService;

    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    // Endpoint to upload a media file (music or video)
    @PostMapping("/upload")
    public ResponseEntity<?> uploadMedia(@RequestPart("file") MultipartFile file,
                                         @RequestPart("data") MediaUploadRequest mediaUploadRequest,
                                         @AuthenticationPrincipal UserDetails currentUser) {
        String mediaUrl = mediaService.uploadMedia(currentUser.getUsername(), file, mediaUploadRequest);
        return ResponseEntity.ok(new MessageResponse("Media uploaded successfully", mediaUrl));
    }

    // Endpoint to retrieve all media files for the authenticated user
    @GetMapping
    public ResponseEntity<List<MediaResponse>> getUserMedia(@AuthenticationPrincipal UserDetails currentUser) {
        List<MediaResponse> mediaList = mediaService.getUserMedia(currentUser.getUsername());
        return ResponseEntity.ok(mediaList);
    }

    // Endpoint to fetch a specific media file details by its id
    @GetMapping("/{id}")
    public ResponseEntity<MediaResponse> getMediaDetails(@PathVariable Long id,
                                                         @AuthenticationPrincipal UserDetails currentUser) {
        MediaResponse media = mediaService.getMediaDetails(currentUser.getUsername(), id);
        return ResponseEntity.ok(media);
    }

    // Endpoint to delete a media file by its id
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMedia(@PathVariable Long id,
                                         @AuthenticationPrincipal UserDetails currentUser) {
        mediaService.deleteMedia(currentUser.getUsername(), id);
        return ResponseEntity.ok(new MessageResponse("Media deleted successfully"));
    }
}