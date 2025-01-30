package com.ceylon_fusion.payment_service.controller;


import com.ceylon_fusion.payment_service.service.PaymentService;
import com.ceylon_fusion.payment_service.service.StripeService;
import com.ceylon_fusion.payment_service.util.StandardResponse;
import com.stripe.model.Event;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @PostMapping("/handle-stripe-webhook")
    public ResponseEntity<StandardResponse> handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {
        try {
            stripeService.validateWebhookSignature(payload, sigHeader);
            // Process webhook event
            return ResponseEntity.ok(new StandardResponse(200, "Webhook processed", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new StandardResponse(400, e.getMessage(), null));
        }
    }
}
