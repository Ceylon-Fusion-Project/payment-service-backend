package com.ceylon_fusion.payment_service.controller;


import com.ceylon_fusion.payment_service.dto.analytics.PaymentAnalyticsDTO;
import com.ceylon_fusion.payment_service.dto.request.CreatePaymentRequestDTO;
import com.ceylon_fusion.payment_service.dto.request.PaymentFilterRequestDTO;
import com.ceylon_fusion.payment_service.dto.request.UpdatePaymentRequestDTO;
import com.ceylon_fusion.payment_service.dto.response.PaymentDetailsResponseDTO;
import com.ceylon_fusion.payment_service.service.PaymentService;
import com.ceylon_fusion.payment_service.util.StandardResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


@RestController
@RequestMapping("api/v1/payments")
@CrossOrigin
@Slf4j
@Builder
@SecurityRequirement(name = "bearerAuth")
public class PaymentController {

    @Autowired
    private  PaymentService paymentService;


    @PostMapping(path = "/process-order")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Create a new Order payment")
    public ResponseEntity<StandardResponse> saveOrderPayment(
            @RequestBody CreatePaymentRequestDTO createPaymentRequestDTO) {
        try {
            PaymentDetailsResponseDTO response = paymentService.saveOrderPayment(createPaymentRequestDTO);

            return new ResponseEntity<>(
                    new StandardResponse(201, "Order Payment Saved Successfully", response.getPaymentId()),
                    HttpStatus.CREATED
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new StandardResponse(409, e.getMessage(), null),
                    HttpStatus.CONFLICT
            );
        }
    }

    @PostMapping(path = "/process-booking")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Create a new Booking payment")
    public ResponseEntity<StandardResponse> saveBookingPayment(
            @RequestBody CreatePaymentRequestDTO createPaymentRequestDTO) {
        try {
            PaymentDetailsResponseDTO response = paymentService.saveBookingPayment(createPaymentRequestDTO);

            return new ResponseEntity<>(
                    new StandardResponse(201, "Booking Payment Saved Successfully", response.getPaymentId()),
                    HttpStatus.CREATED
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new StandardResponse(409, e.getMessage(), null),
                    HttpStatus.CONFLICT
            );
        }
    }

    @GetMapping(path = "/details/{paymentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
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

    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
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

    @GetMapping("/analytics/metrics ")
    @PreAuthorize("hasRole('ADMIN')")
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

    @GetMapping("/filter")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
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

    @PutMapping("/update/{paymentId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update payment")
    public ResponseEntity<StandardResponse> updatePayment(
            @PathVariable Long paymentId,
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
    @DeleteMapping("/{paymentId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete payment")
    public ResponseEntity<StandardResponse> deletePayment(@PathVariable Long paymentId) {
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
    @PutMapping("/remove/{paymentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Cancel an order payment using order ID")
    public ResponseEntity<StandardResponse> cancelOrderPaymentByOrderId(
            @PathVariable Long orderId) {
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
}