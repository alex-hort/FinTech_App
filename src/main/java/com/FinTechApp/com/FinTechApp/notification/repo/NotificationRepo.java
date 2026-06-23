package com.FinTechApp.com.FinTechApp.notification.repo;
import com.FinTechApp.com.FinTechApp.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
public interface NotificationRepo extends JpaRepository<Notification, Long> {}
