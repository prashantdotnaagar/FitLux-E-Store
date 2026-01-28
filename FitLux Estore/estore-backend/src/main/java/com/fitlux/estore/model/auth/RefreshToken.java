package com.fitlux.estore.model.auth;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "refresh_token")
@Getter
@Setter
@ToString
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;

    @Column(nullable = false, unique = true, length = 128)
    private String token;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Column(nullable = false)
    private boolean revoked = false;

    protected RefreshToken() {}

    public static RefreshToken create(User user, long validitySeconds) {
        RefreshToken rt = new RefreshToken();
        rt.user = user;
        rt.token = UUID.randomUUID().toString();
        rt.expiresAt = LocalDateTime.now().plusSeconds(validitySeconds);
        return rt;
    }
}

