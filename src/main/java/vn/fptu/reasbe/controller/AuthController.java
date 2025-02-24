package vn.fptu.reasbe.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import vn.fptu.reasbe.model.constant.AppConstants;
import vn.fptu.reasbe.model.dto.auth.JWTAuthResponse;
import vn.fptu.reasbe.model.dto.auth.LoginDto;
import vn.fptu.reasbe.model.dto.auth.PasswordChangeRequest;
import vn.fptu.reasbe.model.dto.auth.SignupDto;
import vn.fptu.reasbe.model.dto.user.UserResponse;
import vn.fptu.reasbe.service.AuthService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final HttpHeaders headers = new HttpHeaders();

    @PostMapping("/login")
    public ResponseEntity<JWTAuthResponse> authenticationUser(@RequestBody @Valid LoginDto loginDto){
        JWTAuthResponse jwtAuthResponse = authService.authenticateUser(loginDto);
        headers.add(AppConstants.AUTH_ATTR_NAME, AppConstants.AUTH_VALUE_PREFIX + jwtAuthResponse.getAccessToken());
        return new ResponseEntity<>(jwtAuthResponse, headers, HttpStatus.OK);
    }

    @PostMapping("/register/user")
    public ResponseEntity<JWTAuthResponse> signupUser(@Valid @RequestBody SignupDto signupDto){
        JWTAuthResponse jwtAuthResponse = authService.signupVerifiedUser(signupDto);
        headers.add(AppConstants.AUTH_ATTR_NAME, AppConstants.AUTH_VALUE_PREFIX + jwtAuthResponse.getAccessToken());
        return new ResponseEntity<>(jwtAuthResponse, headers, HttpStatus.OK);
    }

    @PostMapping("/otp")
    public ResponseEntity<String> sendOtp(@Valid @RequestBody SignupDto signupDto){
        return ResponseEntity.ok(authService.validateAndSendOtp(signupDto));
    }

    @PostMapping("/oauth2/login")
    public ResponseEntity<String> handleGoogleLogin() {
        return ResponseEntity.ok(authService.getGoogleLoginUrl());
    }

    @GetMapping("/oauth2/callback")
    public ResponseEntity<JWTAuthResponse> handleGoogleCallback(@RequestParam("code") String code) {
        JWTAuthResponse tokenResponse = authService.authenticateGoogleUser(code);
        headers.add(AppConstants.AUTH_ATTR_NAME, AppConstants.AUTH_VALUE_PREFIX + tokenResponse.getAccessToken());
        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping("/change-password")
    public ResponseEntity<Boolean> changePassword(@RequestBody PasswordChangeRequest request) {
        authService.changePassword(request.getOldPassword(), request.getNewPassword());
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<JWTAuthResponse> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        return authService.refreshToken(request, response);
    }

    @SecurityRequirement(name = AppConstants.SEC_REQ_NAME)
    @GetMapping("/info")
    public ResponseEntity<UserResponse> getInfo() {
        return ResponseEntity.ok(authService.getUserInfo());
    }
}
