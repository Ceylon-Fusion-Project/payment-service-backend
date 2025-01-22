package com.ceylon_fusion.payment_service.dto.response;

import com.ceylon_fusion.payment_service.entity.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDetailsResponseDTO {
    private Long paymentId;
    private Long userId;
    private Long orderId;
    private Long bookingId;
    private PaymentStatus paymentStatus;
    private LocalDateTime paymentDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String stripePaymentIntentId;
    private String stripeClientSecret;
}
