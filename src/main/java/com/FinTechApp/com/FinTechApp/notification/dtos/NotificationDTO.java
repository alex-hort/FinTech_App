import com.FinTechApp.com.FinTechApp.notification.enums.NotificationType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlanck;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDTO {

    private Long id;
    private String subject;


    @NotBlanck(message = "Recipient email is required")
    private String recipientEmail;

    private String body;

    private NotificationType type;

    private LocalDateTime createdAt;

    private String templateName;
    private Map<String, Object> templateVariables;


}