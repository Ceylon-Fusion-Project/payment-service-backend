package com.ceylon_fusion.payment_service.dto.analytics;

import com.ceylon_fusion.payment_service.entity.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentAnalyticsDTO {
    private BigDecimal totalAmount;
    private Long totalTransactions;

    private Map<PaymentStatus, Long> statusCounts;
    private Map<PaymentStatus, BigDecimal> amountByStatus;

    private BigDecimal averageTransactionAmount;

    private Map<String, BigDecimal> dailyTotalAmounts;
    private Map<String, Long> dailyTransactionCounts;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private BigDecimal successRate;
    private BigDecimal failureRate;
}
