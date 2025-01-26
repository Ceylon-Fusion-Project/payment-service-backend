package com.ceylon_fusion.payment_service.service.serviceIMPL;

import com.ceylon_fusion.payment_service.service.StripeService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.RefundCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.stripe.model.Refund;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class StripeServiceIMPL implements StripeService {

    @Value("${secret_key}")
    private String secretKey;

//    @Value("${stripe.webhook.secret}")
//    private String webhookSecret;

    @Override
    public PaymentIntent createPaymentIntent(Double amount, String currency, String paymentMethodId) {
        try {
            Stripe.apiKey = secretKey;

            // Convert amount to cents/smallest currency unit
            Long stripeAmount = (long) (amount * 100);

            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(stripeAmount)
                    .setCurrency(currency.toLowerCase())
                    .setPaymentMethod(paymentMethodId)
                    .setConfirmationMethod(PaymentIntentCreateParams.ConfirmationMethod.MANUAL)
                    .setCaptureMethod(PaymentIntentCreateParams.CaptureMethod.AUTOMATIC)
                    .build();

            return PaymentIntent.create(params);
        } catch (StripeException e) {
            log.error("Error creating payment intent: {}", e.getMessage());
            throw new RuntimeException("Failed to create payment intent", e);
        }
    }

    @Override
    public PaymentIntent confirmPayment(String paymentIntentId) {
        try {
            Stripe.apiKey = secretKey;
            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
            Map<String, Object> params = new HashMap<>();
            return paymentIntent.confirm(params);
        } catch (StripeException e) {
            log.error("Error confirming payment: {}", e.getMessage());
            throw new RuntimeException("Failed to confirm payment", e);
        }
    }

    @Override
    public Refund createRefund(String paymentIntentId, Double amount) {
        try {
            Stripe.apiKey = secretKey;
            Long stripeAmount = (long) (amount * 100);

            RefundCreateParams params = RefundCreateParams.builder()
                    .setPaymentIntent(paymentIntentId)
                    .setAmount(stripeAmount)
                    .build();

            return Refund.create(params);
        } catch (StripeException e) {
            log.error("Error creating refund: {}", e.getMessage());
            throw new RuntimeException("Failed to create refund", e);
        }
    }

    @Override
    public void validateWebhookSignature(String payload, String sigHeader) {
        // Will implement webhook signature validation
    }

    @Override
    public String attachPaymentMethod(String customerId, String paymentMethodId) {
        try {
            Stripe.apiKey = secretKey;
            Map<String, Object> params = new HashMap<>();
            params.put("customer", customerId);

            com.stripe.model.PaymentMethod paymentMethod =
                    com.stripe.model.PaymentMethod.retrieve(paymentMethodId);
            paymentMethod.attach(params);

            return paymentMethod.getId();
        } catch (StripeException e) {
            log.error("Error attaching payment method: {}", e.getMessage());
            throw new RuntimeException("Failed to attach payment method", e);
        }
    }

    @Override
    public PaymentIntent retrievePaymentIntent(String paymentIntentId) {
        try {
            Stripe.apiKey = secretKey;

            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);

            if (paymentIntent == null) {
                throw new RuntimeException("Payment intent not found: " + paymentIntentId);
            }

            log.info("Retrieved payment intent: {}", paymentIntentId);
            return paymentIntent;

        } catch (StripeException e) {
            log.error("Error retrieving payment intent {}: {}", paymentIntentId, e.getMessage());
            throw new RuntimeException("Failed to retrieve payment intent", e);
        }

    }
}
