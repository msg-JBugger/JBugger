package com.example.demo;

import com.example.demo.entity.Permission;
import com.example.demo.repo.PermissionRepositoryInterface;
import com.example.demo.service.PermissionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

@SpringBootTest
class PermissionServiceTest {

    @Autowired
    private PermissionRepositoryInterface permissionRepositoryInterface;


    @Autowired
    private PermissionService permissionService;

    @Test
    void testFindAllRoles() {
        List<Permission> expectedResult = permissionRepositoryInterface.findAll();


        List<Permission> actualResult = permissionService.findAllPermissions();


        assertArrayEquals(expectedResult.toArray(), actualResult.toArray());
    }



}
