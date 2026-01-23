package com.fitlux.estore.repository.auth;

import com.fitlux.estore.model.auth.User;
import com.fitlux.estore.model.auth.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    List<UserRole> findByUserAndIsActiveTrue(User user);

    Optional<UserRole> findByUserIdAndRoleId(Long userId, Long roleId);

    boolean existsByUserIdAndRoleId(Long userId, Long roleId);
}
