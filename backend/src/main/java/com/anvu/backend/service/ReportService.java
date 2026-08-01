package com.anvu.backend.service;

import com.anvu.backend.repository.DocumentRepository;
import com.anvu.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class ReportService {

    private final UserRepository userRepository;
    private final DocumentRepository documentRepository;
    private final GeminiService geminiService;

    public ReportService(UserRepository userRepository, DocumentRepository documentRepository, GeminiService geminiService) {
        this.userRepository = userRepository;
        this.documentRepository = documentRepository;
        this.geminiService = geminiService;
    }

    public String generateSystemReport() {
        long totalUsers = userRepository.count();
        long totalAdmins = userRepository.findAll().stream()
                .filter(u -> "ADMIN".equals(u.getRole())).count();
        long totalEmployees = totalUsers - totalAdmins;
        long totalDocuments = documentRepository.count();

        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

        String rawData = String.format(
                "Thoi diem tao bao cao: %s\n" +
                        "Tong so nguoi dung: %d\n" +
                        "So luong Admin: %d\n" +
                        "So luong Employee: %d\n" +
                        "Tong so tai lieu da upload: %d",
                now, totalUsers, totalAdmins, totalEmployees, totalDocuments
        );

        String prompt = "Ban la mot AI Agent chuyen viet bao cao he thong cho doanh nghiep. " +
                "Dua vao du lieu tho sau day, hay viet thanh mot ban bao cao ngan gon, chuyen nghiep, " +
                "co nhan xet/danh gia hop ly (vi du: ty le Admin/Employee co hop ly khong, so luong tai lieu " +
                "co dang duoc su dung tot khong). Viet bang tieng Viet, dinh dang ro rang co tieu de va cac muc.\n\n" +
                "DU LIEU THO:\n" + rawData;

        return geminiService.askGemini(prompt);
    }
}