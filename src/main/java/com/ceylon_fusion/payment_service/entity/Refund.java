package com.ceylon_fusion.payment_service.entity;

import com.ceylon_fusion.payment_service.entity.enums.RefundStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
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

//    @NotNull
//    @Column(name="payment_id")
//    private Long paymentId;


    @Size(max=255)
    private String refundReason;

    @DecimalMin(value="0.0", inclusive=false)
    @NotNull
    private Double amount;

    @Enumerated(EnumType.STRING)
    @NotEmpty
    @Column(name="refund_status", columnDefinition = "VARCHAR(50)")
    private RefundStatus refundStatus;

    @NotEmpty
    @Column(name="transactiona_date")
    private LocalDateTime transactionDate;

    // Essential Stripe fields
    @Column(name="stripe_refund_id", unique = true)
    private String stripeRefundId;  // Required for tracking refund in Stripe

    @Column(name="refund_error")
    private String refundError;  // Important for error handling

    @CreationTimestamp
    @Column(name="created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name="updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;
}
