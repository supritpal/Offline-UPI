package com.offlineupi.service;

import com.offlineupi.entity.Payment;
import com.offlineupi.entity.PaymentStatus;
import com.offlineupi.repository.PaymentRepository;

import org.springframework.stereotype.Service;

@Service
public class SettlementService {

    private final PaymentRepository paymentRepository;

    public SettlementService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Payment receivePayment(String transactionId) {

        Payment payment = paymentRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Payment not found."));

        payment.setStatus(PaymentStatus.RECEIVED);

        return paymentRepository.save(payment);
    }

    public Payment validatePayment(String transactionId) {

        Payment payment = paymentRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Payment not found."));

        if (payment.getAmount() == null
                || payment.getAmount().signum() <= 0) {

            payment.setStatus(PaymentStatus.REJECTED);

            return paymentRepository.save(payment);
        }

        payment.setStatus(PaymentStatus.VALIDATED);

        return paymentRepository.save(payment);
    }

    public Payment settlePayment(String transactionId) {

        Payment payment = paymentRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Payment not found."));

        if (payment.getStatus() != PaymentStatus.VALIDATED) {

            payment.setStatus(PaymentStatus.REJECTED);

            return paymentRepository.save(payment);
        }

        payment.setStatus(PaymentStatus.SETTLED);

        return paymentRepository.save(payment);
    }
}