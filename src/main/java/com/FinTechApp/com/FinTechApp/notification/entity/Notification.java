package com.FinTechApp.com.FinTechApp.notification.entity;
import com.FinTechApp.com.FinTechApp.notification.enums.NotificationType;
import com.FinTechApp.com.FinTechApp.auth_users.entity.User;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
@Data @Entity @Table(name = "notifications")
public class Notification {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    private String subject;
    private String recipient;
    private String body;
    @Enumerated(EnumType.STRING) private NotificationType status;
    @ManyToOne @JoinColumn(name = "user_id") private User user;
    private final LocalDateTime createdAt = LocalDateTime.now();
}
