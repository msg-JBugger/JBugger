package com.example.demo.seeding;

import com.example.demo.service.PermissionService;
import com.example.demo.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    RoleService roleService;

    @Autowired
    PermissionService permissionService;

    @Override
    public void run(String... args) throws Exception {
        loadPermissionData();
        loadRoleData();
    }

    private void loadPermissionData() {
        if (permissionService.findAllPermissions().size() == 0) {
            permissionService.initializePermissionRepository();
        }
    }

    private void loadRoleData() {
        if (roleService.findAllRoles().size() == 0) {
            roleService.initializeRoleRepository();
        }
    }
}