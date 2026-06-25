package com.FinTechApp.com.FinTechApp.notification.services;

import com.FinTechApp.com.FinTechApp.notification.dtos.NotificationDTO;
import com.FinTechApp.com.FinTechApp.auth_users.entity.User;

public interface NotificationService {
    void sendEmail(NotificationDTO notificationDTO, User user);
}
