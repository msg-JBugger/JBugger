package com.example.demo.service;

import com.example.demo.entity.Notification;
import com.example.demo.entity.Permission;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.enums.PermissionEnum;
import com.example.demo.enums.RoleEnum;
import com.example.demo.events.*;
import com.example.demo.factory.NotificationFactory;
import com.example.demo.repo.NotificationRepositoryInterface;
import com.example.demo.repo.PermissionRepositoryInterface;
import com.example.demo.repo.RoleRepositoryInterface;
import com.example.demo.repo.UserRepositoryInterface;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class NotificationService {
    @Autowired
    private final UserRepositoryInterface userRepository;
    @Autowired
    private final RoleRepositoryInterface roleRepository;
    @Autowired
    private final PermissionRepositoryInterface permissionRepository;
    @Autowired
    private final NotificationRepositoryInterface notificationRepository;
    @Autowired
    private final NotificationFactory notificationFactory;
    @EventListener
    public void handleUserLoginEvent(LoginEvent event) {
        Notification notification = notificationFactory.createWelcomeNotification(event.getLoggedUser());
        notification.getUsers().add(event.getLoggedUser());
        event.getLoggedUser().getNotifications().add(notification);
        notificationRepository.save(notification);
        userRepository.save(event.getLoggedUser());
    }
    @EventListener
    public void handleUserUpdatedEvent(UserUpdateEvent event) {
        Notification notification = notificationFactory.createUserUpdatedNotification(event.getUpdated(), event.getUpdater(), event.getOldInfo());
        notification.getUsers().add(event.getUpdated());
        notification.getUsers().add(event.getUpdater());
        event.getUpdated().getNotifications().add(notification);
        event.getUpdater().getNotifications().add(notification);
        notificationRepository.save(notification);
        userRepository.save(event.getUpdated());
        userRepository.save(event.getUpdater());
    }
    @EventListener
    public void handleUserDeleteEvent(UserDeleteEvent event) {
        Notification notification = notificationFactory.createUserDeletedNotification(usersWithPermission(PermissionEnum.USER_MANAGEMENT), event.getDeletedUser());
        notificationRepository.save(notification);
    }
    @EventListener
    public void handleUserDeactivatedEvent(UserDeactivateEvent event) {
        Notification notification = notificationFactory.createUserDeactivatedNotification(usersWithRole(RoleEnum.ROLE_ADMIN), event.getDeactivatedUser());
        notificationRepository.save(notification);
    }
    @EventListener
    public void handleBugStatusUpdateEvent(BugStatusUpdateEvent event) {
        Notification notification = notificationFactory.createBugStatusUpdateNotification(event.getUpdatedBug());
        notification.getUsers().add(event.getUpdatedBug().getAssignedToUser());
        notification.getUsers().add(event.getUpdatedBug().getCreatedByUser());
        event.getUpdatedBug().getAssignedToUser().getNotifications().add(notification);
        event.getUpdatedBug().getCreatedByUser().getNotifications().add(notification);
        notificationRepository.save(notification);
        userRepository.save(event.getUpdatedBug().getAssignedToUser());
        userRepository.save(event.getUpdatedBug().getCreatedByUser());
    }
    @EventListener
    public void handleBugAddedEvent(BugAddEvent event) {
        Notification notification = notificationFactory.createBugUpdateNotification(event.getAddedBug(), Boolean.TRUE);
        notification.getUsers().add(event.getAddedBug().getAssignedToUser());
        notification.getUsers().add(event.getAddedBug().getCreatedByUser());
        event.getAddedBug().getAssignedToUser().getNotifications().add(notification);
        event.getAddedBug().getCreatedByUser().getNotifications().add(notification);
        notificationRepository.save(notification);
        userRepository.save(event.getAddedBug().getAssignedToUser());
        userRepository.save(event.getAddedBug().getCreatedByUser());
    }
    @EventListener
    public void handleBugUpdateEvent(BugUpdateEvent event) {
        Notification notification = notificationFactory.createBugUpdateNotification(event.getUpdatedBug(), Boolean.FALSE);
        notification.getUsers().add(event.getUpdatedBug().getAssignedToUser());
        notification.getUsers().add(event.getUpdatedBug().getCreatedByUser());
        event.getUpdatedBug().getAssignedToUser().getNotifications().add(notification);
        event.getUpdatedBug().getCreatedByUser().getNotifications().add(notification);
        notificationRepository.save(notification);
        userRepository.save(event.getUpdatedBug().getAssignedToUser());
        userRepository.save(event.getUpdatedBug().getCreatedByUser());
    }
    @EventListener
    public void handleBugCloseEvent(BugCloseEvent event) {
        Notification notification = notificationFactory.createBugClosedNotification(event.getClosedBug());
        notification.getUsers().add(event.getClosedBug().getAssignedToUser());
        notification.getUsers().add(event.getClosedBug().getCreatedByUser());
        event.getClosedBug().getAssignedToUser().getNotifications().add(notification);
        event.getClosedBug().getCreatedByUser().getNotifications().add(notification);
        notificationRepository.save(notification);
        userRepository.save(event.getClosedBug().getAssignedToUser());
        userRepository.save(event.getClosedBug().getCreatedByUser());
    }
    public List<Role> rolesWithPermission(PermissionEnum permissionEnum) {
        Permission permission = permissionRepository.findByType(permissionEnum).orElseThrow();
        List<Role> roles = new ArrayList<>();
        for(Role role : roleRepository.findAll()) {
            if(role.getPermissions().contains(permission)) {
                roles.add(role);
            }
        }
        return roles;
    }
    public List<Notification> notificationsForUser(String username) {
        if(userRepository.findByUsername(username).isPresent()) {
            return notificationRepository.findByUsersContaining(userRepository.findByUsername(username).get());
        }
        return new ArrayList<>();
    }
    public List<User> usersWithPermission(PermissionEnum permissionEnum) {
        List<Role> rolesWithPermission = rolesWithPermission(permissionEnum);
        List<User> usersWithPermission = new ArrayList<>();
        for(User user : userRepository.findAll()) {
            for(Role role : rolesWithPermission) {
                if(user.getRoles().contains(role)) {
                    usersWithPermission.add(user);
                }
            }
        }
        return usersWithPermission;
    }
    public List<User> usersWithRole(RoleEnum roleEnum) {
        Role role = roleRepository.findByType(roleEnum).orElseThrow();
        List<User> usersWithRole = new ArrayList<>();
        for(User user : userRepository.findAll()) {
            if (user.getRoles().contains(role)) {
                usersWithRole.add(user);
            }
        }
        return usersWithRole;
    }
    @Scheduled(cron = "0 0 0 * * *")
    public void deleteOldNotifications() {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        List<Notification> notificationsToDelete = notificationRepository.findByCreatedTimeBefore(thirtyDaysAgo);
        notificationRepository.deleteAll(notificationsToDelete);
    }
}
