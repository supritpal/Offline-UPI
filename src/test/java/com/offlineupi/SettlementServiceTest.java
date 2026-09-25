package com.offlineupi;

import com.offlineupi.entity.Payment;
import com.offlineupi.entity.PaymentStatus;
import com.offlineupi.repository.PaymentRepository;
import com.offlineupi.service.SettlementService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SettlementServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private SettlementService settlementService;

    private Payment createPayment(BigDecimal amount) {

        Payment payment = new Payment(
                "tx-001",
                "suprit",
                amount);

        payment.setStatus(PaymentStatus.CREATED);

        return payment;
    }

    @Test
    void receivePaymentShouldSetReceivedStatus() {

        Payment payment = createPayment(new BigDecimal("500.00"));

        when(paymentRepository.findById("tx-001"))
                .thenReturn(Optional.of(payment));

        when(paymentRepository.save(payment))
                .thenReturn(payment);

        Payment result = settlementService.receivePayment("tx-001");

        assertEquals(
                PaymentStatus.RECEIVED,
                result.getStatus());

        verify(paymentRepository)
                .save(payment);
    }

    @Test
    void validatePaymentShouldSetValidatedStatus() {

        Payment payment = createPayment(new BigDecimal("500.00"));

        when(paymentRepository.findById("tx-001"))
                .thenReturn(Optional.of(payment));

        when(paymentRepository.save(payment))
                .thenReturn(payment);

        Payment result = settlementService.validatePayment("tx-001");

        assertEquals(
                PaymentStatus.VALIDATED,
                result.getStatus());
    }

    @Test
    void settlePaymentShouldSetSettledStatus() {

        Payment payment = createPayment(new BigDecimal("500.00"));

        payment.setStatus(
                PaymentStatus.VALIDATED);

        when(paymentRepository.findById("tx-001"))
                .thenReturn(Optional.of(payment));

        when(paymentRepository.save(payment))
                .thenReturn(payment);

        Payment result = settlementService.settlePayment("tx-001");

        assertEquals(
                PaymentStatus.SETTLED,
                result.getStatus());
    }

    @Test
    void settlePaymentShouldRejectUnvalidatedPayment() {

        Payment payment = createPayment(new BigDecimal("500.00"));

        payment.setStatus(
                PaymentStatus.RECEIVED);

        when(paymentRepository.findById("tx-001"))
                .thenReturn(Optional.of(payment));

        when(paymentRepository.save(payment))
                .thenReturn(payment);

        Payment result = settlementService.settlePayment("tx-001");

        assertEquals(
                PaymentStatus.REJECTED,
                result.getStatus());
    }

    @Test
    void validatePaymentShouldRejectInvalidAmount() {

        Payment payment = createPayment(BigDecimal.ZERO);

        when(paymentRepository.findById("tx-001"))
                .thenReturn(Optional.of(payment));

        when(paymentRepository.save(payment))
                .thenReturn(payment);

        Payment result = settlementService.validatePayment("tx-001");

        assertEquals(
                PaymentStatus.REJECTED,
                result.getStatus());
    }

    @Test
    void missingPaymentShouldThrowException() {

        when(paymentRepository.findById("tx-404"))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> settlementService
                        .receivePayment("tx-404"));
    }
}