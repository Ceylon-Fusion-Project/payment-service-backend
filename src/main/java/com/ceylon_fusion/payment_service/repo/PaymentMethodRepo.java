package com.ceylon_fusion.payment_service.repo;

import com.ceylon_fusion.payment_service.entity.PaymentMethod;
import com.ceylon_fusion.payment_service.entity.enums.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface PaymentMethodRepo extends JpaRepository<PaymentMethod, Long> {

    Optional<PaymentMethod> findByUserIdAndIsDefaultTrue(Long userId);

    Page<PaymentMethod> findByUserId(Long userId, PageRequest of);
}
