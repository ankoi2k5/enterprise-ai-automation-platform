package com.anvu.backend.controller;

import com.anvu.backend.service.EmailDraftService;
import org.springframework.web.bind.annotation.*;
import com.anvu.backend.service.EmailSenderService;

import java.util.Map;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    private final EmailDraftService emailDraftService;
    private final EmailSenderService emailSenderService;

    public EmailController(EmailDraftService emailDraftService, EmailSenderService emailSenderService) {
        this.emailDraftService = emailDraftService;
        this.emailSenderService = emailSenderService;
    }

    @PostMapping("/draft")
    public Map<String, String> draft(@RequestBody Map<String, String> body) {
        String instruction = body.get("instruction");
        String recipientName = body.getOrDefault("recipientName", "Quy khach");
        return emailDraftService.draftEmail(instruction, recipientName);
    }
    @PostMapping("/send")
    public Map<String, String> send(@RequestBody Map<String, String> body) {
        String to = body.get("to");
        String subject = body.get("subject");
        String content = body.get("body");
        emailSenderService.sendEmail(to, subject, content);
        return Map.of("message", "Da gui email thanh cong");
    }
}