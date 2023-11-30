package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long notificationId;

    private String type;

    private String URL;//todo nu inteleg schema de db

    private String msg;

    @ManyToMany(
            targetEntity = User.class,
            mappedBy = "notifications"
    )
    private Set<User> users = new HashSet<>();
}
