package com.ceylon_fusion.payment_service.dto.response;

import com.ceylon_fusion.payment_service.entity.enums.RefundStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefundDetailsResponseDTO {

    private Long refundId;
    private Long paymentId;
    private RefundStatus refundStatus;
    private LocalDateTime transactionDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
