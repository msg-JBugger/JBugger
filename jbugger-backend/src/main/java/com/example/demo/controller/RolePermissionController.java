package com.example.demo.controller;

import com.example.demo.enums.PermissionEnum;
import com.example.demo.enums.RoleEnum;
import com.example.demo.role_permission_call.AddPermissionToRoleResponse;
import com.example.demo.role_permission_call.RemovePermissionFromRoleResponse;
import com.example.demo.service.RolePermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/right")
@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
@RequiredArgsConstructor
public class RolePermissionController {
    private final RolePermissionService rolePermissionService;
    @PostMapping("/{role}/add/{permission}")
    public ResponseEntity<AddPermissionToRoleResponse> addPermissionToRole(
            @PathVariable RoleEnum role,
            @PathVariable PermissionEnum permission
    ) {
        return ResponseEntity.ok(rolePermissionService.addPermissionToRole(permission, role));
    }
    @DeleteMapping ("/{role}/delete/{permission}")
    public ResponseEntity<RemovePermissionFromRoleResponse> removePermissionFromRole(
            @PathVariable RoleEnum role,
            @PathVariable PermissionEnum permission
    ) {
        return ResponseEntity.ok(rolePermissionService.removePermissionFromRole(permission, role));
    }
}
