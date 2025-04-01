//package com.ceylon_fusion.payment_service.controller;
//
//
//import com.ceylon_fusion.payment_service.dto.PaymentMethodDTO;
//import com.ceylon_fusion.payment_service.dto.paginated.PaginatedPaymentMethodDTO;
//import com.ceylon_fusion.payment_service.dto.request.CreatePaymentMethodRequestDTO;
//import com.ceylon_fusion.payment_service.dto.response.PaymentMethodDetailsResponseDTO;
//import com.ceylon_fusion.payment_service.dto.response.StandardResponseDTO;
////import com.ceylon_fusion.payment_service.service.PaymentMethodService;
//import com.ceylon_fusion.payment_service.util.StandardResponse;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.security.SecurityRequirement;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
////import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//
//@CrossOrigin
//@RestController
//@RequestMapping("api/v1/payment-methods")
////@SecurityRequirement(name="bearerAuth")
//@RequiredArgsConstructor
//public class PaymentMethodController {
//
//    private final PaymentMethodService paymentMethodService;
//
//    @PostMapping("/create-payment-method")
//    //@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
//    @Operation(summary = "Create a new payment method")
//    public ResponseEntity<StandardResponseDTO> createPaymentMethod(
//            @RequestBody CreatePaymentMethodRequestDTO request) {
//        try {
//            PaymentMethodDetailsResponseDTO response = paymentMethodService.createPaymentMethod(request);
//
//            StandardResponseDTO standardResponse = new StandardResponseDTO(
//                    true,
//                    response.getPaymentMethodId(),
//                    null,
//
//                    null
//            );
//
//            return ResponseEntity
//                    .status(HttpStatus.CREATED)
//                    .body(standardResponse);
//
//        } catch (Exception e) {
//            // Create error response
//            StandardResponseDTO errorResponse = new StandardResponseDTO(
//                    false,
//                    null,
//                    null,
//                    null
//            );
//
//            return ResponseEntity
//                    .status(HttpStatus.BAD_REQUEST)
//                    .body(errorResponse);
//        }
//    }
//
//    @GetMapping(path = "/get-payment-method-details",params = "id")
//    //@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
//    @Operation(summary = "Get payment method by ID")
//    public ResponseEntity<StandardResponse> getPaymentMethodById(@RequestParam Long id) {
//        try {
//            PaymentMethodDetailsResponseDTO response = paymentMethodService.getPaymentMethodById(id);
//            return ResponseEntity.ok(new StandardResponse(200, "Payment method retrieved successfully", response));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(new StandardResponse(404, e.getMessage(), null));
//        }
//    }
//
//    @GetMapping(path = "/all-payment-methods-for-user",params = "userId")
//    //@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
//    @Operation(summary = "Get all payment methods for a user")
//    public ResponseEntity<StandardResponse> getUserPaymentMethods(
//            @RequestParam Long userId,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size) {
//        try {
//            Object response = paymentMethodService.getUserPaymentMethods(userId, page, size);
//            return ResponseEntity.ok(new StandardResponse(200, "Payment methods retrieved successfully", response));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body(new StandardResponse(400, e.getMessage(), null));
//        }
//    }
//
//    @PatchMapping(path = "/update-payment-method",params = "id")
//    //@PreAuthorize("hasRole('ADMIN')")
//    @Operation(summary = "Update payment method (Admin only)")
//    public ResponseEntity<StandardResponse> updatePaymentMethod(
//            @RequestParam Long id,
//            @RequestBody CreatePaymentMethodRequestDTO request) {
//        try {
//            PaymentMethodDetailsResponseDTO response = paymentMethodService.updatePaymentMethod(id, request);
//            return new ResponseEntity<>(
//                    new StandardResponse(200, "Payment method updated successfully", response),
//                    HttpStatus.OK
//            );
//        } catch (Exception e) {
//            return new ResponseEntity<>(
//                    new StandardResponse(400, e.getMessage(), null),
//                    HttpStatus.BAD_REQUEST
//            );
//        }
//    }
//
//
//    @DeleteMapping(path = "/delete-payment-method",params ="id")
//    //@PreAuthorize("hasRole('ADMIN')")
//    @Operation(summary = "Delete payment method (Admin only)")
//    public ResponseEntity<StandardResponse> deletePaymentMethod(@RequestParam Long id) {
//        try {
//            paymentMethodService.deletePaymentMethod(id);
//            return new ResponseEntity<>(
//                    new StandardResponse(200, "Payment method deleted successfully", null),
//                    HttpStatus.OK
//            );
//        } catch (Exception e) {
//            return new ResponseEntity<>(
//                    new StandardResponse(400, e.getMessage(), null),
//                    HttpStatus.BAD_REQUEST
//            );
//        }
//    }
//}
