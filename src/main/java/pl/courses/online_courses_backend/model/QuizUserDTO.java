package pl.courses.online_courses_backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuizUserDTO {

    private String username;

    private int correctAnswer = 0;

    private int wrongAnswer = 0;

    private String courseTitle;
}
