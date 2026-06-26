package com.FinTechApp.com.FinTechApp.auth_users.services;
import com.FinTechApp.com.FinTechApp.auth_users.entity.User;
import com.FinTechApp.com.FinTechApp.res.Response;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import com.FinTechApp.com.FinTechApp.auth_users.dtos.UserDTO;
import com.FinTechApp.com.FinTechApp.auth_users.dtos.UpdatePasswordRequest;

public interface UserService {
    User getCurrentLoggedInUser();

    Response<UserDTO> getMyProfile();

    Response<Page<UserDTO>> getAllUsers(int page, int size);
    Response<?> updatePassword(UpdatePasswordRequest updatePasswordRequest);
    Response<?> updateProfilePicture(MultipartFile file);
    
}
