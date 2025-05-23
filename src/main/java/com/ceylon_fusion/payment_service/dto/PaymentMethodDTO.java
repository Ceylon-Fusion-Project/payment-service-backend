package com.ceylon_fusion.payment_service.dto;

import com.ceylon_fusion.payment_service.entity.enums.Provider;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentMethodDTO {

    private Long paymentMethodId;
    private String methodName;
    private Boolean isActive;
    private String maskedDetails;
    private Provider provider;
}
