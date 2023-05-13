package pl.courses.online_courses_backend.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.courses.online_courses_backend.model.CoursesDTO;
import pl.courses.online_courses_backend.projection.CourseForList;
import pl.courses.online_courses_backend.projection.wrapper.CoursesForCalendar;
import pl.courses.online_courses_backend.projection.wrapper.CoursesForEdit;
import pl.courses.online_courses_backend.service.CourseService;
import pl.courses.online_courses_backend.specification.FoundCourses;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/courses")
@CrossOrigin(origins = "http://localhost:4200")
@PreAuthorize("hasRole('USER')")
@RequiredArgsConstructor
public class CoursesController extends BaseController<CoursesDTO, CourseService> {

    private final CourseService courseService;

    @Override
    protected CourseService getService() {
        return courseService;
    }


    @GetMapping("/how-many-courses")
    public ResponseEntity<Long> howManyCoursesIsInDatabase() {
        return new ResponseEntity<>(courseService.howManyCoursesIsInDatabase(), HttpStatus.OK);
    }

    @GetMapping("/get-course-page")
    public ResponseEntity<Page<CourseForList>> findCoursesPage(@RequestParam(name = "page") Integer page,
                                                               @RequestParam(name = "size") Integer size,
                                                               @RequestParam(name = "sort") String sort,
                                                               @RequestParam(name = "order") String order) {


        PageRequest pageRequest = courseService.buildPageRequestForCoursePage(page, size, sort, order);

        return new ResponseEntity<>(courseService.findCoursesPage(pageRequest), HttpStatus.OK);
    }


    @GetMapping("/search-for-courses")
    public ResponseEntity<FoundCourses> findCourseWithCriteria(@RequestParam(value = "title", required = false) String title,
                                                               @RequestParam(value = "startDate", required = false) LocalDate startDate,
                                                               @RequestParam(value = "endDate", required = false) LocalDate endDate,
                                                               @RequestParam(value = "topic", required = false) String topic) {

        return new ResponseEntity<>(courseService.searchForCourses(title, startDate, endDate, topic), HttpStatus.OK);

    }

    @PostMapping(value = "/add-course")
    public ResponseEntity<CoursesDTO> addCourse(@Valid @RequestBody CoursesDTO course) {

        return new ResponseEntity<>(courseService.addCourseWithRandomImageName(course), HttpStatus.CREATED);

    }


    @PostMapping(value = "/upload-file")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile multipartFile) {

        return new ResponseEntity<>(courseService.uploadImageCourseImage(multipartFile), HttpStatus.CREATED);

    }


    @GetMapping(value = "/get-course-data-for-calendar")
    public ResponseEntity<CoursesForCalendar> getCourseDataForCalendar() {

        return new ResponseEntity<>(courseService.getCourseDataForCalendar(), HttpStatus.OK);

    }

    @GetMapping(value = "/get-course-data-for-edit")
    public ResponseEntity<CoursesForEdit> getCourseDataForEdit() {

        return new ResponseEntity<>(courseService.getCourseDataForEdit(), HttpStatus.OK);

    }


}
