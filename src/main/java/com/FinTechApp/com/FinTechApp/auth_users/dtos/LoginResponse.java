package com.FinTechApp.com.FinTechApp.auth_users.dtos;
import lombok.*;
import java.util.List;


@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class LoginResponse {
    private String token;
    private List<String> roles;
}
