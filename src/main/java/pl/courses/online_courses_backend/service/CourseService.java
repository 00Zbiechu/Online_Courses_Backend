package pl.courses.online_courses_backend.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import pl.courses.online_courses_backend.model.AddCoursesDTO;
import pl.courses.online_courses_backend.model.CoursesDTO;
import pl.courses.online_courses_backend.projection.CourseForList;
import pl.courses.online_courses_backend.projection.wrapper.CoursesForAdmin;
import pl.courses.online_courses_backend.specification.FoundCourses;

import java.time.LocalDate;

public interface CourseService extends BaseService<CoursesDTO> {

    Long howManyCoursesIsInDatabase();

    Page<CourseForList> findCoursesPage(Pageable pageable);

    CoursesDTO addCourseWithRandomImageName(AddCoursesDTO addCoursesDTO);

    String uploadImageCourseImage(MultipartFile multipartFile);

    FoundCourses searchForCourses(String title, LocalDate startDate, LocalDate endDate, String topic, String username);

    PageRequest buildPageRequestForCoursePage(Integer page, Integer size, String sort, String order);

    CoursesForAdmin getCourseDataForAdmin(String username);


}
