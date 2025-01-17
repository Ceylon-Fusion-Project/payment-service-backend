package com.ceylon_fusion.payment_service.repo;


import com.ceylon_fusion.payment_service.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

@Repository
@EnableJpaRepositories
public interface PaymentRepo extends JpaRepository<Payment, Long> {


}
