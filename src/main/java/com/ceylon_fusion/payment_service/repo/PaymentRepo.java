package com.ceylon_fusion.payment_service.repo;


import com.ceylon_fusion.payment_service.entity.Payment;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface PaymentRepo extends JpaRepository<Payment, Long> {

    List<Payment> findByUserId(Long userId);

    List<Payment> findByUserIdAndPaymentDateBetween(Long userId, LocalDateTime startDate, LocalDateTime endDate);

    List<Payment> findByPaymentDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    Page<Payment> findByPaymentDateBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    Optional<Payment> findByOrderId(Long orderId);}
