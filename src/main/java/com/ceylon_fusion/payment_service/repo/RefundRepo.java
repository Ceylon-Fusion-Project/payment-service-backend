package com.ceylon_fusion.payment_service.repo;

import com.ceylon_fusion.payment_service.entity.Refund;
import com.ceylon_fusion.payment_service.entity.enums.RefundStatus;
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
public interface RefundRepo extends JpaRepository<Refund, Long> {
    Page<Refund> findByPayment_UserId(Long userId, Pageable pageable);

    Page<Refund> findByTransactionDateBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

//    Page<Refund> findByPayment_UserId(Long userId, Pageable pageable);
//    Page<Refund> findByTransactionDateBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
//    List<Refund> findByPayment_UserIdAndTransactionDateBetween(Long userId, LocalDateTime startDate, LocalDateTime endDate);
}
