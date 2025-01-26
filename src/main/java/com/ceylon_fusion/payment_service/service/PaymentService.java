package com.ceylon_fusion.payment_service.service;


import com.ceylon_fusion.payment_service.dto.analytics.PaymentAnalyticsDTO;
import com.ceylon_fusion.payment_service.dto.request.CreatePaymentRequestDTO;
import com.ceylon_fusion.payment_service.dto.request.PaymentFilterRequestDTO;
import com.ceylon_fusion.payment_service.dto.request.UpdatePaymentRequestDTO;
import com.ceylon_fusion.payment_service.dto.response.PaymentDetailsResponseDTO;
import jakarta.validation.Valid;

import java.time.LocalDateTime;

public interface PaymentService {


    PaymentDetailsResponseDTO saveOrderPayment(CreatePaymentRequestDTO createPaymentRequestDTO);

    PaymentDetailsResponseDTO saveBookingPayment(CreatePaymentRequestDTO createPaymentRequestDTO);

    PaymentDetailsResponseDTO getPaymentById(Long paymentId);

    Object getAllPayments(PaymentFilterRequestDTO filterRequestDTO);

    PaymentAnalyticsDTO getPaymentAnalytics(LocalDateTime start, LocalDateTime end, Long userId);

    Object getPaymentsWithDateRange(PaymentFilterRequestDTO filterRequestDTO);

    Object updatePayment(Long paymentId, @Valid UpdatePaymentRequestDTO updatePaymentRequestDTO);

    void deletePayment(Long paymentId);

    PaymentDetailsResponseDTO cancelOrderPaymentByOrderId(Long orderId);

}
