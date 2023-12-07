package com.example.demo.entity;

import com.example.demo.enums.NotificationEnum;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
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

    private NotificationEnum type;
    private LocalDateTime createdTime;

    private String URL;//todo nu inteleg schema de db
    @Column(length = 1024)
    private String msg;

    @ManyToMany(
            targetEntity = User.class,
            mappedBy = "notifications"
    )
    private Set<User> users = new HashSet<>();
}
