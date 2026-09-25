package com.offlineupi.security;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class IdempotencyService {

    private final Map<String, String> processedResults = new ConcurrentHashMap<>();

    public boolean hasBeenProcessed(String transactionId) {
        return processedResults.containsKey(transactionId);
    }

    public void storeResult(
            String transactionId,
            String result) {

        processedResults.put(transactionId, result);
    }

    public String getResult(String transactionId) {
        return processedResults.get(transactionId);
    }
}