package vn.fptu.reasbe.service.impl;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.fptu.reasbe.model.constant.AppConstants;
import vn.fptu.reasbe.model.dto.auth.JWTAuthResponse;
import vn.fptu.reasbe.model.dto.auth.LoginDto;
import vn.fptu.reasbe.model.dto.auth.SignupDto;
import vn.fptu.reasbe.model.dto.user.UserResponse;
import vn.fptu.reasbe.model.entity.Role;
import vn.fptu.reasbe.model.entity.Token;
import vn.fptu.reasbe.model.entity.User;
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
    private final UserMapper userMapper;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public JWTAuthResponse authenticateUser(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUserNameOrEmailOrPhone(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = userRepository.findByUserNameOrEmailOrPhone(loginDto.getUserNameOrEmailOrPhone(), loginDto.getUserNameOrEmailOrPhone(), loginDto.getUserNameOrEmailOrPhone())
                .orElseThrow(() -> new ResourceNotFoundException("User"));
        String accessToken = jwtTokenProvider.generateAccessToken(user);
        String refreshToken = jwtTokenProvider.generateRefreshToken(user);

        revokeAllTokenByUser(user);
        saveUserToken(accessToken, refreshToken, user);
        return new JWTAuthResponse(accessToken, refreshToken);
    }

    @Override
    public JWTAuthResponse signupUser(SignupDto signupDto) {
        User user = setUpUser(signupDto);
        Role userRole = roleRepository.findByName(AppConstants.ROLE_USER)
                .orElseThrow(() -> new ReasApiException(HttpStatus.BAD_REQUEST, "Role does not exist"));
        user.setRole(userRole);
        user.setPassword(passwordEncoder.encode(signupDto.getPassword()));
        user = userRepository.save(user);

        String accessToken = jwtTokenProvider.generateAccessToken(user);
        String refreshToken = jwtTokenProvider.generateRefreshToken(user);
        saveUserToken(accessToken, refreshToken, user);

        sendMailToUser(user);
        return new JWTAuthResponse(accessToken, refreshToken);
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
                "<table>" +
                "<tr><th>Username</th><td>" + user.getEmail() + "</td></tr>" +
                "</table>" +
                "</body>" +
                "</html>";
        emailService.sendEmail(user.getEmail(), "[REAS] - Account Successfully Created", content);
    }

    @Override
    public UserResponse getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userRepository.findByUserNameOrEmailOrPhone(userName, userName, userName)
                .orElseThrow(() -> new ResourceNotFoundException("User"));
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
        User user = userRepository.findByUserNameOrEmailOrPhone(username, username, username)
                .orElseThrow(() -> new RuntimeException("No user found"));

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
        User user = userRepository.findByUserNameOrEmailOrPhone(username, username, username)
                .orElseThrow(() -> new ReasApiException(HttpStatus.NOT_FOUND, "User cannot found!"));
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "Old password does not match!");
        }
        if (!newPassword.matches(AppConstants.PASSWORD_REGEX))
            throw new ReasApiException(HttpStatus.BAD_REQUEST,
                    "Password must have at least 8 characters with at least one uppercase letter, one number, and one special character (!@#$%^&*).");
        user.setPassword(passwordEncoder.encode(newPassword));
        if (user.isFirstLogin()) user.setFirstLogin(false);
        userRepository.save(user);
    }

    private User setUpUser(SignupDto signupDto) {
        if (Boolean.TRUE.equals(userRepository.existsByUserName(signupDto.getUsername()))) {
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "Username is already exist!");
        }
        if (Boolean.TRUE.equals(userRepository.existsByEmail(signupDto.getEmail()))) {
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "Email is already exist!");
        }
        return getUser(signupDto);
    }

    private static User getUser(SignupDto signupDto) {
        return User.builder()
                .userName(signupDto.getUsername())
                .email(signupDto.getEmail())
                .fullName(signupDto.getFullName())
                .password(signupDto.getPassword())
                .phone(signupDto.getPhone())
                .gender(signupDto.getGender())
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
