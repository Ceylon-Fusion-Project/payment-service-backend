package com.ceylon_fusion.payment_service.util.mappers;

import com.ceylon_fusion.payment_service.dto.PaymentMethodDTO;
import com.ceylon_fusion.payment_service.dto.request.CreatePaymentMethodRequestDTO;
import com.ceylon_fusion.payment_service.dto.response.PaymentMethodDetailsResponseDTO;
import com.ceylon_fusion.payment_service.entity.PaymentMethod;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PaymentMethodMapper {

    // Entity to DTO
    @org.mapstruct.Mapping(source = "methodName", target = "methodName")
    PaymentMethodDTO paymentMethodToPaymentMethodDTO(PaymentMethod paymentMethod);

    List<PaymentMethodDTO> paymentMethodsToPaymentMethodDTOs(List<PaymentMethod> paymentMethods);

    // Entity to Response DTO
    @org.mapstruct.Mapping(source = "methodName", target = "methodName")
    @org.mapstruct.Mapping(source = "isActive", target = "isActive")
    @org.mapstruct.Mapping(source = "isDefault", target = "isDefault")
    @org.mapstruct.Mapping(source = "provider", target = "provider")
    @org.mapstruct.Mapping(source = "maskedDetails", target = "maskedDetails")
    @org.mapstruct.Mapping(source = "status", target = "status")
    @org.mapstruct.Mapping(source = "createdAt", target = "createdAt")
    @org.mapstruct.Mapping(source = "updatedAt", target = "updatedAt")
    PaymentMethodDetailsResponseDTO paymentMethodToPaymentMethodDetailsResponseDTO(PaymentMethod paymentMethod);

    // Request DTO to Entity
    @org.mapstruct.Mapping(source = "methodName", target = "methodName")
    @org.mapstruct.Mapping(source = "isActive", target = "isActive")
    @org.mapstruct.Mapping(source = "isDefault", target = "isDefault")
    @org.mapstruct.Mapping(source = "userId", target = "userId")
    @org.mapstruct.Mapping(source = "provider", target = "provider")
    @org.mapstruct.Mapping(source = "maskedDetails", target = "maskedDetails")
    PaymentMethod createPaymentMethodRequestDTOToPaymentMethod(CreatePaymentMethodRequestDTO createPaymentMethodRequestDTO);

}
