package com.anvu.backend.controller;

import com.anvu.backend.entity.Document;
import com.anvu.backend.repository.DocumentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentRepository documentRepository;
    private final String uploadDir = "uploads/";

    public DocumentController(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) {
        try {
            if (!file.getOriginalFilename().endsWith(".txt")) {
                return ResponseEntity.badRequest().body(Map.of("message", "Chi ho tro file .txt trong buoc dau"));
            }

            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath);

            String content = Files.readString(filePath, StandardCharsets.UTF_8);

            Document doc = new Document();
            doc.setFileName(file.getOriginalFilename());
            doc.setFilePath(filePath.toString());
            doc.setContent(content);
            documentRepository.save(doc);

            return ResponseEntity.ok(Map.of("message", "Upload thanh cong", "id", doc.getId()));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(Map.of("message", "Loi upload: " + e.getMessage()));
        }
    }

    @GetMapping
    public List<Document> getAll() {
        return documentRepository.findAll();
    }
}