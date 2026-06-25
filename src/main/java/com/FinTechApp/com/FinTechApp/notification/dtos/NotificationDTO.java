package com.FinTechApp.com.FinTechApp.notification.dtos;
import com.FinTechApp.com.FinTechApp.notification.enums.NotificationType;
import com.fasterxml.jackson.annotation.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.time.LocalDateTime;
import java.util.Map;
@Data @Builder @AllArgsConstructor @NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) @JsonIgnoreProperties(ignoreUnknown = true)
public class NotificationDTO {
    private Long id;
    private String subject;
    @NotBlank(message = "Recipient email is required") 
    private String recipientEmail;
    private String body;
    private NotificationType type;
    private LocalDateTime createdAt;
    private String templateName;
    private Map<String, Object> templateVariables;
    
}
