package com.offlineupi.service;

import com.offlineupi.entity.Payment;
import com.offlineupi.packet.PaymentPacket;
import com.offlineupi.repository.PaymentRepository;
import java.util.UUID;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Payment createPayment(
            String senderId,
            String receiverId,
            BigDecimal amount) {

        Payment payment = new Payment(
                senderId,
                receiverId,
                amount);

        payment.setPacketId(UUID.randomUUID().toString());

        return paymentRepository.save(payment);
    }

    public PaymentPacket createPaymentPacket(Payment payment) {

        return new PaymentPacket(
                payment.getPacketId(),
                payment.getTransactionId(),
                payment.getSenderId(),
                payment.getReceiverId(),
                payment.getAmount(),
                payment.getTimestamp(),
                payment.getNonce(),
                5);
    }

    public PaymentPacket getPaymentPacket(String transactionId) {

        Payment payment = paymentRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        return createPaymentPacket(payment);
    }
}