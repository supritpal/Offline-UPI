package com.offlineupi.mesh;

import com.offlineupi.packet.PaymentPacket;

import java.util.List;

public class MeshRouter {

    public boolean forwardPacket(
            PaymentPacket packet,
            List<MeshNode> route) {

        for (int i = 0; i < route.size() - 1; i++) {

            // Check whether another hop is allowed
            if (packet.getTtl() <= 1) {

                System.out.println(
                        "Packet stopped. TTL expired.");

                return false;
            }

            MeshNode from = route.get(i);
            MeshNode to = route.get(i + 1);

            // Consume one TTL for this hop
            packet.decrementTtl();

            System.out.println(
                    from.getNodeName()
                            + " → "
                            + to.getNodeName()
                            + " | TTL: "
                            + packet.getTtl());
        }

        System.out.println(
                "Packet reached destination.");

        return true;
    }
}