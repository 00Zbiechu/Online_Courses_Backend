package pl.courses.online_courses_backend.model.wrapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
