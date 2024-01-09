package com.example.demo.role_permission_call;

import com.example.demo.enums.PermissionEnum;
import com.example.demo.enums.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddPermissionToRoleResponse {
    RoleEnum role;
    PermissionEnum permission;
}
