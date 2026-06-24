package com.FinTechApp.com.FinTechApp;

import com.FinTechApp.com.FinTechApp.notification.dtos.NotificationDTO;
import com.FinTechApp.com.FinTechApp.notification.enums.NotificationType;
import com.FinTechApp.com.FinTechApp.notification.services.NotificationService;
import com.FinTechApp.com.FinTechApp.auth_users.entity.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    // Solo para probar el envío de email al iniciar — quitar en producción
    // @Bean
    // CommandLineRunner runner(NotificationService notificationService) {
    //     return args -> {
    //         NotificationDTO notificationDTO = NotificationDTO.builder()
    //                 .recipientEmail("ahorteespi16@gmail.com")
    //                 .subject("Prueba de correo")
    //                 .body("Este es un correo de prueba.")
    //                 .type(NotificationType.EMAIL)
    //                 .build();

    //         notificationService.sendEmail(notificationDTO, new User());
    //     };
    // }
}
