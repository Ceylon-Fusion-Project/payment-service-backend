package com.ceylon_fusion.payment_service.repo;

import com.ceylon_fusion.payment_service.entity.Refund;
import com.ceylon_fusion.payment_service.entity.enums.RefundStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface RefundRepo extends JpaRepository<Refund,Long> {

    List<Refund> findByPaymentId(String paymentId);
    List<Refund> findByRefundStatus(RefundStatus status);
    List<Refund> findByTransactionDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    Optional<Refund> findByRefundIdAndPaymentId(Long refundId, String paymentId);
}
