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
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import vn.fptu.reasbe.model.constant.AppConstants;
import vn.fptu.reasbe.model.dto.auth.JWTAuthResponse;
import vn.fptu.reasbe.model.dto.auth.LoginDto;
import vn.fptu.reasbe.model.dto.auth.SignupDto;
import vn.fptu.reasbe.model.dto.user.UserResponse;
import vn.fptu.reasbe.model.entity.Role;
import vn.fptu.reasbe.model.entity.Token;
import vn.fptu.reasbe.model.entity.User;
import vn.fptu.reasbe.model.enums.user.RoleName;
import vn.fptu.reasbe.model.exception.ReasApiException;
import vn.fptu.reasbe.repository.RoleRepository;
import vn.fptu.reasbe.repository.TokenRepository;
import vn.fptu.reasbe.repository.UserRepository;
import vn.fptu.reasbe.security.JwtTokenProvider;
import vn.fptu.reasbe.service.EmailService;
import vn.fptu.reasbe.service.OtpService;
import vn.fptu.reasbe.utils.mapper.UserMapper;

import java.util.HashMap;
import java.util.Map;
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

    private User user;
    private Role role;
    private SignupDto signupDto;
    private LoginDto loginDto;

    @BeforeEach
    void setUp() {
        // Prepare a dummy user and role
        user = new User();
        user.setId(1);
        user.setEmail("test@example.com");
        user.setUserName("testuser");
        user.setFullName("Test User");
        user.setPassword("encodedPassword");

        role = new Role();
        role.setName(RoleName.ROLE_RESIDENT);

        // IMPORTANT: The expected constructor order is (email, fullName, password)
        signupDto = new SignupDto("test@example.com", "Test User", "password123");
        loginDto = new LoginDto("test@example.com", "password123");

        // Prepare a dummy security context
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        // Set up @Value fields via ReflectionTestUtils
        ReflectionTestUtils.setField(authService, "googleClientId", "myClientId");
        ReflectionTestUtils.setField(authService, "googleClientSecret", "myClientSecret");
        ReflectionTestUtils.setField(authService, "redirectUri", "http://localhost/redirect");
    }

    // --- Test for authenticateUser() ---
    @Test
    void authenticateUser_Success() {
        Authentication auth = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
        when(userRepository.findByUserNameOrEmailOrPhone(anyString(), anyString(), anyString()))
                .thenReturn(Optional.of(user));
        when(jwtTokenProvider.generateAccessToken(user)).thenReturn("access-token");
        when(jwtTokenProvider.generateRefreshToken(user)).thenReturn("refresh-token");

        JWTAuthResponse response = authService.authenticateUser(loginDto);

        assertNotNull(response);
        assertEquals("access-token", response.getAccessToken());
        assertEquals("refresh-token", response.getRefreshToken());
    }

    // --- Tests for validateAndSendOtp() ---
    @Test
    void validateAndSendOtp_Success() {
        when(userRepository.existsByEmail(signupDto.getEmail())).thenReturn(false);
        when(otpService.generateAndSendOtp(signupDto.getEmail(), signupDto.getFullName()))
                .thenReturn("123456");

        String otp = authService.validateAndSendOtp(signupDto);
        assertEquals("123456", otp);
    }

    @Test
    void validateAndSendOtp_EmailAlreadyExists() {
        when(userRepository.existsByEmail(signupDto.getEmail())).thenReturn(true);

        ReasApiException exception = assertThrows(ReasApiException.class, () ->
                authService.validateAndSendOtp(signupDto));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Email is already exist!", exception.getMessage());
    }

    // --- Tests for signupVerifiedUser() ---
    @Test
    void signupVerifiedUser_Success() {
        when(roleRepository.findByName(RoleName.ROLE_RESIDENT)).thenReturn(Optional.of(role));
        // Simulate creation of a new user from signupDto
        User newUser = new User();
        newUser.setEmail(signupDto.getEmail());
        newUser.setUserName("test"); // Will be derived from email substring in getUser()
        newUser.setFullName(signupDto.getFullName());
        newUser.setPassword("encodedPass");
        newUser.setFirstLogin(false);
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
        assertEquals("Role does not exist", exception.getMessage());
    }

    // --- Test for getGoogleLoginUrl() ---
    @Test
    void getGoogleLoginUrl() {
        String url = authService.getGoogleLoginUrl();
        assertTrue(url.contains("myClientId"));
        assertTrue(url.contains("http://localhost/redirect"));
        assertTrue(url.contains("response_type=code"));
    }

    // --- Tests for authenticateGoogleUser() using MockedConstruction for RestTemplate ---
    @Test
    void authenticateGoogleUser_JsonDataNull() {
        try (MockedConstruction<RestTemplate> mocked = mockConstruction(RestTemplate.class, (mock, context) -> {
            when(mock.postForObject(eq("https://oauth2.googleapis.com/token"), any(), eq(Map.class)))
                    .thenReturn(null);
        })) {
            ReasApiException exception = assertThrows(ReasApiException.class, () ->
                    authService.authenticateGoogleUser("authCode"));
            assertEquals(HttpStatus.CONFLICT, exception.getStatus());
            assertEquals("No access token retrieved from OAuth2", exception.getMessage());
        }
    }

    @Test
    void authenticateGoogleUser_UserInfoResponseNotOk() {
        Map<String, Object> jsonData = new HashMap<>();
        jsonData.put("access_token", "google-access-token");

        try (MockedConstruction<RestTemplate> mocked = mockConstruction(RestTemplate.class, (mock, context) -> {
            when(mock.postForObject(eq("https://oauth2.googleapis.com/token"), any(), eq(Map.class)))
                    .thenReturn(jsonData);
            ResponseEntity<Map> badResponse = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            when(mock.exchange(eq("https://www.googleapis.com/oauth2/v3/userinfo"),
                    eq(HttpMethod.GET), any(), eq(Map.class))).thenReturn(badResponse);
        })) {
            ReasApiException exception = assertThrows(ReasApiException.class, () ->
                    authService.authenticateGoogleUser("authCode"));
            assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
            assertEquals("Failed to retrieve user info", exception.getMessage());
        }
    }

    @Test
    void authenticateGoogleUser_UserInfoNull() {
        Map<String, Object> jsonData = new HashMap<>();
        jsonData.put("access_token", "google-access-token");

        try (MockedConstruction<RestTemplate> mocked = mockConstruction(RestTemplate.class, (mock, context) -> {
            when(mock.postForObject(eq("https://oauth2.googleapis.com/token"), any(), eq(Map.class)))
                    .thenReturn(jsonData);
            ResponseEntity<Map> okResponse = new ResponseEntity<>(null, HttpStatus.OK);
            when(mock.exchange(eq("https://www.googleapis.com/oauth2/v3/userinfo"),
                    eq(HttpMethod.GET), any(), eq(Map.class))).thenReturn(okResponse);
        })) {
            ReasApiException exception = assertThrows(ReasApiException.class, () ->
                    authService.authenticateGoogleUser("authCode"));
            assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
            assertEquals("Failed to retrieve user info: userInfo is null", exception.getMessage());
        }
    }

    @Test
    void authenticateGoogleUser_NewUserCreation() {
        // Prepare user info map returned by Google
        Map<String, Object> userInfoMap = new HashMap<>();
        userInfoMap.put("email", "google@example.com");
        userInfoMap.put("name", "Google User");
        userInfoMap.put("sub", "12345");
        userInfoMap.put("picture", "imageUrl");

        Map<String, Object> jsonData = new HashMap<>();
        jsonData.put("access_token", "google-access-token");

        // Create a new user to be returned upon save
        User googleUser = new User();
        googleUser.setId(2);
        googleUser.setEmail("google@example.com");

        try (MockedConstruction<RestTemplate> mocked = mockConstruction(RestTemplate.class, (mock, context) -> {
            when(mock.postForObject(eq("https://oauth2.googleapis.com/token"), any(), eq(Map.class)))
                    .thenReturn(jsonData);
            ResponseEntity<Map> okResponse = new ResponseEntity<>(userInfoMap, HttpStatus.OK);
            when(mock.exchange(eq("https://www.googleapis.com/oauth2/v3/userinfo"),
                    eq(HttpMethod.GET), any(), eq(Map.class))).thenReturn(okResponse);
        })) {
            // Simulate that the user does not exist yet
            when(userRepository.findByEmail("google@example.com")).thenReturn(Optional.empty());
            when(roleRepository.findByName(RoleName.ROLE_RESIDENT)).thenReturn(Optional.of(role));
            when(passwordEncoder.encode("Google:" + "12345")).thenReturn("encoded-google-pass");
            when(userRepository.save(any(User.class))).thenReturn(googleUser);

            // For the inner call to authenticateUser, stub dependencies:
            Authentication auth = mock(Authentication.class);
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
            when(userRepository.findByUserNameOrEmailOrPhone(eq("google@example.com"), any(), any())).
                    thenReturn(Optional.of(googleUser));
            when(jwtTokenProvider.generateAccessToken(googleUser)).thenReturn("access-token-new");
            when(jwtTokenProvider.generateRefreshToken(googleUser)).thenReturn("refresh-token-new");

            JWTAuthResponse response = authService.authenticateGoogleUser("authCode");
            assertNotNull(response);
            assertEquals("access-token-new", response.getAccessToken());
            assertEquals("refresh-token-new", response.getRefreshToken());
        }
    }

    @Test
    void authenticateGoogleUser_ExistingUser() {
        // Prepare user info map returned by Google
        Map<String, Object> userInfoMap = new HashMap<>();
        userInfoMap.put("email", "google@example.com");
        userInfoMap.put("name", "Google User");
        userInfoMap.put("sub", "12345");
        userInfoMap.put("picture", "imageUrl");

        Map<String, Object> jsonData = new HashMap<>();
        jsonData.put("access_token", "google-access-token");

        // An existing user in the system
        User existingUser = new User();
        existingUser.setId(3);
        existingUser.setEmail("google@example.com");

        try (MockedConstruction<RestTemplate> mocked = mockConstruction(RestTemplate.class, (mock, context) -> {
            when(mock.postForObject(eq("https://oauth2.googleapis.com/token"), any(), eq(Map.class)))
                    .thenReturn(jsonData);
            ResponseEntity<Map> okResponse = new ResponseEntity<>(userInfoMap, HttpStatus.OK);
            when(mock.exchange(eq("https://www.googleapis.com/oauth2/v3/userinfo"),
                    eq(HttpMethod.GET), any(), eq(Map.class))).thenReturn(okResponse);
        })) {
            // Simulate that the user already exists
            when(userRepository.findByEmail("google@example.com")).thenReturn(Optional.of(existingUser));
            // IMPORTANT: Stub role lookup so that the role exists
            when(roleRepository.findByName(RoleName.ROLE_RESIDENT)).thenReturn(Optional.of(role));
            // For the inner call to authenticateUser, stub dependencies:
            Authentication auth = mock(Authentication.class);
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
            when(userRepository.findByUserNameOrEmailOrPhone(eq("google@example.com"), any(), any())).
                    thenReturn(Optional.of(existingUser));
            when(jwtTokenProvider.generateAccessToken(existingUser)).thenReturn("access-token-existing");
            when(jwtTokenProvider.generateRefreshToken(existingUser)).thenReturn("refresh-token-existing");

            JWTAuthResponse response = authService.authenticateGoogleUser("authCode");
            assertNotNull(response);
            assertEquals("access-token-existing", response.getAccessToken());
            assertEquals("refresh-token-existing", response.getRefreshToken());
        }
    }

    // --- Test for getUserInfo() ---
    @Test
    void getUserInfo_Success() {
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication auth = mock(Authentication.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        when(auth.getName()).thenReturn("test@example.com");
        SecurityContextHolder.setContext(securityContext);
        when(userRepository.findByUserNameOrEmailOrPhone(anyString(), anyString(), anyString()))
                .thenReturn(Optional.of(user));
        UserResponse userResponse = new UserResponse();
        when(userMapper.toUserResponse(user)).thenReturn(userResponse);

        UserResponse response = authService.getUserInfo();
        assertNotNull(response);
    }

    // --- Tests for refreshToken() ---
    @Test
    void refreshToken_Success() {
        when(request.getHeader(HttpHeaders.AUTHORIZATION))
                .thenReturn(AppConstants.AUTH_VALUE_PREFIX + "access-token");
        when(jwtTokenProvider.getUsernameFromJwt("access-token")).thenReturn("test@example.com");
        when(userRepository.findByUserNameOrEmailOrPhone(anyString(), anyString(), anyString()))
                .thenReturn(Optional.of(user));
        when(jwtTokenProvider.isValidRefreshToken("access-token", user.getUserName())).thenReturn(true);
        when(jwtTokenProvider.generateAccessToken(user)).thenReturn("new-access-token");
        when(jwtTokenProvider.generateRefreshToken(user)).thenReturn("new-refresh-token");

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
                .thenReturn(Optional.of(user));
        when(jwtTokenProvider.isValidRefreshToken("access-token", user.getUserName())).thenReturn(false);

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
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches("oldPassword", user.getPassword())).thenReturn(true);
        when(passwordEncoder.encode("NewPassword1!")).thenReturn("newEncodedPassword");

        authService.changePassword("oldPassword", "NewPassword1!");
        verify(userRepository).save(user);
        assertEquals("newEncodedPassword", user.getPassword());
    }

    @Test
    void changePassword_InvalidOldPassword() {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("testuser");
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByUserNameOrEmailOrPhone(anyString(), anyString(), anyString()))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongOldPassword", user.getPassword())).thenReturn(false);

        ReasApiException exception = assertThrows(ReasApiException.class, () ->
                authService.changePassword("wrongOldPassword", "NewPassword1!"));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Old password does not match!", exception.getMessage());
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
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches("oldPassword", user.getPassword())).thenReturn(true);

        ReasApiException exception = assertThrows(ReasApiException.class, () ->
                authService.changePassword("oldPassword", "short"));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertTrue(exception.getMessage().contains("Password must have at least 8 characters"));
    }
}
