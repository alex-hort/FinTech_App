package com.FinTechApp.com.FinTechApp.auth_users.services;

import com.FinTechApp.com.FinTechApp.res.Response;
import com.FinTechApp.com.FinTechApp.auth_users.dtos.LoginRequest;
import com.FinTechApp.com.FinTechApp.auth_users.dtos.LoginResponse;
import com.FinTechApp.com.FinTechApp.auth_users.dtos.RegistrationRequest;
import com.FinTechApp.com.FinTechApp.auth_users.dtos.RequestPasswordReset;

public interface AuthService {
    Response<String> register(RegistrationRequest request);
    Response<LoginResponse> login(LoginRequest request);
    Response<?> forgetPassword(String email);
    Response<?> updatePasswordViaResetCode(RequestPasswordReset request);
}
