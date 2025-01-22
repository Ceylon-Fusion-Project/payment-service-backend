package com.ceylon_fusion.payment_service.service.serviceIMPL;

import com.ceylon_fusion.payment_service.service.StripeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class StripeServiceIMPL implements StripeService {
    @Override
    public void validateWebhookSignature(String payload, String sigHeader) {

    }
}
