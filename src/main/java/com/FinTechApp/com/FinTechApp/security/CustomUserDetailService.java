package com.FinTechApp.com.FinTechApp.security;

import com.FinTechApp.com.FinTechApp.auth_users.entity.User;
import com.FinTechApp.com.FinTechApp.auth_users.repo.UserRepo;
import com.FinTechApp.com.FinTechApp.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepo userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + username));
        return AuthUser.builder()
                .user(user)
                .build();
    }
}
