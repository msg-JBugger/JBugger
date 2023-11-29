package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.repo.UserRepositoryInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {
    @Autowired
    private UserRepositoryInterface userRepository;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            List<User> userList = new ArrayList<>(userRepository.findAll());

            if(userList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(HttpStatus.OK);
        } catch(Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getUserById/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> userData = userRepository.findById(id);

        return userData.map(user -> new ResponseEntity<>(user, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }

    @PostMapping("/addUser")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        User userObj = userRepository.save(user);

        return new ResponseEntity<>(userObj, HttpStatus.OK);
    }

    @PostMapping("/updateUserById/{id}")
    public ResponseEntity<User> updateUserById(@PathVariable Long id, @RequestBody User newUserData) {
        Optional<User> oldUserData = userRepository.findById(id);

        if(oldUserData.isPresent()) {
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
