package pl.courses.online_courses_backend.model;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CoursesDTO {

    private Long id;

    private String title;

    private LocalDate startData;

    private LocalDate endData;

    private String topic;

    private String password;

}
