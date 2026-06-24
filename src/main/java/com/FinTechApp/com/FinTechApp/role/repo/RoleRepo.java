package com.FinTechApp.com.FinTechApp.role.repo;

import com.FinTechApp.com.FinTechApp.role.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role, Long> {
    boolean existsByName(String name);
}