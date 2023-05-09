package pl.courses.online_courses_backend.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import pl.courses.online_courses_backend.model.CoursesDTO;
import pl.courses.online_courses_backend.specification.FoundCourses;

import java.time.LocalDate;

public interface CourseService extends BaseService<CoursesDTO> {

    Long howManyCoursesIsInDatabase();

    Page<CoursesDTO> findCoursesPage(Pageable pageable);

    CoursesDTO addCourseWithRandomImageName(CoursesDTO coursesDTO);

    String uploadImageCourseImage(MultipartFile multipartFile);

    FoundCourses searchForCourses(String title, LocalDate startDate, LocalDate endDate, String topic);

    PageRequest buildPageRequestForCoursePage(Integer page, Integer size, String sort, String order);


}
