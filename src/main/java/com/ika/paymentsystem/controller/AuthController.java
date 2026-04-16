package com.ika.paymentsystem.controller;

import com.ika.paymentsystem.dto.LoginRequest;
import com.ika.paymentsystem.dto.RegisterRequest;
import com.ika.paymentsystem.entity.User;
import com.ika.paymentsystem.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public String register(@RequestBody @Valid RegisterRequest request) {
        boolean success = authService.register(request.getEmail(), request.getPassword());

        if (success) {
            return "User registered successfully";
        } else {
            return "User already exists";
        }
    }

    @PostMapping("/login")
    public String login(@RequestBody @Valid LoginRequest request) {
        User user = authService.login(request.getEmail(), request.getPassword());

        if (user != null) {
            return "Login successful";
        } else {
            return "Invalid credentials";
        }
    }
}