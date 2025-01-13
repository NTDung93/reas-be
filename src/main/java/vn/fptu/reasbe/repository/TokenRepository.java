package vn.fptu.reasbe.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.fptu.reasbe.model.entity.Token;


public interface TokenRepository extends JpaRepository<Token, Integer> {
    Optional<Token> findByAccessToken(String accessToken);
    Optional<Token> findByRefreshToken(String refreshToken);
    List<Token> findAllByUserId(Integer userId);
}
