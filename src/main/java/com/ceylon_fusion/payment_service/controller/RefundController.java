package com.ceylon_fusion.payment_service.controller;

import com.ceylon_fusion.payment_service.dto.request.InitiateRefundRequestDTO;
import com.ceylon_fusion.payment_service.dto.response.StandardResponseDTO;
import com.ceylon_fusion.payment_service.service.RefundService;
import com.ceylon_fusion.payment_service.util.StandardResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("api/v1/refunds")
@CrossOrigin
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class RefundController {

    private final RefundService refundService;

    @PostMapping("/initiate")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Initiate a refund (Admin only)")
    public ResponseEntity<StandardResponse> initiateRefund(
            @RequestBody InitiateRefundRequestDTO request) {
        try {
            Object response = refundService.initiateRefund(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new StandardResponse(201, "Refund initiated successfully", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new StandardResponse(400, e.getMessage(), null));
        }
    }
    @GetMapping("/details/{refundId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Get refund by ID")
    public ResponseEntity<StandardResponse> getRefundById(@PathVariable Long refundId) {
        try {
            Object response = refundService.getRefundById(refundId);
            return ResponseEntity.ok(new StandardResponse(200, "Refund retrieved successfully", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new StandardResponse(404, e.getMessage(), null));
        }
    }

    @GetMapping("/list ")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Get all refunds with pagination")
    public ResponseEntity<StandardResponse> getAllRefunds(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "transactionDate") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {
        try {
            var response = refundService.getAllRefunds(page, size, sortBy, sortDirection);
            return new ResponseEntity<>(
                    new StandardResponse(200, "Refunds retrieved successfully", response),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new StandardResponse(500, e.getMessage(), null),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
    @GetMapping("/filter")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Get refunds by date range")
    public ResponseEntity<StandardResponse> getRefundsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false) Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            var response = refundService.getRefundsByDateRange(startDate, endDate, userId, page, size);
            return new ResponseEntity<>(
                    new StandardResponse(200, "Refunds retrieved successfully", response),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new StandardResponse(400, e.getMessage(), null),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
    @PutMapping("/status/{refundId} ")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update refund status (Admin only)")
    public ResponseEntity<StandardResponse> updateRefundStatus(
            @PathVariable Long refundId,
            @RequestParam String status) {
        try {
            var response = refundService.updateRefundStatus(refundId, status);
            return new ResponseEntity<>(
                    new StandardResponse(200, "Refund status updated successfully", response),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new StandardResponse(400, e.getMessage(), null),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
    @DeleteMapping("cancel/{refundId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Cancel refund (Admin only)")
    public ResponseEntity<StandardResponse> cancelRefund(@PathVariable Long refundId) {
        try {
            refundService.cancelRefund(refundId);
            return new ResponseEntity<>(
                    new StandardResponse(200, "Refund cancelled successfully", null),
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
