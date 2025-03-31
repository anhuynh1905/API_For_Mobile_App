package com.ltdd.mobile_api.controller;

import com.ltdd.mobile_api.model.User;
import com.ltdd.mobile_api.payload.LoginRequest;
import com.ltdd.mobile_api.payload.MessageResponse;
import com.ltdd.mobile_api.payload.SignupRequest;
import com.ltdd.mobile_api.service.AuthService;
import com.ltdd.mobile_api.util.JwtUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtUtils jwtUtils;

    public AuthController(AuthService authService, JwtUtils jwtUtils) {
        this.authService = authService;
        this.jwtUtils = jwtUtils;
    }

    // Register endpoint: creates a new user and sends OTP via email
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest) {
        User user = authService.registerUser(signUpRequest);
        return ResponseEntity.ok(new MessageResponse("User registered successfully. Please check your email for the OTP."));
    }

    // OTP verification endpoint: verifies the OTP sent to user's email
    @PostMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestParam("code") String code) {
        authService.verifyUser(code);
        return ResponseEntity.ok(new MessageResponse("User verified successfully. You can now login."));
    }

    // Login endpoint: authenticates the user and returns a JWT token
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        authService.authenticateUser(loginRequest);
        String token = jwtUtils.generateJwtToken(loginRequest.getUsername());
        return ResponseEntity.ok(new MessageResponse("Login successful", token));
    }
}