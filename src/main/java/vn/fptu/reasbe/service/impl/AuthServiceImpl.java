package vn.fptu.reasbe.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import vn.fptu.reasbe.model.exception.ResourceNotFoundException;
import vn.fptu.reasbe.repository.RoleRepository;
import vn.fptu.reasbe.repository.TokenRepository;
import vn.fptu.reasbe.repository.UserRepository;
import vn.fptu.reasbe.security.JwtTokenProvider;
import vn.fptu.reasbe.service.AuthService;
import vn.fptu.reasbe.service.EmailService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import vn.fptu.reasbe.service.OtpService;
import vn.fptu.reasbe.utils.mapper.UserMapper;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final OtpService otpService;
    private final UserMapper userMapper;
    private final JwtTokenProvider jwtTokenProvider;

    RestTemplate restTemplate = new RestTemplate();

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String googleClientSecret;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;

    @Value("${oauth2.google.token-uri}")
    private String googleTokenUri;

    @Value("${oauth2.google.user-info-uri}")
    private String googleUserInfoUri;

    @Value("${oauth2.google.auth-uri}")
    private String googleAuthUri;

    @Override
    public JWTAuthResponse authenticateUser(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUserNameOrEmailOrPhone(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = userRepository.findByUserNameOrEmailOrPhone(
                loginDto.getUserNameOrEmailOrPhone(),
                loginDto.getUserNameOrEmailOrPhone(),
                loginDto.getUserNameOrEmailOrPhone())
                .orElseThrow(() -> new ResourceNotFoundException("User"));
        String accessToken = jwtTokenProvider.generateAccessToken(user);
        String refreshToken = jwtTokenProvider.generateRefreshToken(user);

        revokeAllTokenByUser(user);
        saveUserToken(accessToken, refreshToken, user);
        return new JWTAuthResponse(accessToken, refreshToken);
    }

    @Override
    public String validateAndSendOtp(SignupDto dto) {
        validateUser(dto);
        return otpService.generateAndSendOtp(dto.getEmail(), dto.getFullName());
    }

    @Override
    public JWTAuthResponse signupVerifiedUser(SignupDto request) {
        Role userRole = roleRepository.findByName(RoleName.ROLE_RESIDENT)
                .orElseThrow(() -> new ReasApiException(HttpStatus.BAD_REQUEST, "Role does not exist"));
        User user = getUser(request);
        user.setRole(userRole);
        user = userRepository.save(user);

        String accessToken = jwtTokenProvider.generateAccessToken(user);
        String refreshToken = jwtTokenProvider.generateRefreshToken(user);
        saveUserToken(accessToken, refreshToken, user);

        sendMailToUser(user);
        return new JWTAuthResponse(accessToken, refreshToken);
    }

    @Override
    public String getGoogleLoginUrl() {
        return googleAuthUri + "?client_id=" +
                googleClientId + "&redirect_uri=" +
                redirectUri + "&response_type=code" +
                "&scope=openid%20email%20profile";
    }

    @Override
    @Transactional
    public JWTAuthResponse authenticateGoogleUser(String authorizationCode) {
        String googleAccessToken;
        Map<String, Object> userInfo;
        Map<String, Object> jsonData;

        jsonData = getGoogleJsonData(authorizationCode);

        googleAccessToken = getGoogleAccessToken(jsonData);

        userInfo = getGoogleUserData(googleAccessToken);

        return prepareUserInfoForAuthentication(userInfo);
    }

    private void sendMailToUser(User user) {
        String content = "<html>" +
                "<head>" +
                "<style>" +
                "table { width: 100%; border-collapse: collapse; }" +
                "th, td { padding: 10px; border: 1px solid #ddd; text-align: left; }" +
                "th { background-color: #f2f2f2; }" +
                "body { font-family: Arial, sans-serif; }" +
                ".highlight { font-weight: bold; color: red; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<p class='greeting'>Hi, " + user.getFullName() + ",</p>" +
                "<p>Your REAS account has been successfully created.</p>" +
                "<p>Please log into the system with the following information:</p>" +
                "<table>" + "<tr><th>Username</th><td>" + user.getEmail() + "</td></tr>" +
                "</table>" +
                "</body>" +
                "</html>";
        emailService.sendEmail(user.getEmail(), "[REAS] - Account Successfully Created", content);
    }

    @Override
    public UserResponse getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userRepository.findByUserNameOrEmailOrPhone(userName, userName, userName).orElseThrow(() -> new ResourceNotFoundException("User"));
        return userMapper.toUserResponse(user);
    }

    @Override
    public ResponseEntity<JWTAuthResponse> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith(AppConstants.AUTH_VALUE_PREFIX)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        // get user from token
        String token = authHeader.substring(7);
        String username = jwtTokenProvider.getUsernameFromJwt(token);
        User user = userRepository.findByUserNameOrEmailOrPhone(username, username, username).orElseThrow(() -> new RuntimeException("No user found"));

        // check if the token is valid then generate new token
        if (jwtTokenProvider.isValidRefreshToken(token, user.getUserName())) {
            String accessToken = jwtTokenProvider.generateAccessToken(user);
            String refreshToken = jwtTokenProvider.generateRefreshToken(user);

            revokeAllTokenByUser(user);
            saveUserToken(accessToken, refreshToken, user);

            return new ResponseEntity<>(new JWTAuthResponse(accessToken, refreshToken), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUserNameOrEmailOrPhone(username, username, username).orElseThrow(() -> new ReasApiException(HttpStatus.NOT_FOUND, "User cannot found!"));
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "Old password does not match!");
        }
        if (!newPassword.matches(AppConstants.PASSWORD_REGEX))
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "Password must have at least 8 characters with at least one uppercase letter, one number, and one special character (!@#$%^&*).");
        user.setPassword(passwordEncoder.encode(newPassword));
        if (user.isFirstLogin()) user.setFirstLogin(false);
        userRepository.save(user);
    }

    private JWTAuthResponse prepareUserInfoForAuthentication(Map<String, Object> userInfo) {
        User user = new User();
        String email = (String) userInfo.get("email");
        String fullName = (String) userInfo.get("name");
        String ggId = (String) userInfo.get("sub");
        String password = "Google:" + ggId;
        String username = email.substring(0, email.indexOf("@"));
        String image = (String) userInfo.get("picture");

        Optional<User> existingUser = userRepository.findByEmail(email);
        Role userRole = roleRepository.findByName(RoleName.ROLE_RESIDENT)
                .orElseThrow(() -> new ReasApiException(HttpStatus.BAD_REQUEST, "Role does not exist"));

        if (existingUser.isEmpty()) {
            user.setEmail(email);
            user.setFullName(fullName);
            user.setGoogleAccountId(ggId);
            user.setUserName(username);
            user.setPassword(passwordEncoder.encode(password));
            user.setImage(image);
            user.setFirstLogin(true);
            user.setGoogleAccountId(ggId);
            user.setRole(userRole);
            user = userRepository.save(user);
        } else {
            user = existingUser.get();
        }
        return authenticateUser(new LoginDto(user.getEmail(), password));
    }

    private Map<String, Object> getGoogleUserData(String accessToken) {
        Map<String, Object> userInfo;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> userInfoResponse = restTemplate.exchange(googleUserInfoUri, HttpMethod.GET, entity, Map.class);

        if (userInfoResponse.getStatusCode() == HttpStatus.OK) {
            userInfo = userInfoResponse.getBody();
        } else {
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "Failed to retrieve user info");
        }
        if (userInfo == null) {
            throw new ReasApiException(HttpStatus.NOT_FOUND, "Failed to retrieve user info: userInfo is null");
        } else return userInfo;
    }

    private String getGoogleAccessToken(Map<String, Object> jsonData) {
        if (jsonData != null) {
            return (String) jsonData.get("access_token");
        } else {
            throw new ReasApiException(HttpStatus.CONFLICT, "No access token retrieved from OAuth2");
        }
    }

    private Map<String, Object> getGoogleJsonData(String authorizationCode) {
        Map<String, String> tokenRequest = Map.of(
                "code", authorizationCode,
                "client_id", googleClientId,
                "client_secret", googleClientSecret,
                "redirect_uri", redirectUri,
                "grant_type", "authorization_code");

        return restTemplate.postForObject(googleTokenUri, tokenRequest, Map.class);
    }

    private void validateUser(SignupDto dto) {
        if (Boolean.TRUE.equals(userRepository.existsByEmail(dto.getEmail()))) {
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "Email is already exist!");
        }
    }

    private User getUser(SignupDto signupDto) {
        return User.builder()
                .email(signupDto.getEmail())
                .userName(signupDto.getEmail().substring(0, signupDto.getEmail().indexOf("@")))
                .fullName(signupDto.getFullName())
                .password(passwordEncoder.encode(signupDto.getPassword()))
                .image(null)
                .isFirstLogin(false)
                .build();
    }

    private void saveUserToken(String accessToken, String refreshToken, User user) {
        Token token = Token.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .loggedOut(false)
                .user(user)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllTokenByUser(User user) {
        List<Token> validTokens = tokenRepository.findAllByUserId(user.getId());
        validTokens.forEach(t -> t.setLoggedOut(true));
        tokenRepository.saveAll(validTokens);
    }
}
