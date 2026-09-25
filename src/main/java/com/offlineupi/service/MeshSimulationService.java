package com.offlineupi.service;

import com.offlineupi.mesh.MeshNetwork;
import com.offlineupi.mesh.MeshNode;
import com.offlineupi.mesh.MeshRouter;
import com.offlineupi.packet.PaymentPacket;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MeshSimulationService {

    private final PaymentService paymentService;
    private final MeshRouter meshRouter;

    public MeshSimulationService(PaymentService paymentService) {
        this.paymentService = paymentService;
        this.meshRouter = new MeshRouter();
    }

    public PaymentPacket transmitPayment(String transactionId) {

        PaymentPacket packet = paymentService.getPaymentPacket(transactionId);

        MeshNode sender = new MeshNode("NODE-A", "Suprit's Phone");

        MeshNode relay1 = new MeshNode("NODE-B", "Relay Device 1");

        MeshNode relay2 = new MeshNode("NODE-C", "Relay Device 2");

        MeshNode receiver = new MeshNode("NODE-D", "Santy's Phone");

        MeshNetwork network = new MeshNetwork();

        network.addNode(sender);
        network.addNode(relay1);
        network.addNode(relay2);
        network.addNode(receiver);

        List<MeshNode> route = network.getNodes();

        meshRouter.forwardPacket(packet, route);

        return packet;
    }
}