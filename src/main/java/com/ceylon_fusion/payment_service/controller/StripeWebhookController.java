package com.ceylon_fusion.payment_service.controller;


import com.ceylon_fusion.payment_service.dto.stripe.StripeWebhookDTO;
import com.ceylon_fusion.payment_service.service.PaymentService;
import com.ceylon_fusion.payment_service.service.StripeService;
import com.ceylon_fusion.payment_service.util.StandardResponse;
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



}
