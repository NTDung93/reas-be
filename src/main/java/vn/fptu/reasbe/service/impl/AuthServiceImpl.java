package vn.fptu.reasbe.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
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
import vn.fptu.reasbe.model.entity.Role;
import vn.fptu.reasbe.model.entity.Token;
import vn.fptu.reasbe.model.entity.User;
import vn.fptu.reasbe.model.enums.user.RoleName;
import vn.fptu.reasbe.model.enums.user.StatusOnline;
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
import vn.fptu.reasbe.service.mongodb.UserMService;

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
    private final UserMService userMService;
    private final OtpService otpService;
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
    public JWTAuthResponse authenticateResident(LoginDto loginDto) {
        User user = findUserByEmailOrUsernameOrPhone(loginDto.getUserNameOrEmailOrPhone());

        if (user.getRole().getName().equals(RoleName.ROLE_ADMIN) || user.getRole().getName().equals(RoleName.ROLE_STAFF)) {
            throw new AccessDeniedException("error.notAuthorizedForResidentLogin");
        }

        return authenticateUser(user, loginDto);
    }

    @Override
    public JWTAuthResponse authenticateAdminOrStaff(LoginDto loginDto) {
        User user = findUserByEmailOrUsernameOrPhone(loginDto.getUserNameOrEmailOrPhone());

        if (user.getRole().getName().equals(RoleName.ROLE_RESIDENT)) {
            throw new AccessDeniedException("error.notAuthorizedForAdminLogin");
        }

        return authenticateUser(user, loginDto);
    }

    private JWTAuthResponse authenticateUser(User user, LoginDto dto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getUserNameOrEmailOrPhone(), dto.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String accessToken = jwtTokenProvider.generateAccessToken(user);
            String refreshToken = jwtTokenProvider.generateRefreshToken(user);

            revokeAllTokenByUser(user);
            saveUserToken(accessToken, refreshToken, user);

            return new JWTAuthResponse(accessToken, refreshToken);

        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("error.incorrectPassword");
        }
    }

    @Override
    public String validateAndSendOtp(SignupDto dto) {
        validateUser(dto);
        return otpService.generateAndSendOtp(dto.getEmail(), dto.getFullName());
    }

    @Override
    public JWTAuthResponse signupVerifiedUser(SignupDto request) {
        Role userRole = roleRepository.findByName(RoleName.ROLE_RESIDENT)
                .orElseThrow(() -> new ReasApiException(HttpStatus.BAD_REQUEST, "error.roleNotExist"));
        validateUser(request);
        User user = prepareUserData(request);
        user.setRole(userRole);
        user = userRepository.save(user);

        // save user to MongoDB
        vn.fptu.reasbe.model.mongodb.User userM = vn.fptu.reasbe.model.mongodb.User.builder()
                .refId(user.getId())
                .userName(user.getUserName())
                .fullName(user.getFullName())
                .statusOnline(StatusOnline.OFFLINE)
                .build();
        userMService.saveUser(userM);

        // save user token
        String accessToken = jwtTokenProvider.generateAccessToken(user);
        String refreshToken = jwtTokenProvider.generateRefreshToken(user);
        saveUserToken(accessToken, refreshToken, user);

        // send email to user
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

        if (userInfo == null){
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "error.googleUserInfoNotFound");
        }

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
    public User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUserName(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
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
        User user = userRepository.findByUserNameOrEmailOrPhone(username, username, username).orElseThrow(() -> new RuntimeException("error.userNotFound"));

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
        User user = userRepository.findByUserNameOrEmailOrPhone(username, username, username).orElseThrow(() -> new ReasApiException(HttpStatus.NOT_FOUND, "error.userNotFound"));
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "error.oldPasswordNotMatch");
        }
        if (!newPassword.matches(AppConstants.PASSWORD_REGEX))
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "error.passwordNotMatchRegex");
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
                .orElseThrow(() -> new ReasApiException(HttpStatus.BAD_REQUEST, "error.roleNotExist"));

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
        return authenticateUser(user, new LoginDto(user.getEmail(), password));
    }

    private Map<String, Object> getGoogleUserData(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> userInfoResponse = restTemplate.exchange(googleUserInfoUri, HttpMethod.GET, entity, Map.class);

        if (userInfoResponse.getStatusCode() != HttpStatus.OK || userInfoResponse.getBody() == null) {
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "error.googleUserInfoNotFound");
        }

        return userInfoResponse.getBody();
    }

    private String getGoogleAccessToken(Map<String, Object> jsonData) {
        if (jsonData != null) {
            return (String) jsonData.get("access_token");
        } else {
            throw new ReasApiException(HttpStatus.CONFLICT, "error.googleAccessTokenNotFound");
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

    private User findUserByEmailOrUsernameOrPhone(String userLogin){
        return userRepository.findByUserNameOrEmailOrPhone(
                userLogin,
                userLogin,
                userLogin
        ).orElseThrow(() -> new BadCredentialsException("error.emailNotExist"));
    }

    private void validateUser(SignupDto dto) {
        if (Boolean.TRUE.equals(userRepository.existsByEmail(dto.getEmail()))) {
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "error.emailAlreadyExist");
        }
    }

    private User prepareUserData(SignupDto signupDto) {
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
