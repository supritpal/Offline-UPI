package com.offlineupi;

import com.offlineupi.security.ReplayProtectionService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReplayProtectionServiceTest {

        @Test
        void newPacketShouldNotBeReplay() {

                ReplayProtectionService replayProtectionService = new ReplayProtectionService();

                assertFalse(
                                replayProtectionService.isReplay("packet-001"));
        }

        @Test
        void processedPacketShouldBeDetectedAsReplay() {

                ReplayProtectionService replayProtectionService = new ReplayProtectionService();

                replayProtectionService.markAsProcessed("packet-001");

                assertTrue(
                                replayProtectionService.isReplay("packet-001"));
        }
}