package pl.courses.online_courses_backend.model.wrapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.courses.online_courses_backend.model.CourseForListDTO;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CoursesForListDTO {

    private List<CourseForListDTO> coursesForList;
}
