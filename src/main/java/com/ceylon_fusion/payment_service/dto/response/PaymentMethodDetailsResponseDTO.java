package com.ceylon_fusion.payment_service.dto.response;

import com.ceylon_fusion.payment_service.entity.enums.PaymentMethodsStatus;
import com.ceylon_fusion.payment_service.entity.enums.Provider;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentMethodDetailsResponseDTO {

    private Long paymentMethodId;
    private String methodName;
    private Boolean isActive;
    private Boolean isDefault;
    private Provider provider;
    private String maskedDetails;
    private PaymentMethodsStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
