package com.offlineupi.repository;

import com.offlineupi.entity.OfflinePayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OfflinePaymentRepository
        extends JpaRepository<OfflinePayment, Long> {

    Optional<OfflinePayment> findByTransactionId(String transactionId);
}