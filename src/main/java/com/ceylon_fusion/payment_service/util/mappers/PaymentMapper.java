package com.ceylon_fusion.payment_service.util.mappers;

import com.ceylon_fusion.payment_service.dto.PaymentDTO;
import com.ceylon_fusion.payment_service.dto.request.CreatePaymentRequestDTO;
import com.ceylon_fusion.payment_service.dto.request.PaymentFilterRequestDTO;
import com.ceylon_fusion.payment_service.dto.response.PaymentDetailsResponseDTO;
import com.ceylon_fusion.payment_service.dto.response.StandardResponseDTO;
import com.ceylon_fusion.payment_service.entity.Payment;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    // Entity to DTO
    PaymentDTO paymentToPaymentDTO(Payment payment);

    List<PaymentDTO> paymentsToPaymentDTOs(List<Payment> payments);

    // Entity to Response DTO
    @org.mapstruct.Mapping(source = "paymentId", target = "paymentId")
    @org.mapstruct.Mapping(source = "userId", target = "userId")
    @org.mapstruct.Mapping(source = "orderId", target = "orderId")
    @org.mapstruct.Mapping(source = "bookingId", target = "bookingId")
    @org.mapstruct.Mapping(source = "paymentStatus", target = "paymentStatus")
    @org.mapstruct.Mapping(source = "paymentDate", target = "paymentDate")
    @org.mapstruct.Mapping(source = "createdAt", target = "createdAt")
    @org.mapstruct.Mapping(source = "updatedAt", target = "updatedAt")
    PaymentDetailsResponseDTO paymentToPaymentDetailsResponseDTO(Payment payment);

    PaymentFilterRequestDTO paymentToPaymentFilterRequestDTO(Payment payment);


    // Request DTO to Entity
    @org.mapstruct.Mapping(source = "userId", target = "userId")
    @org.mapstruct.Mapping(source = "orderId", target = "orderId")
    @org.mapstruct.Mapping(source = "bookingId", target = "bookingId")
    @org.mapstruct.Mapping(source = "amount", target = "amount")
    Payment createPaymentRequestDTOToPayment(CreatePaymentRequestDTO createPaymentRequestDTO);

    Payment paymentFilterRequestDTOToPayment(PaymentFilterRequestDTO dto);

    PaymentDTO map(Payment savedPayment, Class<PaymentDTO> paymentDTOClass);
}
