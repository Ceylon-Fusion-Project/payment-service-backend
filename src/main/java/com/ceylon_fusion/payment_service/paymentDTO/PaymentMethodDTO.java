package com.ceylon_fusion.payment_service.paymentDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentMethodDTO {

    private Long id;
    private String methodNAme;
    private Boolean isActive;
}
