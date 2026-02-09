package com.crm.server.service;

import com.crm.server.dto.AuthRequest;
import com.crm.server.dto.AuthResponse;
import com.crm.server.entity.User;
import com.crm.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    // In Sprint 3,  add BCrypt Password Encoding
    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (!user.getPasswordHash().equals(request.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        // Generate Token
        String token = jwtService.generateToken(user.getEmail(), user.getTenant().getId().toString());
        return new AuthResponse(token);
    }
}