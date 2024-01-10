package com.example.demo;

import com.example.demo.entity.User;
import com.example.demo.repo.NotificationRepositoryInterface;
import com.example.demo.repo.UserRepositoryInterface;
import com.example.demo.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    NotificationRepositoryInterface notificationRepositoryInterface;

    @Autowired
    UserRepositoryInterface userRepositoryInterface;

    @Test
    public void testDeactivate(){
        User mockUser = new User();
        String username = "testUsername";
        mockUser.setUsername(username);
        mockUser.setEnabled(true);
        mockUser.setUserId(1L);
        userRepositoryInterface.save(mockUser);

        userService.deactivate(username);

        Optional<User> result = userRepositoryInterface.findByUsername(username);
        assertFalse(result.get().isEnabled());
        userRepositoryInterface.deleteAll();
        notificationRepositoryInterface.deleteAll();
    }

//    @Test
//    public void testUpdate(){
//        String username = "testUsername";
//        String username2 = "testUsername2";
//        User mockUser = new User();
//        User mockUser2 = new User();
//        List<String> mockRoles = new ArrayList<>();
//        mockRoles.add("ROLE_ADMIN");
//        UpdateRequest mockUpdateRequest = new UpdateRequest("new mobile", "new mail", mockRoles);
//        mockUser.setUserId(1L);
//        mockUser.setUsername(username);
//        mockUser2.setUsername(username2);
//        userRepositoryInterface.save(mockUser);
//        userRepositoryInterface.save(mockUser2);
//        userService.update(username, mockUpdateRequest, username2);
//
//        Optional<User> updatedUser = userRepositoryInterface.findByUsername(username);
//        assertEquals("new mobile", updatedUser.get().getMobileNumber());
//        assertEquals("new mail", updatedUser.get().getEmail());
//        assertNull(updatedUser.get().getRoles());
//
//        userRepositoryInterface.deleteAll();
//    }
}
