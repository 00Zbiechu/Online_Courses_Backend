package pl.courses.online_courses_backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.courses.online_courses_backend.api.QuestionApiClient;
import pl.courses.online_courses_backend.model.QuestionDTO;
import pl.courses.online_courses_backend.model.wrapper.QuestionsDTO;

@RestController
@RequestMapping("/api/question")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionApiClient questionApiClient;

    @GetMapping("/question-list")
    ResponseEntity<QuestionsDTO> getQuestionListForCourse(@RequestParam String courseTitle) {
        return questionApiClient.getQuestionListForCourse(courseTitle);
    }

    @DeleteMapping("/delete-question")
    public ResponseEntity<QuestionsDTO> deleteQuestion(@RequestParam String courseTitle, @RequestParam String title) {
        return questionApiClient.deleteQuestion(courseTitle, title);
    }

    @PostMapping("/add-question")
    public ResponseEntity<QuestionsDTO> addQuestion(@Valid @RequestBody QuestionDTO questionDTO, @RequestParam String courseTitle) {
        return questionApiClient.addQuestion(questionDTO, courseTitle);
    }
}
