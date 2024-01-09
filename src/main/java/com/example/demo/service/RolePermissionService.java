package com.example.demo.service;

import com.example.demo.entity.Permission;
import com.example.demo.entity.Role;
import com.example.demo.enums.PermissionEnum;
import com.example.demo.enums.RoleEnum;
import com.example.demo.repo.PermissionRepositoryInterface;
import com.example.demo.repo.RoleRepositoryInterface;
import com.example.demo.role_permission_call.AddPermissionToRoleResponse;
import com.example.demo.role_permission_call.RemovePermissionFromRoleResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RolePermissionService {
    @Autowired
    private final PermissionRepositoryInterface permissionRepository;
    @Autowired
    private final RoleRepositoryInterface roleRepository;

    public AddPermissionToRoleResponse addPermissionToRole(PermissionEnum permission, RoleEnum role) {
        Optional<Permission> permissionEntity = permissionRepository.findByType(permission);
        Permission permissionResult = permissionEntity.orElseThrow(() -> new EntityNotFoundException("Permission not found"));
        Optional<Role> roleEntity =  roleRepository.findByType(role);
        Role roleResult = roleEntity.orElseThrow(() -> new EntityNotFoundException("Role not found"));
        roleResult.getPermissions().add(permissionResult);
        roleRepository.save(roleResult);
        return AddPermissionToRoleResponse.builder().permission(permission).role(role).build();
    }
    public RemovePermissionFromRoleResponse removePermissionFromRole(PermissionEnum permission, RoleEnum role) {
        Optional<Permission> permissionEntity = permissionRepository.findByType(permission);
        Permission permissionResult = permissionEntity.orElseThrow(() -> new EntityNotFoundException("Permission not found"));
        Optional<Role> roleEntity =  roleRepository.findByType(role);
        Role roleResult = roleEntity.orElseThrow(() -> new EntityNotFoundException("Role not found"));
        roleResult.getPermissions().remove(permissionResult);
        roleRepository.save(roleResult);
        return RemovePermissionFromRoleResponse.builder().permission(permission).role(role).build();
    }
}
