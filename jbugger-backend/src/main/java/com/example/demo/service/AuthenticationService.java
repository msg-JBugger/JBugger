package com.example.demo.service;

import com.example.demo.auth.AuthenticationRequest;
import com.example.demo.auth.AuthenticationResponse;
import com.example.demo.auth.RegisterRequest;
import com.example.demo.config.JwtService;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.enums.RoleEnum;
import com.example.demo.repo.RoleRepositoryInterface;
import com.example.demo.repo.UserRepositoryInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepositoryInterface userRepository;
    private final RoleRepositoryInterface roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        if (checkPhoneNumberFormat(request.getMobileNumber()) && checkEmailFormat(request.getEmail())){
            var user = User.builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .mobileNumber(request.getMobileNumber())
                    .email(request.getEmail())
                    .username(generateUsername(request.getFirstName(), request.getLastName()))
                    .password(passwordEncoder.encode(generatePassword()))
                    .roles(setRoles(request.getRoles()))
                    .build();
            userRepository.save(user);
            var jwtToken = jwtService.generateToken(user);
            return AuthenticationResponse
                    .builder()
                    .token(jwtToken)
                    .build();
        }
        else throw new RuntimeException("Invalid Fields");
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(); //todo handle exceptions

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse
                .builder()
                .token(jwtToken)
                .build();
    }

    private Set<Role> setRoles(List<String> rolesData) {
        Set<Role> rolesSet = new HashSet<>();
        for (String role : rolesData) {
            rolesSet.add(
                    roleRepository.findByType(RoleEnum.valueOf(role))
                            .orElseThrow(() -> new RuntimeException(role + " role not found"))
            );
        }
        return rolesSet;
    }

    private String generateUsername(String firstName, String lastName) {
        StringBuilder username = new StringBuilder();
        if (lastName.length() > 4) {
            for (int i = 0; i < 5; i++) {
                username.append(lastName.charAt(i));
            }
            username.append(firstName.charAt(0));
        } else {
            for (int i = 0; i < lastName.length(); i++) {
                username.append(lastName.charAt(i));
            }
            for (int i = 0; i < 6 - lastName.length(); i++) {
                username.append(firstName.charAt(i));
            }
        }
        String usr = username.toString();
        usr = usr.toLowerCase();
        int k = 1;
        while (!userRepository.findByUsername(usr).isEmpty()) {
            usr = usr.substring(0, 6) + k;
            k++;
        }
        return usr;
    }

    private String generatePassword() {
        List<String> letters = Arrays.asList(
                "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
                "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
                "!", "@", "#", "$", "%", "&", "*", "?", "!",
                "1", "2", "3", "4", "5", "6", "7", "8", "9", "0");
        StringBuilder pass = new StringBuilder();
        Random rand = new Random();
        for (int i = 0; i < 31; i++) {
            String randomElement = letters.get(rand.nextInt(letters.size()));
            pass.append(randomElement);
        }
        System.out.println(pass);
        return pass.toString();
    }

    private Boolean checkPhoneNumberFormat(String number) {
        if (number.isEmpty()) return false;
        int indexSize = 0;
        if (number.startsWith("+49") || number.startsWith("+40"))
            indexSize = 3;
        else if (number.startsWith("0049") || number.startsWith("0040"))
            indexSize = 4;
        if (indexSize > 0) {
            if (number.subSequence(indexSize, number.length()).length() <= 11) {
                return true;
            } else return false;
        } else return false;
    }

    private Boolean checkEmailFormat(String email) {
        return email.endsWith("@msggroup.com");
    }
}
