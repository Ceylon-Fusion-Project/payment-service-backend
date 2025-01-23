package com.ceylon_fusion.payment_service.controller;


import com.ceylon_fusion.payment_service.dto.PaymentMethodDTO;
import com.ceylon_fusion.payment_service.dto.paginated.PaginatedPaymentMethodDTO;
import com.ceylon_fusion.payment_service.dto.request.CreatePaymentMethodRequestDTO;
import com.ceylon_fusion.payment_service.dto.response.PaymentMethodDetailsResponseDTO;
import com.ceylon_fusion.payment_service.service.PaymentMethodService;
import com.ceylon_fusion.payment_service.util.StandardResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("api/v1/payment-methods")
@SecurityRequirement(name="bearerAuth")
@RequiredArgsConstructor
public class PaymentMethodController {

    private final PaymentMethodService paymentMethodService;

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Create a new payment method")
    public ResponseEntity<StandardResponse> createPaymentMethod(
            @RequestBody CreatePaymentMethodRequestDTO request) {
        try {
            PaymentMethodDetailsResponseDTO response = paymentMethodService.createPaymentMethod(request);
            return new ResponseEntity<>(
                    new StandardResponse(201, "Payment method created successfully", response),
                    HttpStatus.CREATED
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new StandardResponse(400, e.getMessage(), null),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/details/{id} ")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Get payment method by ID")
    public ResponseEntity<StandardResponse> getPaymentMethodById(@PathVariable Long id) {
        try {
            PaymentMethodDetailsResponseDTO response = paymentMethodService.getPaymentMethodById(id);
            return new ResponseEntity<>(
                    new StandardResponse(200, "Payment method retrieved successfully", response),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new StandardResponse(404, e.getMessage(), null),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Get all payment methods for a user")
    public ResponseEntity<StandardResponse> getUserPaymentMethods(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            var response = paymentMethodService.getUserPaymentMethods(userId, page, size);
            return new ResponseEntity<>(
                    new StandardResponse(200, "Payment methods retrieved successfully", response),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new StandardResponse(400, e.getMessage(), null),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update payment method (Admin only)")
    public ResponseEntity<StandardResponse> updatePaymentMethod(
            @PathVariable Long id,
            @RequestBody CreatePaymentMethodRequestDTO request) {
        try {
            PaymentMethodDetailsResponseDTO response = paymentMethodService.updatePaymentMethod(id, request);
            return new ResponseEntity<>(
                    new StandardResponse(200, "Payment method updated successfully", response),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new StandardResponse(400, e.getMessage(), null),
                    HttpStatus.BAD_REQUEST
            );
        }
    }


    @DeleteMapping("/remove/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete payment method (Admin only)")
    public ResponseEntity<StandardResponse> deletePaymentMethod(@PathVariable Long id) {
        try {
            paymentMethodService.deletePaymentMethod(id);
            return new ResponseEntity<>(
                    new StandardResponse(200, "Payment method deleted successfully", null),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new StandardResponse(400, e.getMessage(), null),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}
