package com.quad.trivia.trivia_api.controllers;

import com.quad.trivia.trivia_api.models.AnswerCheckRequest;
import com.quad.trivia.trivia_api.models.Question;
import com.quad.trivia.trivia_api.services.TokenService;
import com.quad.trivia.trivia_api.services.TriviaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "${cors.allowed-origins}")
public class TriviaController {

    private final TriviaService triviaService;
    private final TokenService tokenService;

    @Autowired
    public TriviaController(TriviaService triviaService, TokenService tokenService) {
        this.triviaService = triviaService;
        this.tokenService = tokenService;
    }

    @GetMapping("/questions")
    public ResponseEntity<?> getQuestions(@RequestParam String userId,
                                          @RequestParam(required = false) String category,
                                          @RequestParam(required = false) String difficulty,
                                          @RequestParam(required = false) String type,
                                          @RequestParam(defaultValue = "1") int amount) {

        String token = tokenService.getSessionToken(userId);
        List<Question> questions = triviaService.getQuestions(token, category, difficulty, type, amount);
        return ResponseEntity.ok(questions);
    }

    @PostMapping("/checkanswers")
    public ResponseEntity<Map<String, String>> checkAnswers(@RequestBody List<AnswerCheckRequest> answers) {
        Map<String, String> result = triviaService.checkAnswers(answers);
        return ResponseEntity.ok(result);
    }
}
