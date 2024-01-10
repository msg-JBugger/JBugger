package com.example.demo;

import com.example.demo.entity.Role;
import com.example.demo.repo.RoleRepositoryInterface;
import com.example.demo.service.RoleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

@SpringBootTest
class RoleServiceTest {

    @Autowired
    private RoleRepositoryInterface roleRepository;


    @Autowired
    private RoleService roleService;

    @Test
    void testFindAllRoles() {
        List<Role> expectedResult = roleRepository.findAll();

        for (Role role: expectedResult
             ) {
            role.setPermissions(null);
        }
        List<Role> actualResult = roleService.findAllRoles();

        for (Role role: actualResult
        ) {
            role.setPermissions(null);
        }

        assertArrayEquals(expectedResult.toArray(), actualResult.toArray());
    }



}
