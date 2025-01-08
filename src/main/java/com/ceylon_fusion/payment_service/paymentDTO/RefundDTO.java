package com.ceylon_fusion.payment_service.paymentDTO;

import com.ceylon_fusion.payment_service.payment.enums.RefundStatus;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefundDTO {

    private Long id;

    @NotNull
    private String paymentId;

    @NotEmpty
    private RefundStatus refundStatus;

    @NotEmpty
    private LocalDateTime transactionDate;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


}
