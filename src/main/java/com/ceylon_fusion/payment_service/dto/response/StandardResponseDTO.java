package com.ceylon_fusion.payment_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StandardResponseDTO {
    private boolean success;
    private Long paymentId;
    private Long orderId;
    private Long bookingId;
}
