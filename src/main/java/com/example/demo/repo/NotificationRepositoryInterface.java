package com.example.demo.repo;

import com.example.demo.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepositoryInterface extends JpaRepository<Notification, Long> {
    List<Notification> findByCreatedTimeBefore(LocalDateTime date);
}
