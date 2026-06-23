package com.FinTechApp.com.FinTechApp.auth_users.dtos;
import com.FinTechApp.com.FinTechApp.account.dtos.AccountDTO;
import com.fasterxml.jackson.annotation.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
@Data @Builder @AllArgsConstructor @NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) @JsonIgnoreProperties(ignoreUnknown = true)
public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    @JsonIgnore private String password;
    private String profilePictureUrl;
    private boolean active;
    private List<String> roles;
    @JsonManagedReference private List<AccountDTO> accounts;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
