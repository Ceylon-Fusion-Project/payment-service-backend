package com.ceylon_fusion.payment_service.util.mappers;

import com.ceylon_fusion.payment_service.dto.RefundDTO;
import com.ceylon_fusion.payment_service.dto.request.InitiateRefundRequestDTO;
import com.ceylon_fusion.payment_service.dto.response.RefundDetailsResponseDTO;
import com.ceylon_fusion.payment_service.entity.Refund;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public class RefundMapper {

    // Entity to DTO
    @org.mapstruct.Mapping(source = "refundId", target = "refundId")
    @org.mapstruct.Mapping(source = "paymentId", target = "paymentId")
    @org.mapstruct.Mapping(source = "refundStatus", target = "refundStatus")
    RefundDTO refundToRefundDTO(Refund refund) {
        return null;
    }

    List<RefundDTO> refundsToRefundDTOs(List<Refund> refunds) {
        return null;
    }

    // Entity to Response DTO
    @org.mapstruct.Mapping(source = "refundId", target = "refundId")
    @org.mapstruct.Mapping(source = "paymentId", target = "paymentId")
    @org.mapstruct.Mapping(source = "refundStatus", target = "refundStatus")
    @org.mapstruct.Mapping(source = "transactionDate", target = "transactionDate")
    @org.mapstruct.Mapping(source = "createdAt", target = "createdAt")
    @org.mapstruct.Mapping(source = "updatedAt", target = "updatedAt")
    RefundDetailsResponseDTO refundToRefundDetailsResponseDTO(Refund refund) {
        return null;
    }

    // Request DTO to Entity
    @org.mapstruct.Mapping(source = "paymentId", target = "paymentId")
    @org.mapstruct.Mapping(source = "refundReason", target = "refundReason")
    Refund initiateRefundRequestDTOToRefund(InitiateRefundRequestDTO initiateRefundRequestDTO) {
        return null;
    }
}
