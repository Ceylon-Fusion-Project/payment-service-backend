package com.ceylon_fusion.payment_service.controller;


import com.ceylon_fusion.payment_service.service.PaymentService;
import com.ceylon_fusion.payment_service.service.StripeService;
import com.ceylon_fusion.payment_service.util.StandardResponse;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/stripe")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin
public class StripeWebhookController {

    private final PaymentService paymentService;
    private final StripeService stripeService;

    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    @PostMapping("/handle-stripe-webhook")
    public ResponseEntity<StandardResponse> handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {
        try {
            // Verify the webhook signature
            Event event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
            String eventType = event.getType();
            EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();

            // Ensure the event data can be deserialized
            if (!dataObjectDeserializer.getObject().isPresent()) {
                log.error("Unable to deserialize webhook event: {}", eventType);
                return ResponseEntity.badRequest()
                        .body(new StandardResponse(400, "Unable to deserialize webhook event", null));
            }

            // Process the event
            StripeObject stripeObject = dataObjectDeserializer.getObject().get();
            stripeService.handleStripeEvent(eventType, stripeObject);

            return ResponseEntity.ok(new StandardResponse(200, "Webhook processed successfully", null));

        } catch (SignatureVerificationException e) {
            log.error("Invalid webhook signature: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new StandardResponse(400, "Invalid webhook signature", null));
        } catch (Exception e) {
            log.error("Error processing webhook: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new StandardResponse(500, "Error processing webhook", null));
        }
    }

}
