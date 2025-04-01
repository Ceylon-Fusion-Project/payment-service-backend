package com.ceylon_fusion.payment_service.dto.stripe;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePaymentIntentRequest {
    private Long amount;
    private String currency;
    private String paymentMethodId;
    private String description;
}
