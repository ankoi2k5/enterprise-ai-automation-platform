package com.anvu.backend.service;

import org.springframework.stereotype.Service;
import tools.jackson.databind.json.JsonMapper;

import java.util.Map;

@Service
public class EmailDraftService {

    private final GeminiService geminiService;
    private final JsonMapper jsonMapper = JsonMapper.builder().build();

    public EmailDraftService(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    public Map<String, String> draftEmail(String instruction, String recipientName) {
        String prompt = "Ban la AI Agent chuyen soan email cong viec chuyen nghiep bang tieng Viet. " +
                "Dua vao yeu cau sau, hay viet 1 email hoan chinh gui cho '" + recipientName + "'.\n" +
                "Yeu cau: " + instruction + "\n\n" +
                "QUAN TRONG: Chi tra ve DUY NHAT 1 doi tuong JSON hop le, khong them chu giai thich, " +
                "khong dung markdown code block, theo dung dinh dang:\n" +
                "{\"subject\": \"tieu de email\", \"body\": \"noi dung email day du, co chao hoi va ky ten\"}";

        String rawResponse = geminiService.askGemini(prompt);

        try {
            String cleaned = rawResponse.replace("```json", "").replace("```", "").trim();
            return jsonMapper.readValue(cleaned, Map.class);
        } catch (Exception e) {
            return Map.of("subject", "Loi dinh dang", "body", "AI tra ve khong dung JSON: " + rawResponse);
        }
    }
}