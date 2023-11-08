package pl.courses.online_courses_backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class SearchForCourseDTO extends PaginationForCourseListDTO {

    private String title;

    private LocalDate startDate;

    private LocalDate endDate;

    private String topic;

    private String username;
}
