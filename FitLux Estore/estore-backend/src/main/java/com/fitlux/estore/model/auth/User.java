package com.fitlux.estore.model.auth;

import com.fitlux.estore.constants.serviceCodes.enums.UserStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "users")
public class User extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    private String firstName;
    private String lastName;
    private String phone;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    private LocalDateTime lastLoginAt;

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    private Set<UserRole>userRoles;

}
