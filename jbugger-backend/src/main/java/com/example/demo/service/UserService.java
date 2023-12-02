package com.example.demo.service;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.enums.RoleEnum;
import com.example.demo.repo.RoleRepositoryInterface;
import com.example.demo.repo.UserRepositoryInterface;
import com.example.demo.user_call.UpdateRequest;
import com.example.demo.user_call.UpdateResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepositoryInterface userRepository;
    @Autowired
    private final RoleRepositoryInterface roleRepository;

    public UpdateResponse update(String username, UpdateRequest request) throws UsernameNotFoundException {
        var user = userRepository.findByUsername(username).orElseThrow(()
                -> new UsernameNotFoundException("User with username " + username + " not found"));
        for(Role role:user.getRoles()) {
            removeRoleFromUser(user, role);
        }
        user.setMobileNumber(request.getMobile());
        user.setEmail(request.getEmail());
        user.setRoles(generateRoles(request.getRoles()));
        userRepository.save(user);
        return UpdateResponse
                .builder()
                .user(user)
                .build();
    }

    @Transactional
    public void removeRoleFromUser(User user, Role role) {
        Set<Role> roles = user.getRoles();
        roles.remove(role);
        user.setRoles(roles);
        userRepository.save(user);
    }

    public Set<Role> generateRoles(List<String> rolesData) {
        Set<Role> rolesSet = new HashSet<>();
        for(String role: rolesData) {
            rolesSet.add(
                    roleRepository.findByType(RoleEnum.valueOf(role))
                            .orElseThrow(() -> new RuntimeException(role + " role not found"))
            );
        }
        return rolesSet;
    }
}
