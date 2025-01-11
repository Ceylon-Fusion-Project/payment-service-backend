package com.ceylon_fusion.payment_service.dto;

import com.ceylon_fusion.payment_service.entity.enums.RefundStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefundDTO {

    private Long refundId;
    private String paymentId;
    private RefundStatus refundStatus;


}
