package com.ika.paymentsystem.controller;

import com.ika.paymentsystem.entity.User;
import com.ika.paymentsystem.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public String register(@RequestParam String email, @RequestParam String password) {
        boolean success = authService.register(email, password);

        if (success) {
            return "User registered successfully";
        } else {
            return "User already exists";
        }
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password) {
        User user = authService.login(email, password);

        if (user != null) {
            return "Login successful";
        } else {
            return "Invalid credentials";
        }
    }
}