
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class LoginResponse {

    private String token;
    private List<String> roles;

}