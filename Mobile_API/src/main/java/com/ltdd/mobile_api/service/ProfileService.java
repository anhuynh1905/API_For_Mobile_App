package com.ltdd.mobile_api.service;

import com.ltdd.mobile_api.payload.ProfileUpdateRequest;
import org.springframework.web.multipart.MultipartFile;

public interface ProfileService {
    void updateProfile(String username, ProfileUpdateRequest profileRequest);

    String uploadProfilePicture(String username, MultipartFile file);
}
