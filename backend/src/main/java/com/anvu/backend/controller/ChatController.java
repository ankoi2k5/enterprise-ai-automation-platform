package com.anvu.backend.controller;

import com.anvu.backend.service.GeminiService;
import com.anvu.backend.service.RagService;
import com.anvu.backend.service.RateLimiterService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final GeminiService geminiService;
    private final RateLimiterService rateLimiterService;
    private final RagService ragService;
    private static final int MAX_MESSAGE_LENGTH = 2000;

    public ChatController(GeminiService geminiService, RateLimiterService rateLimiterService, RagService ragService) {
        this.geminiService = geminiService;
        this.rateLimiterService = rateLimiterService;
        this.ragService = ragService;
    }

    @PostMapping
    public ResponseEntity<?> chat(@RequestBody Map<String, String> body) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!rateLimiterService.isAllowed(email)) {
            return ResponseEntity.status(429).body(Map.of("message", "Ban gui qua nhieu tin nhan, vui long doi 1 phut"));
        }

        String message = body.get("message");
        if (message == null || message.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Tin nhan khong duoc de trong"));
        }
        if (message.length() > MAX_MESSAGE_LENGTH) {
            return ResponseEntity.badRequest().body(Map.of("message", "Tin nhan qua dai (toi da " + MAX_MESSAGE_LENGTH + " ky tu)"));
        }

        String reply = geminiService.askGemini(message);
        return ResponseEntity.ok(Map.of("reply", reply));
    }

    @PostMapping("/rag")
    public ResponseEntity<?> chatWithRag(@RequestBody Map<String, String> body) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!rateLimiterService.isAllowed(email)) {
            return ResponseEntity.status(429).body(Map.of("message", "Ban gui qua nhieu tin nhan, vui long doi 1 phut"));
        }

        String message = body.get("message");
        if (message == null || message.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Tin nhan khong duoc de trong"));
        }

        String reply = ragService.askWithContext(message);
        return ResponseEntity.ok(Map.of("reply", reply));
    }
}