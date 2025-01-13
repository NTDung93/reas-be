package vn.fptu.reasbe.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JWTAuthResponse> authenticationUser(@RequestBody @Valid LoginDto loginDto){
        JWTAuthResponse jwtAuthResponse = authService.authenticateUser(loginDto);
        HttpHeaders headers = new HttpHeaders();
        headers.add(AppConstants.AUTH_ATTR_NAME, AppConstants.AUTH_VALUE_PREFIX + jwtAuthResponse.getAccessToken());
        return new ResponseEntity<>(jwtAuthResponse, headers, HttpStatus.OK);
    }

    @PostMapping(value = "/register/user")
    public ResponseEntity<JWTAuthResponse> signupUser(@Valid @RequestBody SignupDto signupDto){
        JWTAuthResponse response = authService.signupUser(signupDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
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
