package com.example.demo.controller;

import com.example.demo.entity.Notification;
import com.example.demo.entity.User;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NotificationController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sendNotificationToUser(Notification notification) {
        for(User user : notification.getUsers()) {
            messagingTemplate.convertAndSendToUser(user.getUsername(), "/notifications", notification);
        }
    }
}
