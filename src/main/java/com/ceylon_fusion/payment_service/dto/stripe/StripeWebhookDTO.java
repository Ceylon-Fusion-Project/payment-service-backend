package com.ceylon_fusion.payment_service.dto.stripe;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StripeWebhookDTO {
    private String eventType;
    private String stripePaymentIntentId;
    private String status;
    private Double amount;
}
