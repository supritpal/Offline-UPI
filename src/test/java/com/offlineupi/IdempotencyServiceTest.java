package com.offlineupi;

import com.offlineupi.security.IdempotencyService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IdempotencyServiceTest {

    @Test
    void newTransactionShouldNotBeProcessed() {

        IdempotencyService service = new IdempotencyService();

        assertFalse(
                service.hasBeenProcessed("tx-001"));
    }

    @Test
    void storedTransactionShouldBeProcessed() {

        IdempotencyService service = new IdempotencyService();

        service.storeResult(
                "tx-001",
                "RECEIVED");

        assertTrue(
                service.hasBeenProcessed("tx-001"));
    }

    @Test
    void shouldReturnStoredResult() {

        IdempotencyService service = new IdempotencyService();

        service.storeResult(
                "tx-001",
                "SETTLED");

        assertEquals(
                "SETTLED",
                service.getResult("tx-001"));
    }

    @Test
    void differentTransactionsShouldBeIndependent() {

        IdempotencyService service = new IdempotencyService();

        service.storeResult(
                "tx-001",
                "SETTLED");

        assertTrue(
                service.hasBeenProcessed("tx-001"));

        assertFalse(
                service.hasBeenProcessed("tx-002"));
    }
}