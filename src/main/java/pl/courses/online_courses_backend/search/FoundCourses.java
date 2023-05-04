package pl.courses.online_courses_backend.search;

import lombok.*;
import pl.courses.online_courses_backend.model.CoursesDTO;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FoundCourses {

    List<CoursesDTO> foundCoursesList;

}
