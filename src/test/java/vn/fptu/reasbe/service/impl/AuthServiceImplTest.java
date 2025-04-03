package vn.fptu.reasbe.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import vn.fptu.reasbe.model.constant.AppConstants;
import vn.fptu.reasbe.model.dto.auth.JWTAuthResponse;
import vn.fptu.reasbe.model.dto.auth.LoginDto;
import vn.fptu.reasbe.model.dto.auth.SignupDto;
import vn.fptu.reasbe.model.entity.Role;
import vn.fptu.reasbe.model.entity.User;
import vn.fptu.reasbe.model.enums.core.StatusEntity;
import vn.fptu.reasbe.model.enums.user.RoleName;
import vn.fptu.reasbe.model.exception.ReasApiException;
import vn.fptu.reasbe.repository.RoleRepository;
import vn.fptu.reasbe.repository.TokenRepository;
import vn.fptu.reasbe.repository.UserRepository;
import vn.fptu.reasbe.security.JwtTokenProvider;
import vn.fptu.reasbe.service.EmailService;
import vn.fptu.reasbe.service.OtpService;
import vn.fptu.reasbe.service.mongodb.UserMService;
import vn.fptu.reasbe.utils.mapper.UserMapper;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

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
    private UserMService userMService;
    @Mock
    private EmailService emailService;
    @Mock
    private OtpService otpService;
    @Mock
    private UserMapper userMapper;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private AuthServiceImpl authService;

    private User resident;
    private Role residentRole;
    private SignupDto signupDto;
    private LoginDto loginDto;

    @BeforeEach
    void setUp() {
        // Prepare a dummy user and role

        residentRole = new Role();
        residentRole.setName(RoleName.ROLE_RESIDENT);

        resident = new User();
        resident.setId(1);
        resident.setEmail("test@example.com");
        resident.setUserName("testuser");
        resident.setFullName("Test User");
        resident.setPassword("encodedPassword");
        resident.setRole(residentRole);

        // IMPORTANT: The expected constructor order is (email, fullName, password)
        signupDto = new SignupDto("test@example.com", "Test User", "password123", List.of());
        loginDto = new LoginDto("test@example.com", "password123");

        // Prepare a dummy security context
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
    }

    // --- Test for authenticateUser() ---
    @Test
    void authenticateUser_Success() {
        Authentication auth = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
        when(userRepository.findByUserNameOrEmailOrPhone(anyString(), anyString(), anyString()))
                .thenReturn(Optional.of(resident));
        when(jwtTokenProvider.generateAccessToken(resident)).thenReturn("access-token");
        when(jwtTokenProvider.generateRefreshToken(resident)).thenReturn("refresh-token");

        JWTAuthResponse response = authService.authenticateUser(loginDto);

        assertNotNull(response);
        assertEquals("access-token", response.getAccessToken());
        assertEquals("refresh-token", response.getRefreshToken());
    }

    @Test
    void authenticateUser_EmailNotExist() {
        when(userRepository.findByUserNameOrEmailOrPhone(anyString(), anyString(), anyString())).thenReturn(Optional.empty());

        BadCredentialsException exception = assertThrows(BadCredentialsException.class, () ->
                authService.authenticateUser(loginDto));

        assertNotNull(exception);
        assertEquals("error.emailNotExist", exception.getMessage());
    }

    @Test
    void authenticateUser_WrongPassword() {
        when(userRepository.findByUserNameOrEmailOrPhone(anyString(), anyString(), anyString()))
                .thenReturn(Optional.of(resident));

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Incorrect password!"));

        BadCredentialsException exception = assertThrows(BadCredentialsException.class, () ->
                authService.authenticateUser(loginDto));

        assertNotNull(exception);
        assertEquals("error.incorrectPassword", exception.getMessage());
    }

    // --- Tests for validateAndSendOtp() ---
    @Test
    void validateAndSendOtp_Success() {
        when(userRepository.existsByEmailAndStatusEntityEquals(signupDto.getEmail(), StatusEntity.ACTIVE)).thenReturn(false);
        when(otpService.generateAndSendOtp(signupDto.getEmail(), signupDto.getFullName()))
                .thenReturn("123456");

        String otp = authService.validateAndSendOtp(signupDto);
        assertEquals("123456", otp);
    }

