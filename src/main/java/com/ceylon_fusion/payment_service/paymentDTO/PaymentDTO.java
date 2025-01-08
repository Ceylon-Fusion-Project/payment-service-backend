package com.ceylon_fusion.payment_service.paymentDTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {

    private Long id;

    @NotNull
    private Long userId;
    private Long orderId;
    private Long bookingId;

    @DecimalMin(value="0.0" ,inclusive = false)
    @NotNull
    private Double amount;

    @NotEmpty
    @Pattern(regexp="CARD" , message="Invalid payment method")
    private String paymentMethod;

    @NotEmpty
    private PaymentMethodDTO paymentMethodDTO;

    @NotNull
    private LocalDateTime paymentDate;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;



}
