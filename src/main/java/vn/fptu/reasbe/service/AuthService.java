package vn.fptu.reasbe.service;

import org.springframework.http.ResponseEntity;

import vn.fptu.reasbe.model.dto.auth.JWTAuthResponse;
import vn.fptu.reasbe.model.dto.auth.LoginDto;
import vn.fptu.reasbe.model.dto.auth.SignupDto;
import vn.fptu.reasbe.model.dto.user.UserResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    JWTAuthResponse authenticateUser(LoginDto loginDto);

    String validateAndSendOtp(SignupDto dto);

    JWTAuthResponse signupVerifiedUser(SignupDto signupDto);

    String getGoogleLoginUrl();

    JWTAuthResponse authenticateGoogleUser(String code);

    UserResponse getUserInfo();

    ResponseEntity<JWTAuthResponse> refreshToken(HttpServletRequest request, HttpServletResponse response);

    void changePassword(String oldPassword, String newPassword);
}
