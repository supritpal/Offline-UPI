package com.offlineupi;

import com.offlineupi.packet.PaymentPacket;
import com.offlineupi.security.PaymentValidationService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PaymentValidationServiceTest {

    private final PaymentValidationService service = new PaymentValidationService();

    private PaymentPacket createPacket(
            String transactionId,
            String senderId,
            String receiverId,
            BigDecimal amount,
            LocalDateTime createdAt) {

        return new PaymentPacket(
                "packet-123",
                transactionId,
                senderId,
                receiverId,
                amount,
                createdAt,
                "nonce-123",
                5);
    }

    @Test
    void validPaymentShouldPass() {

        PaymentPacket packet = createPacket(
                "transaction-123",
                "suprit",
                "priya",
                new BigDecimal("500.00"),
                LocalDateTime.now());

        assertTrue(service.isValid(packet));
    }

    @Test
    void nullPacketShouldFail() {

        assertFalse(service.isValid(null));
    }

    @Test
    void zeroAmountShouldFail() {

        PaymentPacket packet = createPacket(
                "transaction-123",
                "suprit",
                "priya",
                BigDecimal.ZERO,
                LocalDateTime.now());

        assertFalse(service.isValid(packet));
    }

    @Test
    void negativeAmountShouldFail() {

        PaymentPacket packet = createPacket(
                "transaction-123",
                "suprit",
                "priya",
                new BigDecimal("-100"),
                LocalDateTime.now());

        assertFalse(service.isValid(packet));
    }

    @Test
    void missingTransactionIdShouldFail() {

        PaymentPacket packet = createPacket(
                null,
                "suprit",
                "priya",
                new BigDecimal("500"),
                LocalDateTime.now());

        assertFalse(service.isValid(packet));
    }

    @Test
    void blankTransactionIdShouldFail() {

        PaymentPacket packet = createPacket(
                "   ",
                "suprit",
                "priya",
                new BigDecimal("500"),
                LocalDateTime.now());

        assertFalse(service.isValid(packet));
    }

    @Test
    void missingSenderShouldFail() {

        PaymentPacket packet = createPacket(
                "transaction-123",
                null,
                "priya",
                new BigDecimal("500"),
                LocalDateTime.now());

        assertFalse(service.isValid(packet));
    }

    @Test
    void missingReceiverShouldFail() {

        PaymentPacket packet = createPacket(
                "transaction-123",
                "suprit",
                null,
                new BigDecimal("500"),
                LocalDateTime.now());

        assertFalse(service.isValid(packet));
    }

    @Test
    void missingAmountShouldFail() {

        PaymentPacket packet = createPacket(
                "transaction-123",
                "suprit",
                "priya",
                null,
                LocalDateTime.now());

        assertFalse(service.isValid(packet));
    }

    @Test
    void missingCreatedAtShouldFail() {

        PaymentPacket packet = createPacket(
                "transaction-123",
                "suprit",
                "priya",
                new BigDecimal("500"),
                null);

        assertFalse(service.isValid(packet));
    }

    @Test
    void futurePaymentShouldFail() {

        PaymentPacket packet = createPacket(
                "transaction-123",
                "suprit",
                "priya",
                new BigDecimal("500"),
                LocalDateTime.now().plusMinutes(1));

        assertFalse(service.isValid(packet));
    }

    @Test
    void paymentOlderThanFiveMinutesShouldFail() {

        PaymentPacket packet = createPacket(
                "transaction-123",
                "suprit",
                "priya",
                new BigDecimal("500"),
                LocalDateTime.now().minusMinutes(6));

        assertFalse(service.isValid(packet));
    }

    @Test
    void paymentWithinFiveMinutesShouldPass() {

        PaymentPacket packet = createPacket(
                "transaction-123",
                "suprit",
                "priya",
                new BigDecimal("500"),
                LocalDateTime.now().minusMinutes(4));

        assertTrue(service.isValid(packet));
    }
}