package pl.courses.online_courses_backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.courses.online_courses_backend.service.CourseUsersService;

@RestController
@RequestMapping("/api/course-users")
@RequiredArgsConstructor
public class CourseUsersController {

    private final CourseUsersService courseUsersService;

    @GetMapping("/confirm-participation")
    public ResponseEntity<String> confirmParticipation(@RequestParam String token) {
        return new ResponseEntity<>(courseUsersService.confirmParticipation(token), HttpStatus.OK);
    }
}
