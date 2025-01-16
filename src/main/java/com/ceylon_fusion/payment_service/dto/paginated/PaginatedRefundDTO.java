package com.ceylon_fusion.payment_service.dto.paginated;

import com.ceylon_fusion.payment_service.dto.RefundDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginatedRefundDTO {
    List<RefundDTO> refundsDTOs;
    private long totalItems;
}
