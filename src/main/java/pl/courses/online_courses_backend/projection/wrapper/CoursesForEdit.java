package pl.courses.online_courses_backend.projection.wrapper;

import lombok.*;
import pl.courses.online_courses_backend.projection.CourseForEdit;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CoursesForEdit {

    List<CourseForEdit> courseForEditList;

}
