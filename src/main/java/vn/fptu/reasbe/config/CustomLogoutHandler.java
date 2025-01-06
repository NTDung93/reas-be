package vn.fptu.reasbe.config;

import java.util.Optional;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import vn.fptu.reasbe.model.entity.Token;
import vn.fptu.reasbe.repository.TokenRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class CustomLogoutHandler implements LogoutHandler {
    private final TokenRepository tokenRepository;

    public CustomLogoutHandler(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {
        String authHeader = request.getHeader("Authorization");

        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        String token = authHeader.substring(7);
        Optional<Token> storedToken = tokenRepository.findByAccessToken(token);

        if(storedToken.isPresent()) {
            storedToken.get().setLoggedOut(true);
            tokenRepository.save(storedToken.get());
        }
    }
}
