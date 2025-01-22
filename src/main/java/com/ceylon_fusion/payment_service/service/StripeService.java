package com.ceylon_fusion.payment_service.service;

public interface StripeService {
    void validateWebhookSignature(String payload, String sigHeader);
}
