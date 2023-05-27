package pl.courses.online_courses_backend.projection.wrapper;

import lombok.*;
import pl.courses.online_courses_backend.projection.CourseForAdmin;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CoursesForAdmin {

    List<CourseForAdmin> courseForAdminList;

}
