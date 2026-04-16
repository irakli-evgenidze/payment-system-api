package com.ika.paymentsystem.service;

import com.ika.paymentsystem.entity.User;
import com.ika.paymentsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    public boolean register(String email, String password) {
        if (userRepository.existsByEmail(email)) {
            return false;
        }

        User user = User.builder()
                .email(email)
                .password(password) // later we hash this
                .role("USER")
                .build();

        userRepository.save(user);
        return true;
    }

    public User login(String email, String password) {
        return userRepository.findByEmail(email)
                .filter(user -> user.getPassword().equals(password))
                .orElse(null);
    }
}