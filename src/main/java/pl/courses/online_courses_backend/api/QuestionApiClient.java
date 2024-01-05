package pl.courses.online_courses_backend.api;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import pl.courses.online_courses_backend.model.QuestionDTO;
import pl.courses.online_courses_backend.model.wrapper.QuestionsDTO;

@FeignClient(value = "online-courses-quiz-question", url = "localhost:8040/api/question")
public interface QuestionApiClient {

    @GetMapping("/question-list")
    ResponseEntity<QuestionsDTO> getQuestionListForCourse(@RequestParam String courseTitle);

    @DeleteMapping("/delete-question")
    ResponseEntity<QuestionsDTO> deleteQuestion(@RequestParam String courseTitle, @RequestParam String title);

    @PostMapping("/add-question")
    ResponseEntity<QuestionsDTO> addQuestion(@Valid @RequestBody QuestionDTO questionDTO);
}