//    @Test
//    void validateAndSendOtp_EmailAlreadyExists() {
//        when(userRepository.existsByEmailAndStatusEntityEquals(signupDto.getEmail(), StatusEntity.ACTIVE));
//
//        ReasApiException exception = assertThrows(ReasApiException.class, () ->
//                authService.validateAndSendOtp(signupDto));
//        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
//        assertEquals("error.emailAlreadyExist", exception.getMessage());
//    }

    // --- Tests for signupVerifiedUser() ---
    @Test
    void signupVerifiedUser_Success() {
        // Simulate creation of a new user from signupDto
        User newUser = new User();
        newUser.setEmail(signupDto.getEmail());
        newUser.setUserName("test"); // Will be derived from email substring in getUser()
        newUser.setFullName(signupDto.getFullName());
        newUser.setPassword("encodedPass");
        newUser.setFirstLogin(false);
        newUser.setRole(residentRole);

        when(roleRepository.findByName(RoleName.ROLE_RESIDENT)).thenReturn(Optional.of(residentRole));
        when(passwordEncoder.encode(signupDto.getPassword())).thenReturn("encodedPass");
        when(userRepository.save(any(User.class))).thenReturn(newUser);
        when(jwtTokenProvider.generateAccessToken(newUser)).thenReturn("access-token");
        when(jwtTokenProvider.generateRefreshToken(newUser)).thenReturn("refresh-token");

        JWTAuthResponse response = authService.signupVerifiedUser(signupDto);
        assertNotNull(response);
        assertEquals("access-token", response.getAccessToken());
        assertEquals("refresh-token", response.getRefreshToken());

        // Verify that an email is sent to the new user
        verify(emailService).sendEmail(eq(newUser.getEmail()),
                eq("[REAS] - Account Successfully Created"), anyString());
    }

    @Test
    void signupVerifiedUser_RoleNotFound() {
        when(roleRepository.findByName(RoleName.ROLE_RESIDENT)).thenReturn(Optional.empty());
        ReasApiException exception = assertThrows(ReasApiException.class, () ->
                authService.signupVerifiedUser(signupDto));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("error.roleNotExist", exception.getMessage());
    }

    // --- Test for getUserInfo() ---
//    @Test
//    void getUserInfo_Success() {
//        SecurityContext securityContext = mock(SecurityContext.class);
//        Authentication auth = mock(Authentication.class);
//        when(securityContext.getAuthentication()).thenReturn(auth);
//        when(auth.getName()).thenReturn("test@example.com");
//        SecurityContextHolder.setContext(securityContext);
//        when(userRepository.findByUserName(anyString()))
//                .thenReturn(Optional.of(resident));
//
//        User user1 = authService.getCurrentUser();
//        assertNotNull(user1);
//    }

    // --- Tests for refreshToken() ---
    @Test
    void refreshToken_Success() {
        when(request.getHeader(HttpHeaders.AUTHORIZATION))
                .thenReturn(AppConstants.AUTH_VALUE_PREFIX + "access-token");
        when(jwtTokenProvider.getUsernameFromJwt("access-token")).thenReturn("test@example.com");
        when(userRepository.findByUserNameOrEmailOrPhone(anyString(), anyString(), anyString()))
                .thenReturn(Optional.of(resident));
        when(jwtTokenProvider.isValidRefreshToken("access-token", resident.getUserName())).thenReturn(true);
        when(jwtTokenProvider.generateAccessToken(resident)).thenReturn("new-access-token");
        when(jwtTokenProvider.generateRefreshToken(resident)).thenReturn("new-refresh-token");

        ResponseEntity<JWTAuthResponse> responseEntity = authService.refreshToken(request, response);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    void refreshToken_Unauthorized_InvalidHeader() {
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(null);
        ResponseEntity<JWTAuthResponse> responseEntity = authService.refreshToken(request, response);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    @Test
    void refreshToken_Unauthorized_InvalidToken() {
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("InvalidToken");
        ResponseEntity<JWTAuthResponse> responseEntity = authService.refreshToken(request, response);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    @Test
    void refreshToken_InvalidRefreshToken() {
        when(request.getHeader(HttpHeaders.AUTHORIZATION))
                .thenReturn(AppConstants.AUTH_VALUE_PREFIX + "access-token");
        when(jwtTokenProvider.getUsernameFromJwt("access-token")).thenReturn("test@example.com");
        when(userRepository.findByUserNameOrEmailOrPhone(anyString(), anyString(), anyString()))
                .thenReturn(Optional.of(resident));
        when(jwtTokenProvider.isValidRefreshToken("access-token", resident.getUserName())).thenReturn(false);

        ResponseEntity<JWTAuthResponse> responseEntity = authService.refreshToken(request, response);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    // --- Tests for changePassword() ---
    @Test
    void changePassword_Success() {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("testuser");
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByUserNameOrEmailOrPhone(anyString(), anyString(), anyString()))
                .thenReturn(Optional.of(resident));
        when(passwordEncoder.matches("oldPassword", resident.getPassword())).thenReturn(true);
        when(passwordEncoder.encode("NewPassword1!")).thenReturn("newEncodedPassword");

        authService.changePassword("oldPassword", "NewPassword1!");
        verify(userRepository).save(resident);
        assertEquals("newEncodedPassword", resident.getPassword());
    }

    @Test
    void changePassword_InvalidOldPassword() {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("testuser");
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByUserNameOrEmailOrPhone(anyString(), anyString(), anyString()))
                .thenReturn(Optional.of(resident));
        when(passwordEncoder.matches("wrongOldPassword", resident.getPassword())).thenReturn(false);

        ReasApiException exception = assertThrows(ReasApiException.class, () ->
                authService.changePassword("wrongOldPassword", "NewPassword1!"));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("error.oldPasswordNotMatch", exception.getMessage());
    }

    @Test
    void changePassword_InvalidNewPassword() {
        // New password doesn't match the regex criteria
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("testuser");
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByUserNameOrEmailOrPhone(anyString(), anyString(), anyString()))
                .thenReturn(Optional.of(resident));
        when(passwordEncoder.matches("oldPassword", resident.getPassword())).thenReturn(true);

        ReasApiException exception = assertThrows(ReasApiException.class, () ->
                authService.changePassword("oldPassword", "short"));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertTrue(exception.getMessage().contains("error"));
    }
}
