package com.FinTechApp.com.FinTechApp.auth_users.services.impl;

import com.FinTechApp.com.FinTechApp.account.entity.Account;
import com.FinTechApp.com.FinTechApp.account.repo.AccountRepo;
import com.FinTechApp.com.FinTechApp.auth_users.dtos.LoginRequest;
import com.FinTechApp.com.FinTechApp.auth_users.dtos.LoginResponse;
import com.FinTechApp.com.FinTechApp.auth_users.dtos.RegistrationRequest;
import com.FinTechApp.com.FinTechApp.auth_users.dtos.RequestPasswordReset;
import com.FinTechApp.com.FinTechApp.auth_users.entity.PasswordResetCode;
import com.FinTechApp.com.FinTechApp.auth_users.entity.User;
import com.FinTechApp.com.FinTechApp.auth_users.repo.UserRepo;
import com.FinTechApp.com.FinTechApp.auth_users.services.AuthService;
import com.FinTechApp.com.FinTechApp.enums.AccountType;
import com.FinTechApp.com.FinTechApp.enums.Currency;
import com.FinTechApp.com.FinTechApp.exceptions.BadRequestException;
import com.FinTechApp.com.FinTechApp.exceptions.NotFoundException;
import com.FinTechApp.com.FinTechApp.notification.dtos.NotificationDTO;
import com.FinTechApp.com.FinTechApp.notification.services.NotificationService;
import com.FinTechApp.com.FinTechApp.res.Response;
import com.FinTechApp.com.FinTechApp.role.entity.Role;
import com.FinTechApp.com.FinTechApp.role.repo.RoleRepo;
import com.FinTechApp.com.FinTechApp.security.TokenService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.FinTechApp.com.FinTechApp.auth_users.services.CodeGenerator;
import com.FinTechApp.com.FinTechApp.auth_users.repo.PasswordResetCodeRepo;
import java.time.LocalDateTime;


import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final AccountRepo accountRepo;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final NotificationService notificationService;

    private final CodeGenerator codeGenerator;
    private final PasswordResetCodeRepo passwordResetCodeRepo;

    @Value("${password.reset.link")
    private String resetLink;

    @Override
    public Response<String> register(RegistrationRequest request) {

        // Asigna roles: si no vienen en el request usa CUSTOMER por defecto
        List<Role> roles;
        if (request.getRoles() == null || request.getRoles().isEmpty()) {
            Role defaultRole = roleRepo.findByName("CUSTOMER")
                    .orElseThrow(() -> new RuntimeException("CUSTOMER role not found"));
            roles = Collections.singletonList(defaultRole);
        } else {
            // fix: el stream devuelve List<Optional<Role>>, hay que mapear correctamente
            roles = request.getRoles().stream()
                    .map(roleName -> roleRepo.findByName(roleName)
                            .orElseThrow(() -> new NotFoundException("Role not found: " + roleName)))
                    .toList();
        }

        if (userRepo.findByEmail(request.getEmail()).isPresent()) {
            throw new BadRequestException("Email already exists");
        }

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(roles)
                .active(true)
                .build();

        User savedUser = userRepo.save(user);

        // Auto genera cuenta SAVINGS para el usuario
        // Account savedAccount = accountRepo.save(Account.builder()
        // .accountNumber(String.valueOf(System.currentTimeMillis()))
        // .accountType(AccountType.SAVINGS)
        // .currency(com.FinTechApp.com.FinTechApp.enums.Currency.USD)
        // .user(savedUser).balance(java.math.BigDecimal.ZERO)
        // .build());

        // Email de bienvenida
        Map<String, Object> welcomeVars = new HashMap<>();
        welcomeVars.put("name", savedUser.getFirstName());

        //  fix: sin punto y coma entre campos del builder
        NotificationDTO welcomeDTO = NotificationDTO.builder()
                .recipientEmail(savedUser.getEmail())
                .subject("Welcome to FinTech BANK")
                .templateName("welcome")
                .templateVariables(welcomeVars)
                .build();

        notificationService.sendEmail(welcomeDTO, savedUser);

        // Email con detalles de la cuenta
        Map<String, Object> accountVars = new HashMap<>();
        accountVars.put("name", savedUser.getFirstName());
        // accountVars.put("accountNumber", savedAccount.getAccountNumber());
        accountVars.put("accountType", AccountType.SAVINGS.name());
        accountVars.put("currency", Currency.USD);

        NotificationDTO accountDTO = NotificationDTO.builder()
                .recipientEmail(savedUser.getEmail())
                .subject("Your Account has been created")
                .templateName("account-created")
                .templateVariables(accountVars)
                .build();

        notificationService.sendEmail(accountDTO, savedUser);

        // fix: sin punto y coma entre campos del builder
        return Response.<String>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Your account has been created successfully")
               // .data("Account details sent to your email. Account number: " + savedAccount.getAccountNumber())
                .build();
    }

    @Override
    public Response<LoginResponse> login(LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        User user = userRepo.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found with email: " + email));

        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new BadRequestException("Invalid password");
        }

        String token = tokenService.generateToken(user.getEmail());
        LoginResponse loginResponse = LoginResponse.builder()
        .roles(user.getRoles().stream().map(Role::getName).toList())
        .token(token)
        .build();

        return Response.<LoginResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Login successful")
                .data(loginResponse)
                .build();
    }

