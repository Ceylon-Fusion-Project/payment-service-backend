package com.ceylon_fusion.payment_service.controller;


import com.ceylon_fusion.payment_service.service.PaymentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/payments")
@CrossOrigin
@Slf4j
@Builder
@SecurityRequirement(name = "bearerAuth")
public class PaymentController {

    @Autowired
    private  PaymentService paymentService;


}