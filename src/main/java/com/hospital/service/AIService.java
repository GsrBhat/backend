package com.hospital.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.*;

@Service
public class AIService {

    @Value("${huggingface.api.key}")
    private String apiKey;

    @Value("${huggingface.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    // 🔥 MAIN AI METHOD
    public String predictSpecialization(String symptoms) {

        // If API key not set → fallback
        if (apiKey == null || apiKey.isBlank() || apiKey.equals("YOUR_HUGGINGFACE_API_KEY")) {
            return mockPrediction(symptoms);
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + apiKey);

            Map<String, Object> payload = new HashMap<>();
            payload.put("inputs", symptoms);

            // ✅ MATCHES YOUR DB EXACTLY
            payload.put("parameters", Map.of(
                    "candidate_labels",
                    List.of(
                            "Cardiology",
                            "Dermatology",
                            "Neurology",
                            "Pediatrics",
                            "Orthopedics",
                            "General Medicine")));

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);

            Map<String, Object> response = restTemplate.postForObject(apiUrl, entity, Map.class);

            if (response != null && response.containsKey("labels")) {
                List<String> labels = (List<String>) response.get("labels");

                if (!labels.isEmpty()) {
                    return labels.get(0); // already matches DB
                }
            }

        } catch (Exception e) {
            System.out.println("⚠️ AI Error → Using fallback");
        }

        return mockPrediction(symptoms);
    }

    // 🔥 FALLBACK (IMPORTANT)
    private String mockPrediction(String symptoms) {

        String s = symptoms.toLowerCase();

        if (s.contains("heart") || s.contains("chest"))
            return "Cardiology";

        if (s.contains("skin") || s.contains("rash"))
            return "Dermatology";

        if (s.contains("brain") || s.contains("headache") || s.contains("nerve"))
            return "Neurology";

        if (s.contains("child") || s.contains("baby"))
            return "Pediatrics";

        if (s.contains("bone") || s.contains("joint") || s.contains("fracture"))
            return "Orthopedics";

        return "General Medicine";
    }
}