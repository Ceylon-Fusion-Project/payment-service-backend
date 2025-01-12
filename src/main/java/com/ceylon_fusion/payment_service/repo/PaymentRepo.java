package com.ceylon_fusion.payment_service.repo;

import com.ceylon_fusion.payment_service.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface PaymentRepo extends JpaRepository<Payment, Long> {

}
