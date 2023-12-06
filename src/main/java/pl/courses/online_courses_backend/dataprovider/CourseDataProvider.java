package pl.courses.online_courses_backend.dataprovider;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import pl.courses.online_courses_backend.entity.CourseEntity;
import pl.courses.online_courses_backend.mapper.CourseMapper;
import pl.courses.online_courses_backend.model.CourseWithAuthorDTO;
import pl.courses.online_courses_backend.model.SearchForCourseDTO;
import pl.courses.online_courses_backend.queryservice.CourseQueryService;

@Component
@RequiredArgsConstructor
public class CourseDataProvider {

    private final CourseQueryService courseQueryService;

    private final CourseMapper courseMapper;

    public Page<CourseWithAuthorDTO> searchForCourses(SearchForCourseDTO searchForCourseDTO) {
        Page<CourseEntity> foundCourses = courseQueryService.searchForCourses(searchForCourseDTO);
        return foundCourses.map(courseMapper::toCourseForList);
    }
}
