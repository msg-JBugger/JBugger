package com.example.demo;

import com.example.demo.entity.Notification;
import com.example.demo.entity.User;
import com.example.demo.factory.NotificationFactory;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static com.example.demo.enums.NotificationEnum.WELCOME_NEW_USER;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FactoryTest {

    @Test
    public void testCreateWelcomeNotification(){

        User testUser = new User();
        Notification expectedResult = new Notification();
        testUser.setFirstName("TestFirstname");
        testUser.setLastName("TestLastname");
        testUser.setEmail("testEmail@test.com");
        testUser.setUsername("TestUsername");
        testUser.setPassword("TestPassword");
        testUser.setMobileNumber("0725127790");
        testUser.setEnabled(true);

        expectedResult.setType(WELCOME_NEW_USER);
        expectedResult.setUsers(new HashSet<>() {{add(testUser);}});
        expectedResult.setURL("");
        expectedResult.setMsg(String.format("Bun venit! Datele dvs.: %s.",
                testUser.userInfoWithoutPassword()));
        expectedResult.setCreatedTime(null);


        NotificationFactory notificationFactory = new NotificationFactory();
        Notification actualResult = notificationFactory.createWelcomeNotification(testUser);
        actualResult.setCreatedTime(null);

        assertEquals(expectedResult, actualResult);
    }

}
