package com.fitlux.estore.model.auth;

import com.fitlux.estore.constants.serviceCodes.enums.RoleCode;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "roles")
public class Role extends BaseEntity {
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private RoleCode roleCode;

    private String name;
    private String description;

    private boolean isActive;

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    private Set<UserRole> userRoles;

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    private Set<RolePermission> rolePermissions;
}
