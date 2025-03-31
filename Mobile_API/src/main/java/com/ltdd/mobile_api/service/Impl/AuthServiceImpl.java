package com.ltdd.mobile_api.service.Impl;

import com.ltdd.mobile_api.model.User;
import com.ltdd.mobile_api.payload.LoginRequest;
import com.ltdd.mobile_api.payload.SignupRequest;
import com.ltdd.mobile_api.repository.UserRepository;
import com.ltdd.mobile_api.service.AuthService;
import com.ltdd.mobile_api.service.MailService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       MailService mailService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public User registerUser(SignupRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username is already taken!");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email is already in use!");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        // Generate OTP code and expiry
        String otp = generateOtp();
        user.setVerificationCode(otp);
        user.setVerificationCodeExpiryTime(LocalDateTime.now().plusMinutes(10));
        user.setActive(false);
        userRepository.save(user);
        // Send OTP via email
        mailService.sendOtpEmail(user.getEmail(), otp);
        return user;
    }

    @Override
    public void verifyUser(String otp) {
        User user = userRepository.findByVerificationCode(otp)
                .orElseThrow(() -> new RuntimeException("Invalid OTP"));
        // You can also check for expiry time if needed
        user.setActive(true);
        user.setVerificationCode(null);
        user.setVerificationCodeExpiryTime(null);
        userRepository.save(user);
    }

    @Override
    public void authenticateUser(LoginRequest request) {
        // Use AuthenticationManager to authenticate user credentials. If authentication fails, an exception will be thrown.
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
    }

    // Simple OTP generation logic (for production use a more robust approach)
    @Override
    public String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }


}