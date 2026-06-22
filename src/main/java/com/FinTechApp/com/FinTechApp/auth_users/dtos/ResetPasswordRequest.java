
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestPasswordReset {
    
    private String email;

    private String code;
    private String newPassword;
}