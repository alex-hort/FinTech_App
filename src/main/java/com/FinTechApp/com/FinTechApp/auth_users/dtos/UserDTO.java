
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDTO{

    private Long id;

    private String firstName;
    private String lastName;
    private String phoneNumber;

    private String email;

    @JsonIgnore
    private String password;
    private String profilePictureUrl;
    private boolean active;

    private List<String> roles;


    @JsonManagedReference
    private List<AccountDTO> accounts;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}