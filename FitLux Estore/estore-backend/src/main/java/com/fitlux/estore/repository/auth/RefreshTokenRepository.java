package com.fitlux.estore.repository.auth;

import com.fitlux.estore.model.auth.RefreshToken;
import com.fitlux.estore.model.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByTokenAndRevokedFalse(String token);

    boolean existsByUserAndRevokedFalseAndExpiresAtAfter(User user, java.time.LocalDateTime now);

    java.util.List<RefreshToken> findByUserAndRevokedFalse(User user);
}
