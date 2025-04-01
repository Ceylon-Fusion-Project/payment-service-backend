package com.ceylon_fusion.payment_service.entity;

import com.ceylon_fusion.payment_service.entity.enums.Currency;
import com.ceylon_fusion.payment_service.entity.enums.PaymentStatus;
import com.ceylon_fusion.payment_service.entity.enums.PaymentType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

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

    @DecimalMin(value="0.0", inclusive=false)
    @NotNull
    private Double amount;

    @Enumerated(EnumType.STRING)
    @Column(name="payment_status", columnDefinition = "VARCHAR(50)",nullable=false)
    private PaymentStatus paymentStatus;

    @NotNull
    private LocalDateTime paymentDate;

    // Essential Stripe fields
    @Column(name="stripe_payment_intent_id")
    private String stripePaymentIntentId;  // Required for tracking payment in Stripe

    @Column(name="payment_error")
    private String paymentError;// Important for error handling

    @NotNull
    private String clientSecret;

    //Timestamps
    @CreationTimestamp
    @Column(name="created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name="updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @NotNull
    @Column(name="currency", columnDefinition = "VARCHAR(50)")
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @NotNull
    @Column(name="paymentType")
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Refund> refunds = new HashSet<>();

//    public boolean isRefundable() {
//        return this.paymentStatus == PaymentStatus.SUCCEEDED;
//    }
//
//    public boolean isCancellable() {
//        return this.paymentStatus == PaymentStatus.PENDING;
//    }

}