@Override
@Transactional
public Response<?> forgetPassword(String email) {

    User user = userRepo.findByEmail(email)
            .orElseThrow(() -> new NotFoundException("User Not Found"));

    passwordResetCodeRepo.deleteByUserId(user.getId());

    String code = codeGenerator.generateUniqueCode();

    PasswordResetCode resetCode = PasswordResetCode.builder()
            .user(user)
            .code(code)
            .expiryDate(calculateExpiryDate())
            .used(false)
            .build();

    passwordResetCodeRepo.save(resetCode);

    // Send password reset link
    Map<String, Object> templateVariables = new HashMap<>();

    templateVariables.put("name", user.getFirstName());
    templateVariables.put("resetLink", resetLink + code);

    NotificationDTO notificationDTO = NotificationDTO.builder()
            .recipientEmail(user.getEmail())
            .subject("Password Reset Code")
            .templateName("password-reset")
            .templateVariables(templateVariables)
            .build();

    notificationService.sendEmail(notificationDTO, user);

    return Response.builder()
            .statusCode(HttpStatus.OK.value())
            .message("Password reset code sent to your email")
            .build();
}

    @Override
    @Transactional
    public Response<?> updatePasswordViaResetCode(RequestPasswordReset resetPasswordRequest) {
        String code = resetPasswordRequest.getCode();
        String newPassword = resetPasswordRequest.getNewPassword();

        //find validate code
        PasswordResetCode resetCode = passwordResetCodeRepo.findByCode(code)
                .orElseThrow(() -> new NotFoundException("Invalid reset code"));
            
       //CHECK EXPIRATION FIRST
       if(resetCode.getExpiryDate().isBefore(LocalDateTime.now())){
           passwordResetCodeRepo.delete(resetCode);
              throw new BadRequestException("Reset code has expired");
       }

       //update user password

       User user = resetCode.getUser();
         user.setPassword(passwordEncoder.encode(newPassword));
         userRepo.save(user);

         //delete code after use
            passwordResetCodeRepo.delete(resetCode);


        Map<String, Object> templateVariables = new HashMap<>();
        templateVariables.put("name", user.getFirstName());

        NotificationDTO notificationDTO = NotificationDTO.builder()
                .recipientEmail(user.getEmail())
                .subject("Password Updated Successfully")
                .templateName("password-updated-confirmation")
                .templateVariables(templateVariables)
                .build();

                notificationService.sendEmail(notificationDTO, user);
                 
                
                return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Password updated successfully")
                .build();



        
    }

    private LocalDateTime calculateExpiryDate() {
        return LocalDateTime.now().plusHours(1); // Expira en 1 hora
    }
}
