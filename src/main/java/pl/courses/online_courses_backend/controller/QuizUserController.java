package pl.courses.online_courses_backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.courses.online_courses_backend.api.QuizUserApiClient;
import pl.courses.online_courses_backend.model.wrapper.QuizUsersDTO;

@RestController
@RequestMapping("/api/quiz-user")
@RequiredArgsConstructor
public class QuizUserController {

    private final QuizUserApiClient quizUserApiClient;

    @GetMapping("/get-result")
    public ResponseEntity<QuizUsersDTO> getQuizUserResult(@RequestParam String courseTitle) {
        return quizUserApiClient.getQuizUsersResult(courseTitle);
    }
}
