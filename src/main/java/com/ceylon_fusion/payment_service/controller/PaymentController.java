package com.ceylon_fusion.payment_service.controller;


import com.ceylon_fusion.payment_service.dto.analytics.PaymentAnalyticsDTO;
import com.ceylon_fusion.payment_service.dto.request.*;
import com.ceylon_fusion.payment_service.dto.response.PaymentDetailsResponseDTO;
import com.ceylon_fusion.payment_service.dto.response.StandardResponseDTO;
import com.ceylon_fusion.payment_service.dto.stripe.CreatePaymentIntentRequest;
import com.ceylon_fusion.payment_service.dto.stripe.PaymentIntentResponse;
import com.ceylon_fusion.payment_service.entity.Payment;
//import com.ceylon_fusion.payment_service.entity.PaymentMethod;
import com.ceylon_fusion.payment_service.entity.enums.PaymentStatus;
//import com.ceylon_fusion.payment_service.repo.PaymentMethodRepo;
import com.ceylon_fusion.payment_service.repo.PaymentRepo;
import com.ceylon_fusion.payment_service.service.PaymentService;
import com.ceylon_fusion.payment_service.service.StripeService;
import com.ceylon_fusion.payment_service.util.StandardResponse;
import com.ceylon_fusion.payment_service.util.mappers.PaymentMapper;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("api/v1/payments")
@CrossOrigin
@Slf4j
@Builder
//@SecurityRequirement(name = "bearerAuth")
public class PaymentController {

    //private final PaymentMethodRepo paymentMethodRepo;
    private  final PaymentService paymentService;
    private final StripeService stripeService;
    private final PaymentRepo paymentRepo;
    private final PaymentMapper paymentMapper;
    @Value("${stripe.secret.key}")
    private String stripeSecretKey;


    @PostMapping(path = "/payment-process-order")
   // @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Create a new Order payment")
    public ResponseEntity<StandardResponseDTO> saveOrderPayment(
            @RequestBody OrderRequestDTO orderRequestDTO) {
        try {
            // Convert OrderRequestDTO to CreatePaymentRequestDTO
            CreatePaymentRequestDTO createPaymentRequestDTO = paymentMapper.orderRequestDTOToCreatePaymentRequestDTO(orderRequestDTO);

            // Process the payment
            PaymentDetailsResponseDTO response = paymentService.saveOrderPayment(createPaymentRequestDTO);

            // Create success response
            StandardResponseDTO standardResponse = new StandardResponseDTO(
                    true,  // success flag
                    response.getPaymentId(),  // payment ID
                    orderRequestDTO.getOrderId(),  // order ID
                    null  // booking ID (null for order payments)
            );

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(standardResponse);

        } catch (Exception e) {
            // Create error response
            StandardResponseDTO errorResponse = new StandardResponseDTO(
                    false,  // success flag
                    null,   // payment ID
                    orderRequestDTO.getOrderId(),  // order ID
                    null    // booking ID
            );

            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(errorResponse);
        }
    }
    @PostMapping(path = "/payment-process-booking")
    //@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Create a new Booking payment")
    public ResponseEntity<StandardResponseDTO> saveBookingPayment(
            @RequestBody BookingRequestDTO bookingRequestDTO) {
        try {
            CreatePaymentRequestDTO createPaymentRequestDTO = paymentMapper.bookingRequestDTOToCreatePaymentRequestDTO(bookingRequestDTO);

            // Process the payment
            PaymentDetailsResponseDTO response = paymentService.saveBookingPayment(createPaymentRequestDTO);

            // Create success response
            StandardResponseDTO standardResponse = new StandardResponseDTO(
                    true,  // success flag
                    response.getPaymentId(),  // payment ID
                    null,//orderId
                    bookingRequestDTO.getBookingId() //bookingId
            );

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(standardResponse);

        } catch (Exception e) {
            // Create error response
            StandardResponseDTO errorResponse = new StandardResponseDTO(
                    false,  // success flag
                    null,   // payment ID
                   null,  // order ID
                    bookingRequestDTO.getBookingId()   // booking ID
            );

            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(errorResponse);
        }
    }


