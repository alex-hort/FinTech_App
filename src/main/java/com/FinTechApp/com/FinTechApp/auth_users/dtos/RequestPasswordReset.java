package com.FinTechApp.com.FinTechApp.auth_users.dtos;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
@Data @JsonIgnoreProperties(ignoreUnknown = true)
public class RequestPasswordReset {
    private String email;
    private String code;
    private String newPassword;
}
