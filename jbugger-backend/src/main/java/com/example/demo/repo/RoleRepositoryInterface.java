package com.example.demo.repo;

import com.example.demo.entity.Permission;
import com.example.demo.entity.Role;
import com.example.demo.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepositoryInterface extends JpaRepository<Role, Long> {
    Optional<Role> findByType(RoleEnum type);
}
