package pl.courses.online_courses_backend.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import pl.courses.online_courses_backend.model.AddCourseDTO;
import pl.courses.online_courses_backend.model.CourseDTO;
import pl.courses.online_courses_backend.model.CourseForListDTO;
import pl.courses.online_courses_backend.model.PaginationForCourseListDTO;
import pl.courses.online_courses_backend.model.wrapper.CoursesDTO;
import pl.courses.online_courses_backend.model.wrapper.CoursesForUserDTO;
import pl.courses.online_courses_backend.photo.PhotoDTO;

public interface CourseService extends BaseService<CourseDTO> {

    Page<CourseForListDTO> findCoursesPage(Pageable pageable);

    CoursesDTO addCourse(AddCourseDTO addCourseDTO);

    CoursesDTO deleteCourse(Long courseId);

    PhotoDTO getCourseImage(Long courseId);

    PhotoDTO uploadCourseImage(Long courseId, MultipartFile multipartFile);

    PhotoDTO deleteCourseImage(Long courseId);

    PageRequest buildPageRequestForCoursePage(PaginationForCourseListDTO paginationForCourseListDTO);

    CoursesForUserDTO getCourseDataForUser();
}
