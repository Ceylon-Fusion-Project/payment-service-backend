package com.ceylon_fusion.payment_service.repo;

import com.ceylon_fusion.payment_service.entity.Refund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface RefundRepo extends JpaRepository<Refund,Long> {
}
