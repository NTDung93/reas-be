package vn.fptu.reasbe.service;

import org.springframework.http.ResponseEntity;

import vn.fptu.reasbe.model.dto.auth.JWTAuthResponse;
import vn.fptu.reasbe.model.dto.auth.LoginDto;
import vn.fptu.reasbe.model.dto.auth.SignupDto;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.fptu.reasbe.model.entity.User;

public interface AuthService {
    JWTAuthResponse authenticateUser(LoginDto loginDto);

    String validateAndSendOtp(SignupDto dto);

    JWTAuthResponse signupVerifiedUser(SignupDto signupDto);

    String getGoogleLoginUrl();

    JWTAuthResponse authenticateGoogleUser(String code);

    User getCurrentUser();

    ResponseEntity<JWTAuthResponse> refreshToken(HttpServletRequest request, HttpServletResponse response);

    void changePassword(String oldPassword, String newPassword);
}
