package com.ika.paymentsystem.service;

import com.ika.paymentsystem.entity.Payment;
import com.ika.paymentsystem.entity.User;
import com.ika.paymentsystem.enums.PaymentMethod;
import com.ika.paymentsystem.enums.PaymentStatus;
import com.ika.paymentsystem.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public Payment createPayment(BigDecimal amount, PaymentMethod method, User user) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }

        Payment payment = Payment.builder()
                .amount(amount)
                .method(method)
                .status(PaymentStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .user(user)
                .build();

        return paymentRepository.save(payment);
    }

    public Payment processPayment(Payment payment) {
        if (payment.getAmount().compareTo(new BigDecimal("1000")) > 0) {
            payment.setStatus(PaymentStatus.FAILED);
        } else {
            payment.setStatus(PaymentStatus.SUCCESS);
        }

        return paymentRepository.save(payment);
    }

    public List<Payment> getUserPayments(User user) {
        return paymentRepository.findByUser(user);
    }

    public Payment refundPayment(Payment payment) {
        if (payment.getStatus() != PaymentStatus.SUCCESS) {
            throw new IllegalArgumentException("Only successful payments can be refunded");
        }

        payment.setStatus(PaymentStatus.REFUNDED);
        return paymentRepository.save(payment);
    }
}