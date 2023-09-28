package pl.courses.online_courses_backend.dataprovider;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.courses.online_courses_backend.mapper.CourseMapper;
import pl.courses.online_courses_backend.model.CourseForListDTO;
import pl.courses.online_courses_backend.model.SearchForCourseDTO;
import pl.courses.online_courses_backend.model.wrapper.CoursesForListDTO;
import pl.courses.online_courses_backend.queryservice.CourseQueryService;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CourseDataProvider {

    private final CourseQueryService courseQueryService;

    private final CourseMapper courseMapper;

    public CoursesForListDTO searchForCourses(SearchForCourseDTO searchForCourseDTO) {
        List<CourseForListDTO> foundCourses = courseQueryService.searchForCourses(searchForCourseDTO).stream()
                .map(courseMapper::toCourseForList)
                .toList();

        return CoursesForListDTO.builder().coursesForList(foundCourses).build();
    }
}
