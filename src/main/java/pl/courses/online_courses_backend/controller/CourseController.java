package pl.courses.online_courses_backend.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.courses.online_courses_backend.dataprovider.CourseDataProvider;
import pl.courses.online_courses_backend.model.*;
import pl.courses.online_courses_backend.model.wrapper.CoursesDTO;
import pl.courses.online_courses_backend.model.wrapper.CoursesForUserDTO;
import pl.courses.online_courses_backend.photo.PhotoDTO;
import pl.courses.online_courses_backend.service.CourseService;
import pl.courses.online_courses_backend.validator.AddCourseValidator;

@RestController
@RequestMapping("/api/courses")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class CourseController extends BaseController<CourseDTO, CourseService> {

    private final CourseService courseService;

    private final CourseDataProvider courseDataProvider;

    private final AddCourseValidator addCourseValidator;

    @InitBinder("addCourseDTO")
    public void addValidationForAddCourseDTO(WebDataBinder binder) {
        binder.addValidators(addCourseValidator);
    }

    @Override
    protected CourseService getService() {
        return courseService;
    }

    @PostMapping("/get-course-page")
    public ResponseEntity<Page<CourseForListDTO>> findCoursesPage(@Valid @RequestBody PaginationForCourseListDTO paginationForCourseListDTO) {
        PageRequest pageRequest = courseService.buildPageRequestForCoursePage(paginationForCourseListDTO);
        return new ResponseEntity<>(courseService.findCoursesPage(pageRequest), HttpStatus.OK);
    }

    @GetMapping(value = "/get-courses-for-user")
    public ResponseEntity<CoursesForUserDTO> getCourseDataForUser() {
        return new ResponseEntity<>(courseService.getCourseDataForUser(), HttpStatus.OK);
    }

    @PostMapping("/search-for-courses")
    public ResponseEntity<Page<CourseForListDTO>> searchForCourses(@Valid @RequestBody SearchForCourseDTO searchForCourseDTO) {
        return new ResponseEntity<>(courseDataProvider.searchForCourses(searchForCourseDTO), HttpStatus.OK);
    }

    @PostMapping(value = "/add-course")
    public ResponseEntity<CoursesDTO> addCourse(@Valid @RequestPart AddCourseDTO addCourseDTO,
                                                @Valid @RequestPart(required = false) MultipartFile photo) {
        return new ResponseEntity<>(courseService.addCourse(addCourseDTO, photo), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/delete-course")
    public ResponseEntity<CoursesDTO> deleteCourse(@RequestParam Long courseId) {
        return new ResponseEntity<>(courseService.deleteCourse(courseId), HttpStatus.ACCEPTED);
    }

    @GetMapping(value = "/get-photo")
    public ResponseEntity<PhotoDTO> getCourseImage(@RequestParam Long courseId) {
        return new ResponseEntity<>(courseService.getCourseImage(courseId), HttpStatus.OK);
    }

    @PostMapping(value = "/upload-photo")
    public ResponseEntity<PhotoDTO> uploadCourseImage(@RequestParam Long courseId, @RequestBody MultipartFile photo) {
        return new ResponseEntity<>(courseService.uploadCourseImage(courseId, photo), HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete-photo")
    public ResponseEntity<PhotoDTO> deleteCourseImage(@RequestParam Long courseId) {
        return new ResponseEntity<>(courseService.deleteCourseImage(courseId), HttpStatus.ACCEPTED);
    }
}