    @GetMapping(path = "/payment-details", params = "paymentId")
    //@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Get payment by ID")
    public ResponseEntity<StandardResponse> getPaymentById(
            @RequestParam(value = "paymentId") Long paymentId
    ) {
        try {
            PaymentDetailsResponseDTO response = paymentService.getPaymentById(paymentId);
            return new ResponseEntity<>(
                    new StandardResponse(200, "Payment Found", response),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new StandardResponse(404, e.getMessage(), null),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping("/payment-filtering-and-pagination")
   // @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Get all payments with filtering and pagination")
    public ResponseEntity<StandardResponse> getAllPayments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long orderId,
            @RequestParam(required = false) Long bookingId,
            @RequestParam(defaultValue = "paymentDate") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {

        try {
            PaymentFilterRequestDTO filterRequestDTO = PaymentFilterRequestDTO.builder()
                    .page(page)
                    .size(size)
                    .userId(userId)
                    .orderId(orderId)
                    .bookingId(bookingId)
                    .sortBy(sortBy)
                    .sortDirection(sortDirection)
                    .build();

            return ResponseEntity.ok(new StandardResponse(
                    200,
                    "Payments retrieved successfully",
                    paymentService.getAllPayments(filterRequestDTO)
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new StandardResponse(500, e.getMessage(), null));
        }
    }

    @GetMapping("/payment-analytics-and-metrics ")
    //@PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get payment analytics within date range")
    public ResponseEntity<StandardResponse> getPaymentAnalytics(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime startDate,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime endDate,

            @RequestParam(required = false) Long userId
    ) {
        try {
            // If dates not provided, use last 30 days as default
            LocalDateTime end = endDate != null ? endDate : LocalDateTime.now();
            LocalDateTime start = startDate != null ? startDate : end.minusDays(30);

            PaymentAnalyticsDTO analytics = paymentService.getPaymentAnalytics(
                    start, end, userId
            );

            return ResponseEntity.ok(new StandardResponse(
                    200,
                    "Payment analytics retrieved successfully",
                    analytics
            ));
        } catch (Exception e) {
            log.error("Error retrieving payment analytics: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new StandardResponse(500, e.getMessage(), null));
        }
    }

    @GetMapping("/payment-filter-and-pagination")
    //@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Get payments within date range with filtering and pagination")
    public ResponseEntity<StandardResponse> getPaymentsByDateRange(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime startDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime endDate,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long orderId,
            @RequestParam(required = false) Long bookingId,
            @RequestParam(defaultValue = "paymentDate") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection
    ) {
        try {
            PaymentFilterRequestDTO filterRequestDTO = PaymentFilterRequestDTO.builder()
                    .page(page)
                    .size(size)
                    .userId(userId)
                    .orderId(orderId)
                    .bookingId(bookingId)
                    .startDate(startDate)
                    .endDate(endDate)
                    .sortBy(sortBy)
                    .sortDirection(sortDirection)
                    .build();

            return ResponseEntity.ok(new StandardResponse(
                    200,
                    "Payments retrieved successfully",
                    paymentService.getPaymentsWithDateRange(filterRequestDTO)
            ));
        } catch (Exception e) {
            log.error("Error retrieving payments: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new StandardResponse(500, e.getMessage(), null));
        }
    }

    @PatchMapping(path="/update-payment", params = "paymentId")
   // @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update payment")
    public ResponseEntity<StandardResponse> updatePayment(
            @RequestParam Long paymentId,
            @Valid @RequestBody UpdatePaymentRequestDTO updatePaymentRequestDTO) {
        try {
            return ResponseEntity.ok(new StandardResponse(
                    200,
                    "Payment updated successfully",
                    paymentService.updatePayment(paymentId, updatePaymentRequestDTO)
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new StandardResponse(400, e.getMessage(), null));
        }
    }
    @DeleteMapping(path = "/delete-payment", params = "paymentId")
    //@PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete payment")
    public ResponseEntity<StandardResponse> deletePayment(@RequestParam Long paymentId) {
        try {
            paymentService.deletePayment(paymentId);
            return ResponseEntity.ok(new StandardResponse(
                    200,
                    "Payment deleted successfully",
                    null
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new StandardResponse(404, e.getMessage(), null));
        }
    }
    // Update PaymentController with new endpoint
    @PatchMapping(path = "/cancel-order-payment", params = "orderId")
   // @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Cancel an order payment using order ID")
    public ResponseEntity<StandardResponse> cancelOrderPaymentByOrderId(
            @RequestParam Long orderId) {
        try {
            PaymentDetailsResponseDTO response = paymentService.cancelOrderPaymentByOrderId(orderId);
            return new ResponseEntity<>(
                    new StandardResponse(
                            200,
                            "Order payment cancelled successfully",
                            response
                    ),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new StandardResponse(
                            400,
                            e.getMessage(),
                            null
                    ),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
    @PostMapping("/create-stripe-payment-intent")
   // @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Create a Stripe payment intent")
    public ResponseEntity<StandardResponse> createPaymentIntent(
            @RequestBody CreatePaymentIntentRequest request) {
        try {
            PaymentIntent intent = stripeService.createPaymentIntent(
                    request.getAmount().doubleValue(),
                    request.getCurrency(),
                    request.getPaymentMethodId()
            );

            PaymentIntentResponse response = new PaymentIntentResponse(
                    intent.getClientSecret(),
                    intent.getId(),
                    intent.getStatus()
            );

            return ResponseEntity.ok(new StandardResponse(
                    200,
                    "Payment intent created successfully",
                    response
            ));
        } catch (Exception e) {
            log.error("Error creating payment intent: ", e);
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new StandardResponse(400, e.getMessage(), null));
        }
    }
    @PostMapping(path = "/confirm-stripe-payment",params = "paymentIntentId")
   // @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Confirm a Stripe payment")
    public ResponseEntity<StandardResponse> confirmPayment(
            @RequestParam String paymentIntentId) {
        try {
            PaymentIntent confirmedIntent = stripeService.confirmPayment(paymentIntentId);
            return ResponseEntity.ok(new StandardResponse(
                    200,
                    "Payment confirmed successfully",
                    confirmedIntent.getStatus()
            ));
        } catch (Exception e) {
            log.error("Error confirming payment: ", e);
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new StandardResponse(400, e.getMessage(), null));
        }
    }
    @GetMapping(path = "/get-payment-intent",params = "paymentIntentId")
    //@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<StandardResponse> getPaymentIntent(@RequestParam String paymentIntentId) {
        try {
            PaymentIntent intent = stripeService.retrievePaymentIntent(paymentIntentId);
            return ResponseEntity.ok(new StandardResponse(200, "Payment intent retrieved",
                    new PaymentIntentResponse(intent.getClientSecret(), intent.getId(), intent.getStatus())));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new StandardResponse(404, e.getMessage(), null));
        }
    }

//    @PostMapping("/create-payment-intent")
//    public Map<String, String> createPaymentIntent(@RequestBody Map<String, Object> data) throws StripeException {
//        Stripe.apiKey = stripeSecretKey;
//
//        Long amount = Long.parseLong(data.get("amount").toString()); // amount in cents
//
//        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
//                .setAmount(amount)
//                .setCurrency("usd")
//                .build();
//
//        PaymentIntent paymentIntent = PaymentIntent.create(params);
//        Map<String, String> responseData = new HashMap<>();
//        responseData.put("clientSecret", paymentIntent.getClientSecret());
//
//        return responseData;
//    }

    @PostMapping("/create-payment-intent")
    public ResponseEntity<?> createPaymentIntent(@RequestBody Map<String, Object> data) {
        try {
            Stripe.apiKey = stripeSecretKey;

            if (!data.containsKey("amount")) {
                return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Amount is required"));
            }

            Long amount = Long.parseLong(data.get("amount").toString());

            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(amount)
                    .setCurrency("usd")
                    .addPaymentMethodType("card")
                    .build();

            PaymentIntent paymentIntent = PaymentIntent.create(params);

            return ResponseEntity.ok(Collections.singletonMap("clientSecret", paymentIntent.getClientSecret()));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Invalid amount format"));
        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Payment processing failed: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Collections.singletonMap("error", "Server error: " + e.getMessage()));
        }
    }
}