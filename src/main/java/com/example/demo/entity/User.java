package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Data
@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    private String firstName;

    private String lastName;

    private String email;
    private String username;
    private String password;

    private String mobileNumber;
    private boolean enabled;

    @ManyToMany(
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL
    )
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "roleId")
    )
    private Set<Role> roles = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "user_notification",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "notificationId")
    )
    private Set<Notification> notifications = new HashSet<>();

    @OneToMany(
            mappedBy = "createdByUser",
            cascade = CascadeType.ALL
    )
    @ToString.Exclude
    private List<Bug> createdBugs;

    @OneToMany(
            mappedBy = "assignedToUser",
            cascade = CascadeType.ALL
    )
    @ToString.Exclude
    private List<Bug> assignedBugs;

    @OneToMany(
            mappedBy = "user",
            targetEntity = Comment.class,
            cascade = CascadeType.ALL
    )
    private Set<Comment> userComments = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getType().toString()));
        }
        return authorities;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
       return this.enabled;
    }

    public String userInfoWithoutPassword() {
        return String.format("Date: nume = '%s %s', email = '%s', username = '%s', nr. telefon = '%s', roluri = '%s'",
                firstName, lastName, email, username, mobileNumber, roles.toString());
    }
    public String userInfoWithPassword() {
        return String.format("Date: nume = '%s %s', email = '%s', username = '%s', password = '%s', nr. telefon = '%s', roluri = '%s'",
                firstName, lastName, email, username, password, mobileNumber, roles.toString());
    }
}
