package com.ceylon_fusion.payment_service.service;

import com.ceylon_fusion.payment_service.dto.request.InitiateRefundRequestDTO;

import java.time.LocalDateTime;

public interface RefundService {
    Object initiateRefund(InitiateRefundRequestDTO request);

    Object getRefundById(Long refundId);

    Object getAllRefunds(int page, int size, String sortBy, String sortDirection);

    Object getRefundsByDateRange(LocalDateTime startDate, LocalDateTime endDate, Long userId, int page, int size);

    Object updateRefundStatus(Long refundId, String status);

    void cancelRefund(Long refundId);
}
