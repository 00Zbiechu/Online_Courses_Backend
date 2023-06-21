package pl.courses.online_courses_backend.specification;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FoundCourse {

    private String title;

    private LocalDate startDate;

    private LocalDate endDate;

    private String topic;

    private String password;

    private String description;

    private String image;

    private String username;


}
