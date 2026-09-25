package com.offlineupi.service;

import com.offlineupi.entity.Payment;
import com.offlineupi.entity.PaymentStatus;
import com.offlineupi.mesh.MeshNetwork;
import com.offlineupi.mesh.MeshNode;
import com.offlineupi.mesh.MeshRouter;
import com.offlineupi.packet.HybridEncryptedPaymentPacket;
import com.offlineupi.packet.PaymentPacket;
import com.offlineupi.security.HybridEncryptionService;
import com.offlineupi.security.RsaKeyPairService;
import com.offlineupi.security.ReplayProtectionService;
import com.offlineupi.security.IdempotencyService;
import com.offlineupi.security.PaymentValidationService;

import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.util.List;

@Service
public class SecureMeshSimulationService {

        private final PaymentService paymentService;
        private final HybridEncryptionService hybridEncryptionService;
        private final RsaKeyPairService rsaKeyPairService;
        private final ReplayProtectionService replayProtectionService;
        private final IdempotencyService idempotencyService;
        private final PaymentValidationService paymentValidationService;
        private final SettlementService settlementService;

        public SecureMeshSimulationService(
                        PaymentService paymentService,
                        HybridEncryptionService hybridEncryptionService,
                        RsaKeyPairService rsaKeyPairService,
                        ReplayProtectionService replayProtectionService,
                        IdempotencyService idempotencyService,
                        PaymentValidationService paymentValidationService,
                        SettlementService settlementService) {

                this.paymentService = paymentService;
                this.hybridEncryptionService = hybridEncryptionService;
                this.rsaKeyPairService = rsaKeyPairService;
                this.replayProtectionService = replayProtectionService;
                this.idempotencyService = idempotencyService;
                this.paymentValidationService = paymentValidationService;
                this.settlementService = settlementService;
        }

        public PaymentPacket transmitSecurePayment(
                        String transactionId) throws Exception {

                // 1. Get original payment packet
                PaymentPacket packet = paymentService.getPaymentPacket(transactionId);

                // 2. Generate receiver RSA key pair
                KeyPair receiverKeyPair = rsaKeyPairService.generateKeyPair();

                System.out.println(
                                "Receiver RSA key pair generated.");

                // 3. Hybrid encryption
                //
                // AES encrypts the actual payment payload.
                // RSA encrypts the AES session key.
                HybridEncryptedPaymentPacket encryptedPacket = hybridEncryptionService.encryptPayment(
                                packet,
                                receiverKeyPair.getPublic());

                System.out.println(
                                "Hybrid encrypted payment created.");

                // System.out.println(
                // "Encrypted payload: "
                // + encryptedPacket.getEncryptedPayload());

                // System.out.println(
                // "Encrypted AES key: "
                // + encryptedPacket.getEncryptedAesKey());

                // 4. Create mesh nodes
                MeshNode sender = new MeshNode(
                                "NODE-A",
                                "Suprit's Phone");

                MeshNode relay1 = new MeshNode(
                                "NODE-B",
                                "Relay Device 1");

                MeshNode relay2 = new MeshNode(
                                "NODE-C",
                                "Relay Device 2");

                MeshNode receiver = new MeshNode(
                                "NODE-D",
                                "Santy's Phone");

                // 5. Build mesh network
                MeshNetwork network = new MeshNetwork();

                network.addNode(sender);
                network.addNode(relay1);
                network.addNode(relay2);
                network.addNode(receiver);

                List<MeshNode> route = network.getNodes();

                // 6. Start secure mesh transmission
                System.out.println(
                                "Starting secure mesh transmission...");

                /*
                 * MeshRouter handles the hop/TTL logic.
                 *
                 * The payment payload remains encrypted.
                 * Relay nodes only forward the encrypted packet.
                 */
                MeshRouter router = new MeshRouter();

                boolean reachedDestination = router.forwardPacket(packet, route);

                if (!reachedDestination) {
                        throw new RuntimeException(
                                        "Secure transmission failed: TTL expired.");
                }

                // 7. Simulate encrypted packet forwarding
                System.out.println(
                                "Hybrid encrypted packet successfully "
                                                + "forwarded through the mesh.");

                // 8. Receiver decrypts the hybrid packet
                PaymentPacket decryptedPacket = hybridEncryptionService.decryptPayment(
                                encryptedPacket,
                                receiverKeyPair.getPrivate());

                System.out.println(
                                "Hybrid payment decrypted at receiver.");

                // 9. Replay protection
                if (replayProtectionService.isReplay(
                                decryptedPacket.getPacketId())) {

                        throw new RuntimeException(
                                        "Payment rejected: packet replay detected.");
                }

                System.out.println(
                                "Packet replay protection check passed.");
                // 10. Payment validation
                if (!paymentValidationService.isValid(
                                decryptedPacket)) {

                        throw new RuntimeException(
                                        "Payment rejected: validation failed.");
                }

                System.out.println(
                                "Payment validation passed.");

                // 11. Idempotency check
                if (idempotencyService.hasBeenProcessed(
                                decryptedPacket.getTransactionId())) {

                        System.out.println(
                                        "Duplicate request detected.");

                        return decryptedPacket;
                }

                // idempotencyService.storeResult(
                // decryptedPacket.getTransactionId(),
                // "RECEIVED");

                // 12. Payment lifecycle

                Payment receivedPayment = settlementService.receivePayment(
                                decryptedPacket.getTransactionId());

                if (receivedPayment.getStatus() != PaymentStatus.RECEIVED) {

                        throw new RuntimeException(
                                        "Payment rejected: receive stage failed.");
                }

                System.out.println(
                                "Payment received successfully.");

                Payment validatedPayment = settlementService.validatePayment(
                                decryptedPacket.getTransactionId());

                if (validatedPayment.getStatus() != PaymentStatus.VALIDATED) {

                        throw new RuntimeException(
                                        "Payment rejected: validation stage failed.");
                }

                System.out.println(
                                "Payment validation passed.");

                Payment settledPayment = settlementService.settlePayment(
                                decryptedPacket.getTransactionId());

                if (settledPayment.getStatus() != PaymentStatus.SETTLED) {

                        throw new RuntimeException(
                                        "Payment rejected: settlement failed.");
                }

                System.out.println(
                                "Payment successfully settled.");

                replayProtectionService.markAsProcessed(
                                decryptedPacket.getPacketId());

                idempotencyService.storeResult(
                                decryptedPacket.getTransactionId(),
                                "SETTLED");
                // 13. Display final payment information
                System.out.println(
                                "Receiver: "
                                                + decryptedPacket.getReceiverId());

                System.out.println(
                                "Amount: "
                                                + decryptedPacket.getAmount());

                System.out.println(
                                "TTL after mesh transmission: "
                                                + packet.getTtl());

                return decryptedPacket;
        }
}