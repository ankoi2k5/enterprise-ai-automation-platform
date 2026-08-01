package com.anvu.backend.service;

import com.anvu.backend.entity.DocumentChunk;
import com.anvu.backend.repository.DocumentChunkRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RagService {

    private final DocumentChunkRepository chunkRepository;
    private final EmbeddingService embeddingService;
    private final GeminiService geminiService;

    public RagService(DocumentChunkRepository chunkRepository, EmbeddingService embeddingService, GeminiService geminiService) {
        this.chunkRepository = chunkRepository;
        this.embeddingService = embeddingService;
        this.geminiService = geminiService;
    }

    public String askWithContext(String question) {
        List<Double> questionVector = embeddingService.embed(question);

        List<DocumentChunk> allChunks = chunkRepository.findAll();
        if (allChunks.isEmpty()) {
            return geminiService.askGemini(question);
        }

        // Tinh do tuong dong cho tung chunk, giu ca diem so
        List<double[]> scoredIndexes = new java.util.ArrayList<>();
        List<DocumentChunk> sortedChunks = allChunks.stream()
                .sorted((a, b) -> {
                    double simA = embeddingService.cosineSimilarity(questionVector, embeddingService.fromStorableString(a.getEmbedding()));
                    double simB = embeddingService.cosineSimilarity(questionVector, embeddingService.fromStorableString(b.getEmbedding()));
                    return Double.compare(simB, simA);
                })
                .limit(3)
                .collect(Collectors.toList());

        // Tinh do tuong dong cao nhat de kiem tra nguong lien quan
        double bestSimilarity = embeddingService.cosineSimilarity(
                questionVector,
                embeddingService.fromStorableString(sortedChunks.get(0).getEmbedding())
        );

        double RELEVANCE_THRESHOLD = 0.6; // duoi muc nay coi nhu khong lien quan

        if (bestSimilarity < RELEVANCE_THRESHOLD) {
            // Cau hoi khong lien quan gi den tai lieu -> tra loi binh thuong bang kien thuc chung
            return geminiService.askGemini(question);
        }

        String context = sortedChunks.stream()
                .map(DocumentChunk::getContent)
                .collect(Collectors.joining("\n---\n"));

        String promptWithContext = "Dua vao thong tin sau day de tra loi cau hoi mot cach chinh xac.\n\n" +
                "THONG TIN THAM KHAO:\n" + context + "\n\n" +
                "CAU HOI: " + question;

        return geminiService.askGemini(promptWithContext);
    }
}