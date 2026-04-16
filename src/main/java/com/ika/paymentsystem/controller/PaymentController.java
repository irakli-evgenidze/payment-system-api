package com.ika.paymentsystem.controller;

import com.ika.paymentsystem.dto.CreatePaymentRequest;
import com.ika.paymentsystem.entity.Payment;
import com.ika.paymentsystem.entity.User;
import com.ika.paymentsystem.repository.PaymentRepository;
import com.ika.paymentsystem.repository.UserRepository;
import com.ika.paymentsystem.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;

    @PostMapping
    public Payment createPayment(@RequestBody @Valid CreatePaymentRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return paymentService.createPayment(
                request.getAmount(),
                request.getMethod(),
                user
        );
    }

    @GetMapping
    public List<Payment> getPayments(@RequestParam String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return paymentService.getUserPayments(user);
    }

    @PostMapping("/{id}/process")
    public Payment processPayment(@PathVariable Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        return paymentService.processPayment(payment);
    }

    @PostMapping("/{id}/refund")
    public Payment refundPayment(@PathVariable Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        return paymentService.refundPayment(payment);
    }
}