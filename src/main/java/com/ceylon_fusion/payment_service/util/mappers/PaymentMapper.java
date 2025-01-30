package com.ceylon_fusion.payment_service.util.mappers;

import com.ceylon_fusion.payment_service.dto.PaymentDTO;
import com.ceylon_fusion.payment_service.dto.request.BookingRequestDTO;
import com.ceylon_fusion.payment_service.dto.request.CreatePaymentRequestDTO;
import com.ceylon_fusion.payment_service.dto.request.OrderRequestDTO;
import com.ceylon_fusion.payment_service.dto.request.PaymentFilterRequestDTO;
import com.ceylon_fusion.payment_service.dto.response.PaymentDetailsResponseDTO;
import com.ceylon_fusion.payment_service.dto.response.StandardResponseDTO;
import com.ceylon_fusion.payment_service.entity.Payment;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(source = "paymentId", target = "paymentId")
    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "amount", target = "amount")
    @Mapping(source = "paymentStatus", target = "paymentStatus")
    @Mapping(source = "createdAt", target = "createdAt")
    PaymentDTO paymentToPaymentDTO(Payment payment);

    List<PaymentDTO> paymentsToPaymentDTOs(List<Payment> payments);

    @Mapping(source = "paymentId", target = "paymentId")
    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "orderId", target = "orderId")
    @Mapping(source = "bookingId", target = "bookingId")
    @Mapping(source = "paymentStatus", target = "paymentStatus")
    @Mapping(source = "paymentDate", target = "paymentDate")
    @Mapping(source = "stripePaymentIntentId", target = "stripePaymentIntentId")
    @Mapping(source = "clientSecret", target = "stripeClientSecret")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "updatedAt", target = "updatedAt")
    PaymentDetailsResponseDTO paymentToPaymentDetailsResponseDTO(Payment payment);

    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "orderId", target = "orderId")
    @Mapping(source = "bookingId", target = "bookingId")
    @Mapping(source = "amount", target = "amount")
    @Mapping(target = "paymentStatus", constant = "PENDING")
    @Mapping(target = "paymentDate", expression = "java(java.time.LocalDateTime.now())")
    Payment createPaymentRequestDTOToPayment(CreatePaymentRequestDTO dto);

    @Mapping(source = "orderId", target = "orderId")
    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "amount", target = "amount")
    CreatePaymentRequestDTO orderRequestDTOToCreatePaymentRequestDTO(OrderRequestDTO orderRequestDTO);

    // Remove incorrect Payment mapping for filter DTO
    default PaymentFilterRequestDTO paymentToPaymentFilterRequestDTO(Payment payment) {
        return PaymentFilterRequestDTO.builder()
                .userId(payment.getUserId())
                .orderId(payment.getOrderId())
                .bookingId(payment.getBookingId())
                .status(payment.getPaymentStatus())
                .build();
    }
}
