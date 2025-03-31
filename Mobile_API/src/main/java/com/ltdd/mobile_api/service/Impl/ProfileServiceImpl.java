package com.ltdd.mobile_api.service.Impl;

import com.ltdd.mobile_api.model.User;
import com.ltdd.mobile_api.payload.ProfileUpdateRequest;
import com.ltdd.mobile_api.repository.UserRepository;
import com.ltdd.mobile_api.service.AzureStorageService;
import com.ltdd.mobile_api.service.ProfileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;
    private final AzureStorageService azureStorageService;

    public ProfileServiceImpl(UserRepository userRepository, AzureStorageService azureStorageService) {
        this.userRepository = userRepository;
        this.azureStorageService = azureStorageService;
    }

    @Override
    public void updateProfile(String username, ProfileUpdateRequest profileRequest) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setFullName(profileRequest.getFullName());
            user.setEmail(profileRequest.getEmail());
            // Update additional fields as required.
            userRepository.save(user);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    @Override
    public String uploadProfilePicture(String username, MultipartFile file) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        User user = userOpt.get();
        String fileUrl = azureStorageService.uploadMediaFile(file);
        user.setProfilePictureUrl(fileUrl);
        userRepository.save(user);
        return fileUrl;
    }
}