package com.ceylon_fusion.payment_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InitiateRefundRequestDTO {

    private String paymentId;
    private String refundReason;
}
