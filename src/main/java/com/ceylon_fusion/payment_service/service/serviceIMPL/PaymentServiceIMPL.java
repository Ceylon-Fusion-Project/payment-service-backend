package com.ceylon_fusion.payment_service.service.serviceIMPL;


import com.ceylon_fusion.payment_service.dto.analytics.PaymentAnalyticsDTO;
import com.ceylon_fusion.payment_service.dto.paginated.PaginatedPaymentDTO;
import com.ceylon_fusion.payment_service.dto.request.CreatePaymentRequestDTO;
import com.ceylon_fusion.payment_service.dto.request.OrderRequestDTO;
import com.ceylon_fusion.payment_service.dto.request.PaymentFilterRequestDTO;
import com.ceylon_fusion.payment_service.dto.request.UpdatePaymentRequestDTO;
import com.ceylon_fusion.payment_service.dto.response.PaymentDetailsResponseDTO;
import com.ceylon_fusion.payment_service.entity.Payment;
//import com.ceylon_fusion.payment_service.entity.PaymentMethod;
import com.ceylon_fusion.payment_service.entity.enums.Currency;
import com.ceylon_fusion.payment_service.entity.enums.PaymentStatus;
//import com.ceylon_fusion.payment_service.repo.PaymentMethodRepo;
import com.ceylon_fusion.payment_service.entity.enums.PaymentType;
import com.ceylon_fusion.payment_service.repo.PaymentRepo;
import com.ceylon_fusion.payment_service.service.PaymentService;
import com.ceylon_fusion.payment_service.service.StripeService;
import com.ceylon_fusion.payment_service.util.mappers.PaymentMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PaymentServiceIMPL implements PaymentService {

    private final PaymentRepo paymentRepo;
//    private final PaymentMethodRepo paymentMethodRepo;
    private final StripeService stripeService;
    private final PaymentMapper paymentMapper;


    @Override
    public PaymentDetailsResponseDTO saveOrderPayment(CreatePaymentRequestDTO createPaymentRequestDTO) {
        //Validate the order
        if (createPaymentRequestDTO == null) {
            throw new IllegalArgumentException("Payment request cannot be null");
        }
        if (createPaymentRequestDTO.getUserId() == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        if (createPaymentRequestDTO.getAmount() == null || createPaymentRequestDTO.getAmount() <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        if (createPaymentRequestDTO.getOrderId() == null) {
            throw new IllegalArgumentException("Order ID cannot be null");
        }

        // Create a new payment entity from the request DTO
//        Payment payment = paymentMapper.createPaymentRequestDTOToPayment(createPaymentRequestDTO);
//        payment.setPaymentStatus(PaymentStatus.PENDING);
//        payment.setPaymentDate(LocalDateTime.now());
//        payment.setCurrency(Currency.USD);

//        PaymentMethod paymentMethod = paymentMethodRepo.findByUserIdAndIsDefaultTrue(createPaymentRequestDTO.getUserId())
//                .orElseThrow(() -> new RuntimeException("No default payment method found for user"));
//        payment.setPaymentMethod(paymentMethod);

        Payment payment=new Payment(
                null,
                createPaymentRequestDTO.getUserId(),
                null,
                createPaymentRequestDTO.getBookingId(),
                createPaymentRequestDTO.getAmount(),
                PaymentStatus.PENDING,
                LocalDateTime.now(),
                "12345",
                null,
                "123654",
                null,
                null,
                Currency.USD,
                PaymentType.Order_Payment,
                null
        );
        Payment savedPayment = paymentRepo.save(payment);
        return paymentMapper.paymentToPaymentDetailsResponseDTO(savedPayment);
    }


    @Override
    public PaymentDetailsResponseDTO saveBookingPayment(CreatePaymentRequestDTO createPaymentRequestDTO) {
        // Validate the booking payment request
        if (createPaymentRequestDTO == null) {
            throw new IllegalArgumentException("Payment request cannot be null");
        }
        if (createPaymentRequestDTO.getUserId() == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        if (createPaymentRequestDTO.getAmount() == null || createPaymentRequestDTO.getAmount() <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        if (createPaymentRequestDTO.getBookingId() == null) {
            throw new IllegalArgumentException("Booking ID cannot be null");
        }

//        Long userId = createPaymentRequestDTO.getUserId();
//        PaymentMethod defaultPaymentMethod = paymentMethodRepo.findByUserIdAndIsDefaultTrue(userId)
//                .orElseThrow(() -> new RuntimeException("No default payment method found for user"));
//
//        Payment payment = new Payment();
//        payment.setUserId(createPaymentRequestDTO.getUserId());
//        payment.setBookingId(createPaymentRequestDTO.getBookingId());
//        payment.setAmount(createPaymentRequestDTO.getAmount());
//        payment.setPaymentStatus(PaymentStatus.PENDING);
//        payment.setPaymentDate(LocalDateTime.now());
//        payment.setCurrency(Currency.USD);
//        payment.setPaymentMethod(defaultPaymentMethod);

        Payment payment=new Payment(
                null,
                createPaymentRequestDTO.getUserId(),
                null,
                createPaymentRequestDTO.getBookingId(),
                createPaymentRequestDTO.getAmount(),
                PaymentStatus.PENDING,
                LocalDateTime.now(),
                "12345",
                null,
                "123654",
                null,
                null,
                Currency.USD,
                PaymentType.Booking_Payment,
                null

        );

        Payment savedPayment = paymentRepo.save(payment);
        return paymentMapper.paymentToPaymentDetailsResponseDTO(savedPayment);
    }

    @Override
    public PaymentDetailsResponseDTO getPaymentById(Long paymentId) {
        Payment payment = paymentRepo.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found with ID: " + paymentId));
        return paymentMapper.paymentToPaymentDetailsResponseDTO(payment);
    }

    @Override
    public Object getAllPayments(PaymentFilterRequestDTO filterRequestDTO) {
        Pageable pageable = createPageable(filterRequestDTO);

        // Fetch paginated payments from the repository
        Page<Payment> paymentsPage = paymentRepo.findAll(pageable);

        // Map the payments to DTOs and return the paginated response
        return new PaginatedPaymentDTO(
                paymentMapper.paymentsToPaymentDTOs(paymentsPage.getContent()),
                paymentsPage.getTotalElements()
        );
    }

    private Pageable createPageable(PaymentFilterRequestDTO filterRequestDTO) {
        // Determine the sorting direction
        Sort.Direction sortDirection = "ASC".equalsIgnoreCase(filterRequestDTO.getSortDirection())
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        // Create a Sort object using the field to sort by and its direction
        Sort sort = Sort.by(sortDirection, filterRequestDTO.getSortBy());

        return PageRequest.of(filterRequestDTO.getPage(), filterRequestDTO.getSize(), sort);
    }

    @Override
    public PaymentAnalyticsDTO getPaymentAnalytics(LocalDateTime startDate, LocalDateTime endDate, Long userId) {
        List<Payment> payments;
        if (userId != null) {
            payments = paymentRepo.findByUserIdAndPaymentDateBetween(userId, startDate, endDate);
        } else {
            payments = paymentRepo.findByPaymentDateBetween(startDate, endDate);
        }

        PaymentAnalyticsDTO analyticsDTO = new PaymentAnalyticsDTO();
        analyticsDTO.setStartDate(startDate);
        analyticsDTO.setEndDate(endDate);

        // Calculate total amount and transactions
        BigDecimal totalAmount = payments.stream()
                .map(payment -> BigDecimal.valueOf(payment.getAmount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        analyticsDTO.setTotalAmount(totalAmount);
        analyticsDTO.setTotalTransactions((long) payments.size());

        // Calculate status counts and amounts
        Map<PaymentStatus, Long> statusCounts = payments.stream()
                .collect(Collectors.groupingBy(Payment::getPaymentStatus, Collectors.counting()));
        analyticsDTO.setStatusCounts(statusCounts);

        Map<PaymentStatus, BigDecimal> amountByStatus = payments.stream()
                .collect(Collectors.groupingBy(
                        Payment::getPaymentStatus,
                        Collectors.mapping(
                                payment -> BigDecimal.valueOf(payment.getAmount()),
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)
                        )
                ));
        analyticsDTO.setAmountByStatus(amountByStatus);

        // Calculate average transaction amount
        if (!payments.isEmpty()) {
            analyticsDTO.setAverageTransactionAmount(
                    totalAmount.divide(BigDecimal.valueOf(payments.size()), 2, RoundingMode.HALF_UP)
            );
        }

        // Calculate daily totals
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Map<String, BigDecimal> dailyTotalAmounts = payments.stream()
                .collect(Collectors.groupingBy(
                        payment -> payment.getPaymentDate().format(formatter),
                        Collectors.mapping(
                                payment -> BigDecimal.valueOf(payment.getAmount()),
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)
                        )
                ));
        analyticsDTO.setDailyTotalAmounts(dailyTotalAmounts);

        Map<String, Long> dailyTransactionCounts = payments.stream()
                .collect(Collectors.groupingBy(
                        payment -> payment.getPaymentDate().format(formatter),
                        Collectors.counting()
                ));
        analyticsDTO.setDailyTransactionCounts(dailyTransactionCounts);

        // Calculate success and failure rates
        long successfulPayments = statusCounts.getOrDefault(PaymentStatus.SUCCEEDED, 0L);
        long failedPayments = statusCounts.getOrDefault(PaymentStatus.FAILED, 0L);

        if (!payments.isEmpty()) {
            BigDecimal totalPayments = BigDecimal.valueOf(payments.size());
            analyticsDTO.setSuccessRate(BigDecimal.valueOf(successfulPayments)
                    .divide(totalPayments, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100)));
            analyticsDTO.setFailureRate(BigDecimal.valueOf(failedPayments)
                    .divide(totalPayments, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100)));
        }

        return analyticsDTO;
    }

    @Override
    public Object getPaymentsWithDateRange(PaymentFilterRequestDTO filterRequestDTO) {
        Pageable pageable = createPageable(filterRequestDTO);

        Page<Payment> paymentsPage;
        if (filterRequestDTO.getStartDate() != null && filterRequestDTO.getEndDate() != null) {
            paymentsPage = paymentRepo.findByPaymentDateBetween(
                    filterRequestDTO.getStartDate(),
                    filterRequestDTO.getEndDate(),
                    pageable
            );
        } else {
            paymentsPage = paymentRepo.findAll(pageable);
        }

        return new PaginatedPaymentDTO(
                paymentMapper.paymentsToPaymentDTOs(paymentsPage.getContent()),
                paymentsPage.getTotalElements()
        );
    }

    @Override
    public Object updatePayment(Long paymentId, UpdatePaymentRequestDTO updatePaymentRequestDTO) {
        Payment payment = paymentRepo.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found with ID: " + paymentId));

        if (updatePaymentRequestDTO.getPaymentStatus() != null) {
            payment.setPaymentStatus(updatePaymentRequestDTO.getPaymentStatus());
        }
        if (updatePaymentRequestDTO.getAmount() != null) {
            payment.setAmount(updatePaymentRequestDTO.getAmount());
        }
        if (updatePaymentRequestDTO.getPaymentDate() != null) {
            payment.setPaymentDate(updatePaymentRequestDTO.getPaymentDate());
        }
//        if (updatePaymentRequestDTO.getPaymentMethodId() != null) {
//            PaymentMethod paymentMethod = paymentMethodRepo.findById(updatePaymentRequestDTO.getPaymentMethodId())
//                    .orElseThrow(() -> new RuntimeException("Payment method not found"));
//            payment.setPaymentMethod(paymentMethod);
//        }

        Payment updatedPayment = paymentRepo.save(payment);
        return paymentMapper.paymentToPaymentDetailsResponseDTO(updatedPayment);
    }

    @Override
    public void deletePayment(Long paymentId) {
        Payment payment = paymentRepo.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found with ID: " + paymentId));

        if (payment.getPaymentStatus() == PaymentStatus.SUCCEEDED) {
            throw new RuntimeException("Cannot delete a successful payment");
        }

        paymentRepo.delete(payment);
    }

    @Override
    public PaymentDetailsResponseDTO cancelOrderPaymentByOrderId(Long orderId) {
        // Find payment by order ID
        Payment payment = paymentRepo.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("No payment found for order ID: " + orderId));

        // Validate if payment can be cancelled
        if (payment.getPaymentStatus() == PaymentStatus.SUCCEEDED) {
            throw new RuntimeException("Cannot cancel a successful payment. Please initiate a refund instead.");
        }

//        // Get current authentication
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String currentUsername = authentication.getName();
//
//        // If the user is not an admin, check if they own the order
//        if (!authentication.getAuthorities().stream()
//                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
//            if (!payment.getUserId().toString().equals(currentUsername)) {
//                throw new RuntimeException("You are not authorized to cancel this order payment");
//            }
//        }

        // Update payment status
        payment.setPaymentStatus(PaymentStatus.CANCELLED);
        Payment updatedPayment = paymentRepo.save(payment);

        return paymentMapper.paymentToPaymentDetailsResponseDTO(updatedPayment);
    }
}