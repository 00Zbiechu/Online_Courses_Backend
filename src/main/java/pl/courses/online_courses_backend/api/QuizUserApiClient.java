package pl.courses.online_courses_backend.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import pl.courses.online_courses_backend.model.QuizUserDTO;

@FeignClient(value = "online-courses-quiz", url = "localhost:8040/api/quiz-user")
public interface QuizUserApiClient {

    @RequestMapping(method = RequestMethod.GET, value = "/get-result")
    ResponseEntity<QuizUserDTO> getQuizUserResult(@RequestParam String username, @RequestParam String courseTitle);
}
