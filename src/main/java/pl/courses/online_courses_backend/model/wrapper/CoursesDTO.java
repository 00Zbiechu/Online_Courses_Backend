package pl.courses.online_courses_backend.model.wrapper;

import lombok.*;
import pl.courses.online_courses_backend.model.CourseDTO;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CoursesDTO {

    private List<CourseDTO> foundCoursesList;
}
