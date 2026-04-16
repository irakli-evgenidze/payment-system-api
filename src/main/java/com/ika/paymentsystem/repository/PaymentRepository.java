package com.ika.paymentsystem.repository;

import com.ika.paymentsystem.entity.Payment;
import com.ika.paymentsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByUser(User user);
}