package com.ceylon_fusion.payment_service.dto.request;

import com.ceylon_fusion.payment_service.entity.enums.Provider;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePaymentMethodRequestDTO {

    private String methodNAme;
    private Boolean isActive;
    private Boolean isDefault;
    private Long userId;
    private Provider provider;
    private String maskedDetails;
}
