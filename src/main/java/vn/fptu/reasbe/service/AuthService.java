package vn.fptu.reasbe.service;

import org.springframework.http.ResponseEntity;

import vn.fptu.reasbe.model.dto.auth.JWTAuthResponse;
import vn.fptu.reasbe.model.dto.auth.LoginDto;
import vn.fptu.reasbe.model.dto.auth.SignupDto;
import vn.fptu.reasbe.model.dto.otp.OtpVerificationRequest;
import vn.fptu.reasbe.model.dto.user.UserResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    JWTAuthResponse authenticateUser(LoginDto loginDto);
    void prepareUserForOtp(SignupDto dto);
    JWTAuthResponse signupVerifiedUser(OtpVerificationRequest signupDto);
    String getGoogleLoginUrl();
    JWTAuthResponse authenticateGoogleUser(String code);
    UserResponse getUserInfo();
    ResponseEntity<JWTAuthResponse> refreshToken(HttpServletRequest request, HttpServletResponse response);
    void changePassword(String oldPassword, String newPassword);
}
