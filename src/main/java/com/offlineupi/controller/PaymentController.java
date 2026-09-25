package com.offlineupi.controller;

import com.offlineupi.dto.PaymentRequest;
import com.offlineupi.entity.OfflinePayment;
import com.offlineupi.entity.Payment;
import com.offlineupi.packet.PaymentPacket;
import com.offlineupi.service.PaymentService;
import com.offlineupi.service.SecureMeshSimulationService;
import com.offlineupi.service.MeshSimulationService;
import com.offlineupi.service.OfflineQueueService;
import com.offlineupi.service.MeshSimulationService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final OfflineQueueService offlineQueueService;
    private final MeshSimulationService meshSimulationService;
    private final SecureMeshSimulationService secureMeshSimulationService;

    public PaymentController(
            PaymentService paymentService,
            OfflineQueueService offlineQueueService,
            MeshSimulationService meshSimulationService,
            SecureMeshSimulationService secureMeshSimulationService) {
        this.paymentService = paymentService;
        this.offlineQueueService = offlineQueueService;
        this.meshSimulationService = meshSimulationService;
        this.secureMeshSimulationService = secureMeshSimulationService;
    }

    @PostMapping
    public ResponseEntity<Payment> createPayment(
            @Valid @RequestBody PaymentRequest request) {

        Payment payment = paymentService.createPayment(
                request.getSenderId(),
                request.getReceiverId(),
                request.getAmount());

        return ResponseEntity.ok(payment);
    }

    @GetMapping("/{transactionId}/packet")
    public ResponseEntity<PaymentPacket> getPaymentPacket(
            @PathVariable String transactionId) {

        PaymentPacket packet = paymentService.getPaymentPacket(transactionId);

        return ResponseEntity.ok(packet);
    }

    @PostMapping("/{transactionId}/queue")
    public ResponseEntity<OfflinePayment> queuePayment(
            @PathVariable String transactionId) {
        PaymentPacket packet = paymentService.getPaymentPacket(transactionId);

        OfflinePayment queuedPayment = offlineQueueService.queuePayment(packet);

        return ResponseEntity.ok(queuedPayment);
    }

    @PostMapping("/{transactionId}/transmit")
    public ResponseEntity<PaymentPacket> transmitPayment(
            @PathVariable String transactionId) {

        PaymentPacket packet = meshSimulationService.transmitPayment(transactionId);

        return ResponseEntity.ok(packet);
    }

    @PostMapping("/{transactionId}/secure-transmit")
    public ResponseEntity<PaymentPacket> secureTransmitPayment(
            @PathVariable String transactionId) throws Exception {

        PaymentPacket packet = secureMeshSimulationService
                .transmitSecurePayment(transactionId);

        return ResponseEntity.ok(packet);
    }
}