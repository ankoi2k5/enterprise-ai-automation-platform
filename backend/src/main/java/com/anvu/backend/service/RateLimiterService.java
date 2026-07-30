package com.anvu.backend.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class RateLimiterService {

    private final ConcurrentHashMap<String, AtomicInteger> requestCounts = new ConcurrentHashMap<>();
    private static final int MAX_REQUESTS_PER_MINUTE = 10;

    public boolean isAllowed(String userEmail) {
        AtomicInteger count = requestCounts.computeIfAbsent(userEmail, k -> new AtomicInteger(0));
        int current = count.incrementAndGet();

        if (current == 1) {
            // Reset sau 60 giay
            new Thread(() -> {
                try {
                    Thread.sleep(60000);
                    requestCounts.remove(userEmail);
                } catch (InterruptedException ignored) {}
            }).start();
        }

        return current <= MAX_REQUESTS_PER_MINUTE;
    }
}