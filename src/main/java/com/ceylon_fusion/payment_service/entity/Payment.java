package com.ceylon_fusion.payment_service.entity;

import com.ceylon_fusion.payment_service.entity.enums.Currency;
import com.ceylon_fusion.payment_service.entity.enums.PaymentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
@Table(name="payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @NotNull
    @Column(name="user_id")
    private Long userId;

    @Column(name="order_id")
    private Long orderId;

    @Column(name="bookng_id")
    private Long bookingId;

    private Long paymentMethodId;

    @DecimalMin(value="0.0", inclusive=false)
    @NotNull
    private Double amount;

    @NotEmpty
    @Enumerated(EnumType.STRING)
    @Column(name="payment_status")
    private PaymentStatus paymentStatus;

    @NotNull
    private LocalDateTime paymentDate;

    @CreationTimestamp
    @Column(name="created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name="updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @NotNull
    private Currency currency;

}
