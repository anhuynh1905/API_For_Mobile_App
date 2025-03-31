package com.ltdd.mobile_api.service;

import com.ltdd.mobile_api.model.User;
import com.ltdd.mobile_api.payload.LoginRequest;
import com.ltdd.mobile_api.payload.SignupRequest;

public interface AuthService {

    User registerUser(SignupRequest request);

    void verifyUser(String otp);

    void authenticateUser(LoginRequest request);

    // Simple OTP generation logic (for production use a more robust approach)
    String generateOtp();
}
