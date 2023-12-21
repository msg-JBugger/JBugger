package com.example.demo.user_call;

import com.example.demo.entity.Role;
import lombok.Builder;

import java.util.Set;

@Builder
public class UserInfo {
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;
    private String mobileNumber;
    private boolean enabled;
    private Set<Role> roles;
    public String userInfoWithPassword() {
        return String.format("nume = '%s %s', email = '%s', username = '%s', password = '%s', nr. telefon = '%s', roluri = '%s'",
                firstName, lastName, email, username, password, mobileNumber, roles.toString());
    }
}
