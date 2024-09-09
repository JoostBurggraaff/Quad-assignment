package com.quad.trivia.trivia_api.services;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenService {
    @Value("${trivia.api.token-url}")
    private String TOKEN_URL;

    @Value("${trivia.api.reset-token-url}")
    private String RESET_TOKEN_URL;

    private final RestTemplate restTemplate;
    private final Map<String, String> userTokens = new ConcurrentHashMap<>();

    @Autowired
    public TokenService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getSessionToken(String userId) {
        if (!userTokens.containsKey(userId)) {
            ResponseEntity<JsonNode> tokenResponse = restTemplate.getForEntity(TOKEN_URL, JsonNode.class);
            if (tokenResponse.getBody() != null && tokenResponse.getBody().get("response_code").asInt() == 0) {
                String token = tokenResponse.getBody().get("token").asText();
                userTokens.put(userId, token);
            } else {
                throw new RuntimeException("Failed to retrieve session token.");
            }
        }
        return userTokens.get(userId);
    }

    public String resetSessionToken(String token) {
        if (token != null) {
            Optional<String> userIdOptional = userTokens.entrySet()
                    .stream()
                    .filter(entry -> entry.getValue().equals(token))
                    .map(Map.Entry::getKey)
                    .findFirst();

            if (userIdOptional.isPresent()) {
                String userId = userIdOptional.get();

                ResponseEntity<JsonNode> resetResponse = restTemplate.getForEntity(RESET_TOKEN_URL + token, JsonNode.class);
                if (resetResponse.getBody() != null && resetResponse.getBody().get("response_code").asInt() == 0) {
                    String newToken = resetResponse.getBody().get("token").asText();
                    userTokens.put(userId, newToken);
                    return newToken;
                } else {
                    throw new RuntimeException("Failed to reset session token.");
                }
            } else {
                throw new RuntimeException("No user found with the provided token.");
            }
        } else {
            throw new IllegalArgumentException("Token cannot be null.");
        }
    }
}
