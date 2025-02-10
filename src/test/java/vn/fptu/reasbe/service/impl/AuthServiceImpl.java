package vn.fptu.reasbe.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import vn.fptu.reasbe.model.constant.AppConstants;
import vn.fptu.reasbe.model.dto.auth.JWTAuthResponse;
import vn.fptu.reasbe.model.dto.auth.LoginDto;
import vn.fptu.reasbe.model.dto.auth.SignupDto;
import vn.fptu.reasbe.model.dto.otp.OtpVerificationRequest;
import vn.fptu.reasbe.model.entity.Role;
import vn.fptu.reasbe.model.entity.User;
import vn.fptu.reasbe.model.exception.ReasApiException;
import vn.fptu.reasbe.model.exception.ResourceNotFoundException;
import vn.fptu.reasbe.repository.RoleRepository;
import vn.fptu.reasbe.repository.TokenRepository;
import vn.fptu.reasbe.repository.UserRepository;
import vn.fptu.reasbe.security.JwtTokenProvider;
import vn.fptu.reasbe.service.EmailService;
import vn.fptu.reasbe.service.OtpService;
import vn.fptu.reasbe.utils.mapper.UserMapper;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceImplTest {

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailService emailService;

    @Mock
    private OtpService otpService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    private SignupDto signupDto;
    private User user;
    private Role role;
    private LoginDto loginDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        signupDto = new SignupDto("testUser", "test@example.com", "Test User", "Password123!", "123456789", "Male");
        user = User.builder()
                .userName("testUser")
                .email("test@example.com")
                .fullName("Test User")
                .password("EncodedPassword")
                .phone("123456789")
                .gender("Male")
                .build();
        user.setId(1);

        role = new Role(AppConstants.ROLE_USER, Set.of(user));
        loginDto = new LoginDto("test@example.com", "Password123!");
    }

    @Test
    void authenticateUser_Success() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mock(Authentication.class));
        when(userRepository.findByUserNameOrEmailOrPhone(anyString(), anyString(), anyString()))
                .thenReturn(Optional.of(user));
        when(jwtTokenProvider.generateAccessToken(user)).thenReturn("accessToken");
        when(jwtTokenProvider.generateRefreshToken(user)).thenReturn("refreshToken");

        JWTAuthResponse response = authService.authenticateUser(loginDto);

        assertNotNull(response);
        assertEquals("accessToken", response.getAccessToken());
        assertEquals("refreshToken", response.getRefreshToken());
        verify(tokenRepository).save(any());
    }

    @Test
    void authenticateUser_UserNotFound() {
        when(userRepository.findByUserNameOrEmailOrPhone(anyString(), anyString(), anyString()))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> authService.authenticateUser(loginDto));
        assertEquals("User not found!", exception.getMessage());
    }

    @Test
    void prepareUserForOtp_Success() {
        when(otpService.generateAndStoreOtp(signupDto)).thenReturn(true);

        assertDoesNotThrow(() -> authService.prepareUserForOtp(signupDto));
    }

    @Test
    void prepareUserForOtp_Failure() {
        when(otpService.generateAndStoreOtp(signupDto)).thenReturn(false);

        ReasApiException exception = assertThrows(ReasApiException.class, () -> authService.prepareUserForOtp(signupDto));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatus());
        assertEquals("Error generating and store OTP!", exception.getMessage());
    }

    @Test
    void signupVerifiedUser_Success() {
        OtpVerificationRequest request = new OtpVerificationRequest("test@example.com", "123456");
        when(otpService.verifyOtp(request)).thenReturn(signupDto);
        when(roleRepository.findByName(AppConstants.ROLE_USER)).thenReturn(Optional.of(role));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtTokenProvider.generateAccessToken(user)).thenReturn("accessToken");
        when(jwtTokenProvider.generateRefreshToken(user)).thenReturn("refreshToken");

        JWTAuthResponse response = authService.signupVerifiedUser(request);

        assertNotNull(response);
        assertEquals("accessToken", response.getAccessToken());
        assertEquals("refreshToken", response.getRefreshToken());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void signupVerifiedUser_InvalidOtp() {
        OtpVerificationRequest request = new OtpVerificationRequest("test@example.com", "invalidOtp");
        when(otpService.verifyOtp(request)).thenThrow(new ReasApiException(HttpStatus.BAD_REQUEST, "Invalid OTP"));

        ReasApiException exception = assertThrows(ReasApiException.class, () -> authService.signupVerifiedUser(request));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Invalid OTP", exception.getMessage());
    }

    @Test
    void changePassword_Success() {
        mockSecurityContext();
        when(userRepository.findByUserNameOrEmailOrPhone(anyString(), anyString(), anyString()))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        assertDoesNotThrow(() -> authService.changePassword("Password123!", "NewPassword1@"));
        verify(userRepository).save(any(User.class));
    }

    @Test
    void changePassword_InvalidOldPassword() {
        mockSecurityContext();
        when(userRepository.findByUserNameOrEmailOrPhone(anyString(), anyString(), anyString()))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        ReasApiException exception = assertThrows(ReasApiException.class, () -> authService.changePassword("WrongPassword", "NewPassword1@"));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Old password does not match!", exception.getMessage());
    }

    @Test
    void changePassword_InvalidNewPassword() {
        mockSecurityContext();
        when(userRepository.findByUserNameOrEmailOrPhone(anyString(), anyString(), anyString()))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        ReasApiException exception = assertThrows(ReasApiException.class, () -> authService.changePassword("Password123!", "weak"));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertTrue(exception.getMessage().contains("Password must have at least"));
    }

    private void mockSecurityContext() {
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testUser");
        SecurityContextHolder.setContext(securityContext);
    }
}
