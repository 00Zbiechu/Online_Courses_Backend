package pl.courses.online_courses_backend.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CoursesDTO {

    private Long id;

    private String username;

    private String title;

    private LocalDate startData;

    private LocalDate endData;

    private String topic;

    private String password;

}
