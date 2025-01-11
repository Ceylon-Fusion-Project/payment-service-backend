package com.ceylon_fusion.payment_service.entity;

import com.ceylon_fusion.payment_service.entity.enums.RefundStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name="refunds")
public class Refund {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long refundId;

    @NotNull
    @Column(name="payment_id")
    private String paymentId;

    @Size(max=255)
    private String refundReason;

    @NotEmpty
    @Column(name="refund_status")
    private RefundStatus refundStatus;

    @NotEmpty
    @Column(name="transactiona_date")
    private LocalDateTime transactionDate;


    @CreationTimestamp
    @Column(name="created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name="updated_at", nullable = false)
    private LocalDateTime updatedAt;


}
