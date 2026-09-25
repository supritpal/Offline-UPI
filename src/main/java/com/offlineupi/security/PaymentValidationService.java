package com.offlineupi.security;

import com.offlineupi.packet.PaymentPacket;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class PaymentValidationService {

    // Maximum age allowed for an offline payment
    private static final long MAX_PAYMENT_AGE_MINUTES = 5;

    public boolean isValid(PaymentPacket packet) {

        if (packet == null) {
            return false;
        }

        // Basic validation
        if (packet.getTransactionId() == null
                || packet.getTransactionId().isBlank()) {
            return false;
        }

        if (packet.getSenderId() == null
                || packet.getReceiverId() == null) {
            return false;
        }

        if (packet.getAmount() == null
                || packet.getAmount().signum() <= 0) {
            return false;
        }

        // Timestamp validation
        if (packet.getCreatedAt() == null) {
            return false;
        }

        long ageInMinutes = Duration.between(
                packet.getCreatedAt(),
                LocalDateTime.now()).toMinutes();

        // Reject future timestamps
        if (ageInMinutes < 0) {
            return false;
        }

        // Reject expired payments
        return ageInMinutes <= MAX_PAYMENT_AGE_MINUTES;
    }
}