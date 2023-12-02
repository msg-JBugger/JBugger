package com.example.demo.service;

import com.example.demo.entity.Permission;
import com.example.demo.enums.PermissionEnum;
import com.example.demo.repo.PermissionRepositoryInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionService {
    @Autowired
    private final PermissionRepositoryInterface permissionRepository;

    public void initializePermissionRepository() {
        for (PermissionEnum permissionType: PermissionEnum.values()) {
            var permission = Permission.builder()
                    .type(permissionType)
                    .build();
            permissionRepository.save(permission);
        }
    }

    public List<Permission> findAllPermissions() {
        return permissionRepository.findAll();
    }
}
