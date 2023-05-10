package pl.courses.online_courses_backend.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import pl.courses.online_courses_backend.model.CoursesDTO;
import pl.courses.online_courses_backend.specification.FoundCourses;

import java.time.LocalDate;

public interface CourseController {

    ResponseEntity<Long> howManyCoursesIsInDatabase();

    ResponseEntity<Page<CoursesDTO>> findCoursesPage(Integer page, Integer size, String sort, String order);

    ResponseEntity<FoundCourses> findCourseWithCriteria(String title, LocalDate startDate, LocalDate endDate, String topic);

    ResponseEntity<CoursesDTO> addCourse(CoursesDTO course);

    ResponseEntity<String> uploadFile(MultipartFile multipartFile);

}
