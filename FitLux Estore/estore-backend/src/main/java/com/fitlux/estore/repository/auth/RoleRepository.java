package com.fitlux.estore.repository.auth;

import com.fitlux.estore.constants.serviceCodes.enums.RoleCode;
import com.fitlux.estore.model.auth.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRoleCode(RoleCode roleCode);

    boolean existsByRoleCode(RoleCode roleCode);
}
