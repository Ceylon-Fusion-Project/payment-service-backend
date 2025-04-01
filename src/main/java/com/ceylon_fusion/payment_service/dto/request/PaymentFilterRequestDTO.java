package com.ceylon_fusion.payment_service.dto.request;

import com.ceylon_fusion.payment_service.entity.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentFilterRequestDTO {
    @Builder.Default
    private int page=0;
    @Builder.Default
    private int size=10;
    private Long userId;
    private Long orderId;
    private Long bookingId;
    private  PaymentStatus status;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @Builder.Default
    private String sortBy = "paymentDate";
    @Builder.Default
    private String sortDirection= "DESC";


}
