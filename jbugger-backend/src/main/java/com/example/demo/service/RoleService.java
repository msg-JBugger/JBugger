package com.example.demo.service;


import com.example.demo.entity.Permission;
import com.example.demo.entity.Role;

import com.example.demo.enums.PermissionEnum;
import com.example.demo.enums.RoleEnum;
import com.example.demo.repo.PermissionRepositoryInterface;
import com.example.demo.repo.RoleRepositoryInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepositoryInterface roleRepository;
    private final PermissionRepositoryInterface permissionRepository;

    public List<Role> findAllRoles() {
        return roleRepository.findAll();
    }

    public void initializeRoleRepository() {
        for (RoleEnum roleType : RoleEnum.values()) {
            var role = Role.builder()
                    .type(roleType)
                    .permissions(generatePermissions(roleType.toString()))
                    .build();
            roleRepository.save(role);
        }
    }

    private Set<Permission> generatePermissions(String roleType) {
        switch (roleType) {
            case "ADM" -> {
                return generateAdminPermissions();
            }
            case "PM" -> {
                return generateProjectManagerPermissions();
            }
            case "TM" -> {
                return generateTestManagerPermissions();
            }
            case "DEV" -> {
                return generateDeveloperPermissions();
            }
            case "TEST" -> {
                return generateTesterPermissions();
            }
        }
        return Collections.emptySet();
    }

    private Set<Permission> generateAdminPermissions() {
        Set<Permission> permissionSet = new HashSet<>();
        permissionSet.add(
                permissionRepository.findByType(PermissionEnum.USER_MANAGEMENT)
                        .orElseThrow(() -> new RuntimeException("User Management Permission not found"))
        );
        permissionSet.add(
                permissionRepository.findByType(PermissionEnum.BUG_MANAGEMENT)
                        .orElseThrow(() -> new RuntimeException("Bug Management Permission not found"))
        );
        permissionSet.add(
                permissionRepository.findByType(PermissionEnum.BUG_CLOSE)
                        .orElseThrow(() -> new RuntimeException("Bug Close Permission not found"))
        );
        permissionSet.add(
                permissionRepository.findByType(PermissionEnum.PERMISSION_MANAGEMENT)
                        .orElseThrow(() -> new RuntimeException("Management Permission not found"))
        );
        return permissionSet;
    }

    private Set<Permission> generateProjectManagerPermissions() {
        Set<Permission> permissionSet = new HashSet<>();
        permissionSet.add(
                permissionRepository.findByType(PermissionEnum.USER_MANAGEMENT)
                        .orElseThrow(() -> new RuntimeException("User Management Permission not found"))
        );
        return permissionSet;
    }

    private Set<Permission> generateTestManagerPermissions() {
        Set<Permission> permissionSet = new HashSet<>();
        permissionSet.add(
                permissionRepository.findByType(PermissionEnum.BUG_MANAGEMENT)
                        .orElseThrow(() -> new RuntimeException("Bug Management Permission not found"))
        );
        permissionSet.add(
                permissionRepository.findByType(PermissionEnum.BUG_CLOSE)
                        .orElseThrow(() -> new RuntimeException("Bug Close Permission not found"))
        );
        return permissionSet;
    }

    private Set<Permission> generateDeveloperPermissions() {
        Set<Permission> permissionSet = new HashSet<>();
        permissionSet.add(
                permissionRepository.findByType(PermissionEnum.BUG_MANAGEMENT)
                        .orElseThrow(() -> new RuntimeException("Bug Management Permission not found"))
        );
        return permissionSet;
    }

    private Set<Permission> generateTesterPermissions() {
        Set<Permission> permissionSet = new HashSet<>();
        permissionSet.add(
                permissionRepository.findByType(PermissionEnum.BUG_CLOSE)
                        .orElseThrow(() -> new RuntimeException("Bug Close Permission not found"))
        );
        return permissionSet;
    }
}
