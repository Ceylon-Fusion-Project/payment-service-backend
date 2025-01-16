package com.ceylon_fusion.payment_service.service.serviceIMPL;


import com.ceylon_fusion.payment_service.dto.analytics.PaymentAnalyticsDTO;
import com.ceylon_fusion.payment_service.dto.request.CreatePaymentRequestDTO;
import com.ceylon_fusion.payment_service.dto.request.PaymentFilterRequestDTO;
import com.ceylon_fusion.payment_service.dto.request.UpdatePaymentRequestDTO;
import com.ceylon_fusion.payment_service.dto.response.PaymentDetailsResponseDTO;
import com.ceylon_fusion.payment_service.repo.PaymentRepo;
import com.ceylon_fusion.payment_service.service.PaymentService;
import com.ceylon_fusion.payment_service.util.mappers.PaymentMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PaymentServiceIMPL implements PaymentService {

    @Autowired
    private  PaymentRepo paymentRepo;
   @Autowired
    private  PaymentMapper paymentMapper;


    @Override
    public PaymentDetailsResponseDTO saveOrderPayment(CreatePaymentRequestDTO createPaymentRequestDTO) {
        return null;
    }

    @Override
    public PaymentDetailsResponseDTO saveBookingPayment(CreatePaymentRequestDTO createPaymentRequestDTO) {
        return null;
    }

    @Override
    public PaymentDetailsResponseDTO getPaymentById(Long paymentId) {
        return null;
    }

    @Override
    public Object getAllPayments(PaymentFilterRequestDTO filterRequestDTO) {
        return null;
    }

    @Override
    public PaymentAnalyticsDTO getPaymentAnalytics(LocalDateTime start, LocalDateTime end, Long userId) {
        return null;
    }

    @Override
    public Object getPaymentsWithDateRange(PaymentFilterRequestDTO filterRequestDTO) {
        return null;
    }

    @Override
    public Object updatePayment(Long paymentId, UpdatePaymentRequestDTO updatePaymentRequestDTO) {
        return null;
    }

    @Override
    public void deletePayment(Long paymentId) {

    }
}