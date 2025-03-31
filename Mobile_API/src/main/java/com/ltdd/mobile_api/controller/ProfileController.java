package com.ltdd.mobile_api.controller;

import com.ltdd.mobile_api.payload.MessageResponse;
import com.ltdd.mobile_api.payload.ProfileUpdateRequest;
import com.ltdd.mobile_api.service.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/user")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@RequestBody ProfileUpdateRequest profileRequest,
                                           @AuthenticationPrincipal UserDetails currentUser) {
        profileService.updateProfile(currentUser.getUsername(), profileRequest);
        return ResponseEntity.ok(new MessageResponse("Profile updated successfully"));
    }

    @PostMapping("/upload-profile-picture")
    public ResponseEntity<?> uploadProfilePicture(@RequestParam("file") MultipartFile file,
                                                  @AuthenticationPrincipal UserDetails currentUser) {
        String imageUrl = profileService.uploadProfilePicture(currentUser.getUsername(), file);
        return ResponseEntity.ok(new MessageResponse("Profile picture uploaded successfully", imageUrl));
    }
}