package com.anvu.backend.controller;

import com.anvu.backend.entity.Document;
import com.anvu.backend.entity.DocumentChunk;
import com.anvu.backend.repository.DocumentChunkRepository;
import com.anvu.backend.repository.DocumentRepository;
import com.anvu.backend.service.EmbeddingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentRepository documentRepository;
    private final DocumentChunkRepository chunkRepository;
    private final EmbeddingService embeddingService;
    private final String uploadDir = "uploads/";
    private static final int CHUNK_SIZE = 500;

    public DocumentController(DocumentRepository documentRepository, DocumentChunkRepository chunkRepository, EmbeddingService embeddingService) {
        this.documentRepository = documentRepository;
        this.chunkRepository = chunkRepository;
        this.embeddingService = embeddingService;
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

            // Chia nho thanh chunk va tao embedding cho tung chunk
            List<String> chunks = splitIntoChunks(content, CHUNK_SIZE);
            for (String chunkText : chunks) {
                List<Double> vector = embeddingService.embed(chunkText);
                DocumentChunk chunk = new DocumentChunk();
                chunk.setDocumentId(doc.getId());
                chunk.setContent(chunkText);
                chunk.setEmbedding(embeddingService.toStorableString(vector));
                chunkRepository.save(chunk);
            }

            return ResponseEntity.ok(Map.of("message", "Upload va xu ly thanh cong", "id", doc.getId(), "soChunk", chunks.size()));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(Map.of("message", "Loi upload: " + e.getMessage()));
        }
    }

    private List<String> splitIntoChunks(String text, int size) {
        List<String> chunks = new ArrayList<>();
        for (int i = 0; i < text.length(); i += size) {
            chunks.add(text.substring(i, Math.min(text.length(), i + size)));
        }
        return chunks;
    }

    @GetMapping
    public List<Document> getAll() {
        return documentRepository.findAll();
    }
}