package com.fitlux.estore.model.auth;

import com.fitlux.estore.constants.serviceCodes.enums.PermissionCode;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "permissions")
public class Permission extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private PermissionCode permissionCode;

    private String module;
    private String description;

    @OneToMany(mappedBy = "permission", fetch = FetchType.LAZY)
    private Set<RolePermission> rolePermissions;
}
