package com.ceylon_fusion.payment_service.dto;

import com.ceylon_fusion.payment_service.entity.enums.PaymentStatus;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {

    private Long paymentId;
    private Long userId;
    private Double amount;
    private PaymentStatus paymentStatus;

}
