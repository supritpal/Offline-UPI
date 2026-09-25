package com.offlineupi.service;

import com.offlineupi.entity.OfflinePayment;
import com.offlineupi.packet.PaymentPacket;
import com.offlineupi.repository.OfflinePaymentRepository;
import org.springframework.stereotype.Service;

@Service
public class OfflineQueueService {

    private final OfflinePaymentRepository offlinePaymentRepository;

    public OfflineQueueService(
            OfflinePaymentRepository offlinePaymentRepository
    ) {
        this.offlinePaymentRepository = offlinePaymentRepository;
    }

    public OfflinePayment queuePayment(PaymentPacket packet) {

        OfflinePayment offlinePayment = new OfflinePayment(
                packet.getTransactionId(),
                packet.getSenderId(),
                packet.getReceiverId(),
                packet.getAmount(),
                packet.getCreatedAt(),
                packet.getNonce(),
                packet.getTtl()
        );

        return offlinePaymentRepository.save(offlinePayment);
    }
}