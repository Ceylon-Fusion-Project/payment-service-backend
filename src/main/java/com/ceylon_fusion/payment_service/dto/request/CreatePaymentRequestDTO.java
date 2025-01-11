package com.ceylon_fusion.payment_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePaymentRequestDTO {
    private Long userId;
    private Long orderId;
    private Long bookingId;
    private Double amount;

}
