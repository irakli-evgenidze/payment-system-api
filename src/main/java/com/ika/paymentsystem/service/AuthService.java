package com.ika.paymentsystem.service;

import com.ika.paymentsystem.entity.User;
import com.ika.paymentsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean register(String email, String password) {
        if (userRepository.existsByEmail(email)) {
            return false;
        }

        User user = User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .role("USER")
                .build();

        userRepository.save(user);
        return true;
    }

    public User login(String email, String password) {
        return userRepository.findByEmail(email)
                .filter(user -> isPasswordValid(user, password))
                .map(user -> upgradePasswordIfNeeded(user, password))
                .orElse(null);
    }

    private boolean isPasswordValid(User user, String rawPassword) {
        String storedPassword = user.getPassword();

        if (storedPassword == null || storedPassword.isBlank()) {
            return false;
        }

        if (looksLikeBcrypt(storedPassword)) {
            return passwordEncoder.matches(rawPassword, storedPassword);
        }

        // legacy plain-text support
        return storedPassword.equals(rawPassword);
    }

    private User upgradePasswordIfNeeded(User user, String rawPassword) {
        String storedPassword = user.getPassword();

        if (!looksLikeBcrypt(storedPassword)) {
            user.setPassword(passwordEncoder.encode(rawPassword));
            userRepository.save(user);
        }

        return user;
    }

    private boolean looksLikeBcrypt(String password) {
        return password.startsWith("$2a$")
                || password.startsWith("$2b$")
                || password.startsWith("$2y$");
    }
}