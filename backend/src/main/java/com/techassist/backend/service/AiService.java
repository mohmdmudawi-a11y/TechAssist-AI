package com.techassist.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.techassist.backend.dto.CategorizeRequest;
import com.techassist.backend.dto.CategorizeResponse;
import com.techassist.backend.dto.TroubleshootRequest;
import com.techassist.backend.dto.TroubleshootResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class AiService {

    @Value("${openrouter.api-key}")
    private String apiKey;

    @Value("${openrouter.url}")
    private String apiUrl;

    @Value("${openrouter.models}")
    private String modelsCsv;

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(30))
            .build();

    private final ObjectMapper objectMapper = new ObjectMapper();

    // =====================================================
    // FEATURE 1 — Categorize
    // =====================================================
    public CategorizeResponse categorizeTicket(CategorizeRequest request) {
        String prompt = """
                You are an IT support ticket classifier.

                Given the ticket below, respond in EXACTLY this format (no extra text):

                Category: <one of: Hardware, Software, Network, Account & Access, Security>
                Subcategory: <short, e.g. VPN, Wi-Fi, Password, Printer>
                Priority: <one of: CRITICAL, HIGH, MEDIUM, LOW>
                Reasoning: <one short sentence>

                Ticket title: %s
                Ticket description: %s
                """.formatted(request.getTitle(), request.getDescription());

        String aiText = callAi(prompt);
        return parseCategorizeResponse(aiText);
    }

    // =====================================================
    // FEATURE 2 — Troubleshoot
    // =====================================================
    public TroubleshootResponse troubleshoot(TroubleshootRequest request) {
        String prompt = """
                You are a senior IT technician.

                For the problem below, provide a concise, numbered list of
                troubleshooting steps. Return ONLY the numbered list,
                one step per line. Do not add extra commentary.

                Problem: %s
                """.formatted(request.getProblem());

        String aiText = callAi(prompt);
        return parseTroubleshootResponse(aiText);
    }

    // =====================================================
    // HTTP call with model fallback
    // =====================================================
    private String callAi(String userPrompt) {
        List<String> models = Arrays.stream(modelsCsv.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();

        Exception lastError = null;
        for (String model : models) {
            try {
                String result = callOneModel(model, userPrompt);
                System.out.println("[AiService] Success with model: " + model);
                return result;
            } catch (Exception e) {
                System.out.println("[AiService] Model failed: " + model + " — " + e.getMessage());
                lastError = e;
            }
        }
        throw new RuntimeException("All AI models failed. Last error: "
                + (lastError != null ? lastError.getMessage() : "unknown"));
    }

    private String callOneModel(String model, String userPrompt) throws Exception {
        ObjectNode body = objectMapper.createObjectNode();
        body.put("model", model);
        body.put("temperature", 0.3);

        ArrayNode messages = body.putArray("messages");
        ObjectNode userMessage = messages.addObject();
        userMessage.put("role", "user");
        userMessage.put("content", userPrompt);

        String jsonBody = objectMapper.writeValueAsString(body);

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .timeout(Duration.ofSeconds(60))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .header("HTTP-Referer", "http://localhost:8080")
                .header("X-Title", "TechAssist-AI")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = httpClient.send(
                httpRequest, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("HTTP " + response.statusCode() + ": " + response.body());
        }

        JsonNode root = objectMapper.readTree(response.body());
        return root.path("choices").path(0).path("message").path("content").asText();
    }

    // =====================================================
    // Parsing helpers
    // =====================================================
    private CategorizeResponse parseCategorizeResponse(String text) {
        return CategorizeResponse.builder()
                .category(extractLine(text, "Category:"))
                .subcategory(extractLine(text, "Subcategory:"))
                .priority(extractLine(text, "Priority:"))
                .reasoning(extractLine(text, "Reasoning:"))
                .build();
    }

    private String extractLine(String text, String prefix) {
        for (String line : text.split("\\r?\\n")) {
            if (line.trim().startsWith(prefix)) {
                return line.substring(prefix.length()).trim();
            }
        }
        return "";
    }

    private TroubleshootResponse parseTroubleshootResponse(String text) {
        List<String> steps = new ArrayList<>();
        for (String line : text.split("\\r?\\n")) {
            String trimmed = line.trim();
            if (trimmed.matches("^\\d+[.)].*")) {
                steps.add(trimmed.replaceFirst("^\\d+[.)]\\s*", ""));
            }
        }
        return TroubleshootResponse.builder()
                .steps(steps)
                .rawText(text)
                .build();
    }
}