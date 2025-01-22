package com.ceylon_fusion.payment_service.entity;

import com.ceylon_fusion.payment_service.entity.enums.PaymentMethodsStatus;
import com.ceylon_fusion.payment_service.entity.enums.Provider;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name="payment_methods")
public class PaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentMethodId;

    @Column(name="method_name")
    private String methodName;

    @NotNull
    @Column(name="is_active")
    private Boolean isActive;

    @NotNull(message = "User ID is required")
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @NotNull(message = "Default status is required")
    @Column(name = "is_default", nullable = false)
    private Boolean isDefault;

    // Essential Stripe fields
    @Column(name="stripe_payment_method_id", unique = true)
    private String stripePaymentMethodId;  // Required for processing payments

    @NotEmpty(message = "Masked details can't be empty")
    @Pattern(regexp = "\\*{4} \\*{4} \\*{4} \\d{4}",message = "Masked details must follow the format **** **** **** 1234")
    @Column(name = "masked_details", nullable = false)
    private String maskedDetails;

    @Column(name="card_exp_month")
    private Integer cardExpirationMonth;  // Required for card validity

    @Column(name="card_exp_year")
    private Integer cardExpirationYear;  // Required for card validity

    @Enumerated(EnumType.STRING)
    @NotNull(message="Provider is required")
    @Column(name="provider", nullable=false, columnDefinition = "VARCHAR(50)")
    private Provider provider;

    @Enumerated(EnumType.STRING)
    @NotNull(message="Status is required")
    @Column(name="status", nullable=false, columnDefinition = "VARCHAR(50)")
    private PaymentMethodsStatus status;

    @CreationTimestamp
    @Column(name="created_at",nullable=false,updatable=false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name ="updated_at", nullable=false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "paymentMethod", cascade = CascadeType.ALL)
    private Set<Payment> payments = new HashSet<>();

}
