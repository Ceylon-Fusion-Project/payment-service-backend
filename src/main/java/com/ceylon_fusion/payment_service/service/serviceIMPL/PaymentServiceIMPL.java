package com.ceylon_fusion.payment_service.service.serviceIMPL;


import com.ceylon_fusion.payment_service.repo.PaymentRepo;
import com.ceylon_fusion.payment_service.service.PaymentService;
import com.ceylon_fusion.payment_service.util.mappers.PaymentMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PaymentServiceIMPL implements PaymentService {

    @Autowired
    private  PaymentRepo paymentRepo;
   @Autowired
    private  PaymentMapper paymentMapper;


}