package com.anvu.backend.controller;

import com.anvu.backend.service.EmailDraftService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    private final EmailDraftService emailDraftService;

    public EmailController(EmailDraftService emailDraftService) {
        this.emailDraftService = emailDraftService;
    }

    @PostMapping("/draft")
    public Map<String, String> draft(@RequestBody Map<String, String> body) {
        String instruction = body.get("instruction");
        String recipientName = body.getOrDefault("recipientName", "Quy khach");
        return emailDraftService.draftEmail(instruction, recipientName);
    }
}