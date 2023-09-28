package pl.courses.online_courses_backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchForCourseDTO {

    private String title;

    private LocalDate startDate;

    private LocalDate endDate;

    private String topic;

    private String username;
}
