package com.example.demo.controller;

import com.example.demo.auth.AuthenticationResponse;
import com.example.demo.auth.RegisterRequest;
import com.example.demo.entity.User;
import com.example.demo.repo.UserRepositoryInterface;
import com.example.demo.service.AuthenticationService;
import com.example.demo.service.UserService;
import com.example.demo.user_call.DeactivateResponse;
import com.example.demo.user_call.UpdateRequest;
import com.example.demo.user_call.UpdateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private UserRepositoryInterface userRepository;

    private final UserService userService;
    private final AuthenticationService authService;

    @PostMapping("/add")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/addAdmin")
    public ResponseEntity<AuthenticationResponse> registerAdmin(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PutMapping("/update/{username}")
    public ResponseEntity<UpdateResponse> update(@PathVariable String username, @RequestBody UpdateRequest request) {

        return ResponseEntity.ok(userService.update(username, request, SecurityContextHolder.getContext().getAuthentication().getName()));
    }

    @DeleteMapping("/deactivate/{username}")
    public ResponseEntity<DeactivateResponse> deactivate(@PathVariable String username) {
        return ResponseEntity.ok(userService.deactivate(username));
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            List<User> userList = new ArrayList<>(userService.findAll());

            if (userList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        Optional<User> userData = userRepository.findByUsername(username);
        return userData.map(user -> new ResponseEntity<>(user, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/updateUserById/{id}")
    public ResponseEntity<User> updateUserById(@PathVariable Long id, @RequestBody User newUserData) {
        Optional<User> oldUserData = userRepository.findById(id);

        if (oldUserData.isPresent()) {
            User updatedUserData = oldUserData.get();
            updatedUserData.setFirstName(newUserData.getFirstName());
            updatedUserData.setLastName(newUserData.getLastName());
            updatedUserData.setMobileNumber(newUserData.getMobileNumber());
            updatedUserData.setEmail(newUserData.getEmail());
            updatedUserData.setUsername(newUserData.getUsername());

            User userObj = userRepository.save(updatedUserData);
            return new ResponseEntity<>(userObj, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/deleteUserById/{id}")
    public ResponseEntity<HttpStatus> deleteUserById(@PathVariable Long id) {
        userRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
