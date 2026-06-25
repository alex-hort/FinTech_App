package com.FinTechApp.com.FinTechApp.notification.services;
import com.FinTechApp.com.FinTechApp.notification.entity.Notification;
import com.FinTechApp.com.FinTechApp.notification.dtos.NotificationDTO;
import com.FinTechApp.com.FinTechApp.notification.repo.NotificationRepo;
import com.FinTechApp.com.FinTechApp.auth_users.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepo notificationRepo;
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Override
    @Async
    public void sendEmail(NotificationDTO notificationDTO, User user) {
        try {
            MimeMessage message = mailSender.createMimeMessage(); // ← fix: era "mimeMessage" dentro del helper
            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());

            helper.setTo(notificationDTO.getRecipientEmail());
            helper.setSubject(notificationDTO.getSubject());

            if (notificationDTO.getTemplateName() != null) {
                Context context = new Context();
                context.setVariables(notificationDTO.getTemplateVariables());
                String htmlContent = templateEngine.process(notificationDTO.getTemplateName(), context);
                helper.setText(htmlContent, true);
            } else {
                helper.setText(notificationDTO.getBody(), true);
            }

            mailSender.send(message);

            //savee to database
           Notification notificationToSave = Notification.builder()   // builder, no builde
        .recipient(notificationDTO.getRecipientEmail())
        .subject(notificationDTO.getSubject())
        .body(notificationDTO.getBody())
        .status(notificationDTO.getType())   // status, no type (coincide con el campo de la entidad)
        .user(user)
        .build();
            notificationRepo.save(notificationToSave);

        } catch (MessagingException e) {
            log.error(e.getMessage());
        }
    }
}
