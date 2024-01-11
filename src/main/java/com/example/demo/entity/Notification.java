package com.example.demo.entity;

import com.example.demo.enums.NotificationEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
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

    private String URL;
    @Column(length = 1024)
    private String msg;

    @JsonIgnore
    @ToString.Exclude
    @ManyToMany(
            targetEntity = User.class,
            mappedBy = "notifications",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )

    private Set<User> users = new HashSet<>();
    @Override
    public int hashCode() {
        return Objects.hash(notificationId);
    }
}
