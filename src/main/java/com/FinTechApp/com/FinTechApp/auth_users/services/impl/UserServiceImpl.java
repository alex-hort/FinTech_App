package com.FinTechApp.com.FinTechApp.auth_users.services.impl;

import com.FinTechApp.com.FinTechApp.auth_users.entity.User;
import com.FinTechApp.com.FinTechApp.auth_users.services.UserService;
import org.springframework.stereotype.Service;
import com.FinTechApp.com.FinTechApp.res.Response;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import com.FinTechApp.com.FinTechApp.auth_users.dtos.UserDTO;
import com.FinTechApp.com.FinTechApp.auth_users.dtos.UpdatePasswordRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.FinTechApp.com.FinTechApp.exceptions.BadRequestException;
import java.util.Map;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.nio.file.Files;
import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;
import java.util.HashMap;


import org.modelmapper.ModelMapper;

import com.FinTechApp.com.FinTechApp.notification.dtos.NotificationDTO;
import com.FinTechApp.com.FinTechApp.notification.services.NotificationService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.FinTechApp.com.FinTechApp.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import lombok.extern.slf4j.Slf4j;
import com.FinTechApp.com.FinTechApp.auth_users.repo.UserRepo;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final NotificationService notificationService;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    private final String uploadDir = "uploads/profile_pictures/";

    @Override
    public User getCurrentLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new NotFoundException("No authenticated user found");
        }
        String email = authentication.getName();
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + email));
    }

    @Override
    public Response<UserDTO> getMyProfile() {
        User user = getCurrentLoggedInUser();
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);

        return Response.<UserDTO>builder()
                .statusCode(HttpStatus.OK.value())
                .message("User profile retrieved successfully")
                .data(userDTO)
                .build();
    }

    @Override
    public Response<Page<UserDTO>> getAllUsers(int page, int size) {
        Page<User> users = userRepo.findAll(PageRequest.of(page, size));
        Page<UserDTO> userDTOs = users.map(user -> modelMapper.map(user, UserDTO.class));

        return Response.<Page<UserDTO>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Users retrieved successfully")
                .data(userDTOs)
                .build();
    }

    @Override
    public Response<?> updatePassword(UpdatePasswordRequest updatePasswordRequest) {

        User user = getCurrentLoggedInUser();

        String newPassword = updatePasswordRequest.getNewPassword();
        String oldPassword = updatePasswordRequest.getOldPassword();

        if (oldPassword == null || newPassword == null) {
            throw new BadRequestException("Old and New Password Required");
        }

        // Validate the old password.
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BadRequestException("Old Password not Correct");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedAt(LocalDateTime.now());

        userRepo.save(user);

        // Send password change confirmation email.
        Map<String, Object> templateVariables = new HashMap<>();
        templateVariables.put("name", user.getFirstName());

        NotificationDTO notificationDTO = NotificationDTO.builder()
                .recipientEmail(user.getEmail())
                .subject("Your Password Was Successfully Changed")
                .templateName("password-change")
                .templateVariables(templateVariables)
                .build();

        notificationService.sendEmail(notificationDTO, user);

        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Password Changed Successfully")
                .build();
    }

    @Override
    public Response<?> updateProfilePicture(MultipartFile file) {

        User user = getCurrentLoggedInUser();

        try {

            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Delete old profile picture if it exists
            if (user.getProfilePictureUrl() != null &&
                    !user.getProfilePictureUrl().isEmpty()) {

                Path oldFile = Paths.get(user.getProfilePictureUrl());

                if (Files.exists(oldFile)) {
                    Files.delete(oldFile);
                }
            }

            // Generate a unique file name to avoid conflicts
            String originalFileName = file.getOriginalFilename();

            String fileExtension = "";

            if (originalFileName != null && originalFileName.contains(".")) {
                fileExtension = originalFileName.substring(
                        originalFileName.lastIndexOf("."));
            }

            String newFileName = UUID.randomUUID() + fileExtension;

            Path filePath = uploadPath.resolve(newFileName);

            Files.copy(file.getInputStream(), filePath);

            String fileUrl = uploadDir + newFileName;

            user.setProfilePictureUrl(fileUrl);

            userRepo.save(user);

            return Response.builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Profile picture uploaded successfully.")
                    .data(fileUrl)
                    .build();

        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
