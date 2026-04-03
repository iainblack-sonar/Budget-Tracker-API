package com.budgetly.auth;

import com.budgetly.common.exception.UnauthorizedException;
import com.budgetly.common.exception.ValidationException;
import com.budgetly.persistence.entity.UserEntity;
import com.budgetly.persistence.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthenticationService(
            UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public String register(String email, String password, String firstName, String lastName) {
        if (userRepository.existsByEmail(email)) {
            throw new ValidationException("Email already registered");
        }

        UserEntity user =
                UserEntity.builder()
                        .email(email)
                        .passwordHash(passwordEncoder.encode(password))
                        .firstName(firstName)
                        .lastName(lastName)
                        .build();

        user = userRepository.save(user);
        return jwtService.generateToken(user.getId(), user.getEmail());
    }

    public String login(String email, String password) {
        UserEntity user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new UnauthorizedException("Invalid credentials");
        }

        return jwtService.generateToken(user.getId(), user.getEmail());
    }
}
