package com.quad.trivia.trivia_api.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.quad.trivia.trivia_api.models.AnswerCheckRequest;
import com.quad.trivia.trivia_api.models.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TriviaService {
    @Value("${trivia.api.base-url}")
    private String triviaApiBaseUrl;

    private final RestTemplate restTemplate;
    private final Map<String, String> correctAnswers = new ConcurrentHashMap<>();

    private final TokenService tokenService;

    @Autowired
    public TriviaService(RestTemplate restTemplate, TokenService tokenService) {
        this.restTemplate = restTemplate;
        this.tokenService = tokenService;
    }

    public List<Question> getQuestions(String token, String category, String difficulty, String type, int amount) {
        String url = buildApiUrl(token, category, difficulty, type, amount);
        ResponseEntity<JsonNode> response = restTemplate.getForEntity(url, JsonNode.class);
        JsonNode responseBody = response.getBody();

        int responseCode = responseBody.get("response_code").asInt();
        if (responseCode == 4) {
            String newToken = tokenService.resetSessionToken(token);
            return getQuestions(newToken, category, difficulty, type, amount);
        }
        else if (responseCode != 0) {
            throw new RuntimeException("Error occurred while fetching questions.");
        }

        List<Question> questions = new ArrayList<>();
        for (JsonNode questionNode : responseBody.get("results")) {
            Question question = new Question();
            question.setQuestion(questionNode.get("question").asText());
            question.setOptions(buildOptionsList(questionNode));
            String correctAnswer = questionNode.get("correct_answer").asText();

            correctAnswers.put(question.getQuestion(), correctAnswer);
            questions.add(question);
        }
        return questions;
    }

    public Map<String, String> checkAnswers(List<AnswerCheckRequest> answers) {
        Map<String, String> result = new HashMap<>();
        for (AnswerCheckRequest answer : answers) {
            String correctAnswer = correctAnswers.get(answer.getQuestion());
            if (correctAnswer != null) {
                result.put(answer.getQuestion(), correctAnswer.equals(answer.getAnswer()) ? "correct" : "incorrect");
            } else {
                result.put(answer.getQuestion(), "unknown question");
            }
        }
        return result;
    }

    private String buildApiUrl(String token, String category, String difficulty, String type, int amount) {
        String url = triviaApiBaseUrl + "?amount=" + amount + "&token=" + token;
        if (category != null && !category.equals("any")) {
            url += "&category=" + category;
        }
        if (difficulty != null && !difficulty.equals("any")) {
            url += "&difficulty=" + difficulty;
        }
        if (type != null && !type.equals("any")) {
            url += "&type=" + type;
        }
        return url;
    }

    private List<String> buildOptionsList(JsonNode questionNode) {
        List<String> options = new ArrayList<>();
        options.add(questionNode.get("correct_answer").asText());
        questionNode.get("incorrect_answers").forEach(option -> options.add(option.asText()));
        Collections.shuffle(options);
        return options;
    }
}
