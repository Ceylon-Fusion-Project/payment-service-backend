package com.ceylon_fusion.payment_service.service;

import com.stripe.model.PaymentIntent;
import com.stripe.model.Refund;

public interface StripeService {
    PaymentIntent createPaymentIntent(Double amount, String currency, String paymentMethodId);
    PaymentIntent confirmPayment(String paymentIntentId);
      Refund createRefund(String paymentIntentId, Double amount);
    void validateWebhookSignature(String payload, String sigHeader);
    String attachPaymentMethod(String customerId, String paymentMethodId);

    PaymentIntent retrievePaymentIntent(String paymentIntentId);
}