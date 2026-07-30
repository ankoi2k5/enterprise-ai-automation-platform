package com.anvu.backend.controller;

import com.anvu.backend.service.GeminiService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final GeminiService geminiService;

    public ChatController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    @PostMapping
    public Map<String, String> chat(@RequestBody Map<String, String> body) {
        String message = body.get("message");
        String reply = geminiService.askGemini(message);
        return Map.of("reply", reply);
    }
}