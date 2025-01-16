package com.ceylon_fusion.payment_service.dto.request;

import com.ceylon_fusion.payment_service.entity.enums.PaymentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePaymentRequestDTO {

    private Long paymentId;
    @NotNull(message = "Payment status cannot be null")
    private PaymentStatus paymentStatus;
    private Double amount;
    private LocalDateTime paymentDate;
    private Long paymentMethodId;

}
