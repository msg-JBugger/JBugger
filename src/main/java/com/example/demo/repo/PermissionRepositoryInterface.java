package com.example.demo.repo;

import com.example.demo.entity.Permission;
import com.example.demo.enums.PermissionEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionRepositoryInterface extends JpaRepository<Permission, Long> {
    Optional<Permission> findByType(PermissionEnum type);
}
