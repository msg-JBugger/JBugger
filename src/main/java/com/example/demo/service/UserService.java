package com.example.demo.service;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.enums.RoleEnum;
import com.example.demo.events.UserDeactivateEvent;
import com.example.demo.events.UserUpdateEvent;
import com.example.demo.repo.RoleRepositoryInterface;
import com.example.demo.repo.UserRepositoryInterface;
import com.example.demo.user_call.DeactivateResponse;
import com.example.demo.user_call.UpdateRequest;
import com.example.demo.user_call.UpdateResponse;
import com.example.demo.user_call.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired
    private final UserRepositoryInterface userRepository;
    @Autowired
    private final RoleRepositoryInterface roleRepository;
    @Autowired
    private final ApplicationEventPublisher eventPublisher;

    public UpdateResponse update(String username, UpdateRequest request, String updaterName) throws UsernameNotFoundException {
        var user = userRepository.findByUsername(username).orElseThrow(()
                -> new UsernameNotFoundException("User with username " + username + " not found"));
        var updater = userRepository.findByUsername(updaterName).orElseThrow(()
                -> new UsernameNotFoundException("User with username " + updaterName + " not found"));
        UserInfo oldUserInfo = UserInfo.builder()
                .email(user.getEmail())
                .roles(user.getRoles())
                .mobileNumber(user.getMobileNumber())
                .enabled(user.isEnabled())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .password(user.getPassword())
                .build();

        user.setMobileNumber(request.getMobile());
        user.setEmail(request.getEmail());
        user.setRoles(generateRoles(request.getRoles()));
        userRepository.save(user);
        eventPublisher.publishEvent(UserUpdateEvent.builder()
                .updated(user)
                .updater(updater)
                .oldInfo(oldUserInfo)
                .build());
        return UpdateResponse
                .builder()
                .user(user)
                .build();
    }

    public DeactivateResponse deactivate(String username) {
        var user = userRepository.findByUsername(username).orElseThrow(()
                -> new UsernameNotFoundException("User with username " + username + " not found"));
        DeactivateResponse dr = new DeactivateResponse();

        if(user.isEnabled()) {
            user.setEnabled(false);
            userRepository.save(user);
            dr.setMsg("User with username " + username + " has been deactivated");
            eventPublisher.publishEvent(UserDeactivateEvent.builder()
                    .deactivatedUser(user)
                    .build());
        } else {
            dr.setMsg("User with username " + username + " is already deactivated");
        }

        return dr;
    }

    public Set<Role> generateRoles(List<String> rolesData) {
        Set<Role> rolesSet = new HashSet<>();
        for (String role : rolesData) {
            rolesSet.add(
                    roleRepository.findByType(RoleEnum.valueOf(role))
                            .orElseThrow(() -> new RuntimeException(role + " role not found"))
            );
        }
        return rolesSet;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }
}
