package com.ceylon_fusion.payment_service.service.serviceIMPL;

import com.ceylon_fusion.payment_service.dto.PaymentMethodDTO;
import com.ceylon_fusion.payment_service.dto.paginated.PaginatedPaymentMethodDTO;
import com.ceylon_fusion.payment_service.dto.request.CreatePaymentMethodRequestDTO;
import com.ceylon_fusion.payment_service.dto.response.PaymentMethodDetailsResponseDTO;
import com.ceylon_fusion.payment_service.entity.PaymentMethod;
import com.ceylon_fusion.payment_service.entity.enums.PaymentMethodsStatus;
import com.ceylon_fusion.payment_service.repo.PaymentMethodRepo;
import com.ceylon_fusion.payment_service.service.PaymentMethodService;
import com.ceylon_fusion.payment_service.util.mappers.PaymentMethodMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentMethodServiceIMPL implements PaymentMethodService {

    private final PaymentMethodRepo paymentMethodRepo;
    private final PaymentMethodMapper paymentMethodMapper;

    @Override
    public PaymentMethodDetailsResponseDTO createPaymentMethod(CreatePaymentMethodRequestDTO request) {
        // Validate request
        if (request.getUserId() == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        // Check if there's already a default payment method for this user
        if (Boolean.TRUE.equals(request.getIsDefault())) {
            paymentMethodRepo.findByUserIdAndIsDefaultTrue(request.getUserId())
                    .ifPresent(existingDefault -> {
                        existingDefault.setIsDefault(false);
                        paymentMethodRepo.save(existingDefault);
                    });
        }

        PaymentMethod paymentMethod = paymentMethodMapper.createPaymentMethodRequestDTOToPaymentMethod(request);
        paymentMethod.setStatus(PaymentMethodsStatus.ACTIVE);

        PaymentMethod savedMethod = paymentMethodRepo.save(paymentMethod);
        return paymentMethodMapper.paymentMethodToPaymentMethodDetailsResponseDTO(savedMethod);
    }

    @Override
    public PaymentMethodDetailsResponseDTO getPaymentMethodById(Long id) {
        PaymentMethod paymentMethod = paymentMethodRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment method not found with ID: " + id));
        return paymentMethodMapper.paymentMethodToPaymentMethodDetailsResponseDTO(paymentMethod);
    }

    @Override
    public Object getUserPaymentMethods(Long userId, int page, int size) {
        Page<PaymentMethod> paymentMethods = paymentMethodRepo.findByUserId(
                userId,
                PageRequest.of(page, size)
        );

        return new PaginatedPaymentMethodDTO(
                paymentMethodMapper.paymentMethodsToPaymentMethodDTOs(paymentMethods.getContent()),
                paymentMethods.getTotalElements()
        );
    }

    @Override
    public PaymentMethodDetailsResponseDTO updatePaymentMethod(Long id, CreatePaymentMethodRequestDTO request) {
        PaymentMethod existingMethod = paymentMethodRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment method not found with ID: " + id));

        // Update fields
        existingMethod.setMethodName(request.getMethodName());
        existingMethod.setIsActive(request.getIsActive());
        existingMethod.setProvider(request.getProvider());
        existingMethod.setMaskedDetails(request.getMaskedDetails());

        // Handle default payment method changes
        if (Boolean.TRUE.equals(request.getIsDefault()) && !Boolean.TRUE.equals(existingMethod.getIsDefault())) {
            paymentMethodRepo.findByUserIdAndIsDefaultTrue(existingMethod.getUserId())
                    .ifPresent(currentDefault -> {
                        currentDefault.setIsDefault(false);
                        paymentMethodRepo.save(currentDefault);
                    });
            existingMethod.setIsDefault(true);
        }

        PaymentMethod updatedMethod = paymentMethodRepo.save(existingMethod);
        return paymentMethodMapper.paymentMethodToPaymentMethodDetailsResponseDTO(updatedMethod);
    }

    @Override
    public void deletePaymentMethod(Long id) {
        PaymentMethod paymentMethod = paymentMethodRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment method not found with ID: " + id));

        // Instead of hard delete, set status to DELETED
        paymentMethod.setStatus(PaymentMethodsStatus.DELETED);
        paymentMethod.setIsActive(false);
        paymentMethodRepo.save(paymentMethod);
    }

}
