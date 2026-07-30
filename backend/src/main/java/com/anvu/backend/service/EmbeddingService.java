package com.anvu.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EmbeddingService {

    @Value("${gemini.api-key}")
    private String apiKey;

    private final String embedUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-embedding-001:embedContent";
    private final RestTemplate restTemplate = new RestTemplate();

    public List<Double> embed(String text) {
        String url = embedUrl + "?key=" + apiKey;

        Map<String, Object> requestBody = Map.of(
                "model", "models/gemini-embedding-001",
                "content", Map.of("parts", List.of(Map.of("text", text)))
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        Map response = restTemplate.postForObject(url, request, Map.class);
        Map embeddingObj = (Map) response.get("embedding");
        return (List<Double>) embeddingObj.get("values");
    }

    // Chuyen vector thanh chuoi de luu vao DB
    public String toStorableString(List<Double> vector) {
        return vector.stream().map(String::valueOf).collect(Collectors.joining(","));
    }

    // Chuyen chuoi luu trong DB nguoc lai thanh vector
    public List<Double> fromStorableString(String stored) {
        return List.of(stored.split(",")).stream().map(Double::parseDouble).collect(Collectors.toList());
    }

    // Tinh do tuong dong cosine giua 2 vector
    public double cosineSimilarity(List<Double> a, List<Double> b) {
        double dot = 0, normA = 0, normB = 0;
        for (int i = 0; i < a.size(); i++) {
            dot += a.get(i) * b.get(i);
            normA += Math.pow(a.get(i), 2);
            normB += Math.pow(b.get(i), 2);
        }
        return dot / (Math.sqrt(normA) * Math.sqrt(normB));
    }
}