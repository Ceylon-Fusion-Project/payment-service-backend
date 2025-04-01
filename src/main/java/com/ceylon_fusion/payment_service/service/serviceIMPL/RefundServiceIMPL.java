package com.ceylon_fusion.payment_service.service.serviceIMPL;

import com.ceylon_fusion.payment_service.dto.paginated.PaginatedRefundDTO;
import com.ceylon_fusion.payment_service.dto.request.InitiateRefundRequestDTO;
import com.ceylon_fusion.payment_service.dto.response.RefundDetailsResponseDTO;
import com.ceylon_fusion.payment_service.entity.Payment;
import com.ceylon_fusion.payment_service.entity.Refund;
import com.ceylon_fusion.payment_service.entity.enums.PaymentStatus;
import com.ceylon_fusion.payment_service.entity.enums.RefundStatus;
import com.ceylon_fusion.payment_service.repo.PaymentRepo;
import com.ceylon_fusion.payment_service.repo.RefundRepo;
import com.ceylon_fusion.payment_service.service.RefundService;
import com.ceylon_fusion.payment_service.util.mappers.RefundMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class RefundServiceIMPL implements RefundService {

    private final RefundRepo refundRepo;
    private final PaymentRepo paymentRepo;
    private final RefundMapper refundMapper;

    @Override
    public RefundDetailsResponseDTO initiateRefund(InitiateRefundRequestDTO request) {
        Long paymentId;
        try {
            paymentId = Long.valueOf(request.getPaymentId());
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid payment ID format");
        }

        Payment payment = paymentRepo.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found with ID: " + paymentId));

        if (payment.getPaymentStatus() != PaymentStatus.SUCCEEDED) {
            throw new RuntimeException("Only successful payments can be refunded");
        }

        Refund refund = refundMapper.initiateRefundRequestDTOToRefund(request);
        refund.setPayment(payment);
        refund.setAmount(payment.getAmount());
        refund.setRefundStatus(RefundStatus.PENDING);
        refund.setTransactionDate(LocalDateTime.now());

        Refund savedRefund = refundRepo.save(refund);
        return refundMapper.refundToRefundDetailsResponseDTO(savedRefund);
    }

    @Override
    public Object getRefundById(Long refundId) {
        Refund refund = refundRepo.findById(refundId)
                .orElseThrow(() -> new RuntimeException("Refund not found"));
        return refundMapper.refundToRefundDetailsResponseDTO(refund);
    }

    @Override
    public Object getAllRefunds(int page, int size, String sortBy, String sortDirection) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<Refund> refundsPage = refundRepo.findAll(pageable);

        return new PaginatedRefundDTO(
                refundMapper.refundsToRefundDTOs(refundsPage.getContent()),
                refundsPage.getTotalElements()
        );
    }

    @Override
    public Object getRefundsByDateRange(LocalDateTime startDate, LocalDateTime endDate, Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Refund> refundsPage;

        if (userId != null) {
            refundsPage = refundRepo.findByPayment_UserId(userId, pageable);
        } else {
            refundsPage = refundRepo.findByTransactionDateBetween(startDate, endDate, pageable);
        }

        return new PaginatedRefundDTO(
                refundMapper.refundsToRefundDTOs(refundsPage.getContent()),
                refundsPage.getTotalElements()
        );
    }

    @Override
    public Object updateRefundStatus(Long refundId, String status) {
        Refund refund = refundRepo.findById(refundId)
                .orElseThrow(() -> new RuntimeException("Refund not found"));

        try {
            RefundStatus updatedStatus = RefundStatus.valueOf(status.toUpperCase());
            refund.setRefundStatus(updatedStatus);

            if (updatedStatus == RefundStatus.SUCCESSFUL) {
                refund.getPayment().setPaymentStatus(PaymentStatus.REFUNDED);
                paymentRepo.save(refund.getPayment());
            }

            Refund updatedRefund = refundRepo.save(refund);
            return refundMapper.refundToRefundDetailsResponseDTO(updatedRefund);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid refund status: " + status);
        }
    }

    @Override
    public void cancelRefund(Long refundId) {
        Refund refund = refundRepo.findById(refundId)
                .orElseThrow(() -> new RuntimeException("Refund not found"));

        if (refund.getRefundStatus() == RefundStatus.SUCCESSFUL) {
            throw new RuntimeException("Cannot cancel a successful refund");
        }

        refund.setRefundStatus(RefundStatus.REJECTED);
        refundRepo.save(refund);
    }

}
