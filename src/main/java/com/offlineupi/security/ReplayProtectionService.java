package com.offlineupi.security;

import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ReplayProtectionService {

    private final Set<String> processedPackets = ConcurrentHashMap.newKeySet();

    public boolean isReplay(String packetId) {

        return processedPackets.contains(packetId);
    }

    public void markAsProcessed(String packetId) {

        processedPackets.add(packetId);
    }
}