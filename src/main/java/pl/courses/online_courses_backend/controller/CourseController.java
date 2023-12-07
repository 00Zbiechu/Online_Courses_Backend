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
import pl.courses.online_courses_backend.model.wrapper.TopicsDTO;
import pl.courses.online_courses_backend.photo.PhotoDTO;
import pl.courses.online_courses_backend.service.CourseService;
import pl.courses.online_courses_backend.validator.AddCourseValidator;
import pl.courses.online_courses_backend.validator.EditCourseValidator;
import pl.courses.online_courses_backend.validator.TopicValidator;

@RestController
@RequestMapping("/api/courses")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class CourseController extends BaseController<CourseDTO, CourseService> {

    private final CourseService courseService;

    private final CourseDataProvider courseDataProvider;

    private final AddCourseValidator addCourseValidator;

    private final EditCourseValidator editCourseValidator;

    private final TopicValidator topicValidator;

    @InitBinder("addCourseDTO")
    public void addValidationForAddCourseDTO(WebDataBinder binder) {
        binder.addValidators(addCourseValidator);
    }

    @InitBinder("editCourseDTO")
    public void addValidationForEditCourseDTO(WebDataBinder binder) {
        binder.addValidators(editCourseValidator);
    }

    @InitBinder("topicDTO")
    public void addValidationForTopicDTO(WebDataBinder binder) {
        binder.addValidators(topicValidator);
    }

    @Override
    protected CourseService getService() {
        return courseService;
    }

    @PostMapping("/get-course-page")
    public ResponseEntity<Page<CourseWithAuthorDTO>> findCoursesPage(@Valid @RequestBody PaginationForCourseListDTO paginationForCourseListDTO) {
        PageRequest pageRequest = courseService.buildPageRequestForCoursePage(paginationForCourseListDTO);
        return new ResponseEntity<>(courseService.findCoursesPage(pageRequest), HttpStatus.OK);
    }

    @GetMapping("/get-courses-for-user")
    public ResponseEntity<CoursesDTO> getCourseDataForUser() {
        return new ResponseEntity<>(courseService.getCourseDataForUser(), HttpStatus.OK);
    }

    @PostMapping("/search-for-courses")
    public ResponseEntity<Page<CourseWithAuthorDTO>> searchForCourses(@Valid @RequestBody SearchForCourseDTO searchForCourseDTO) {
        return new ResponseEntity<>(courseDataProvider.searchForCourses(searchForCourseDTO), HttpStatus.OK);
    }

    @GetMapping("/get-course")
    public ResponseEntity<CourseWithAuthorDTO> getCourse(@RequestParam Long courseId) {
        return new ResponseEntity<>(courseService.getCourse(courseId), HttpStatus.OK);
    }

    @PostMapping("/add-course")
    public ResponseEntity<CourseDTO> addCourse(@Valid @RequestBody AddCourseDTO addCourseDTO) {
        return new ResponseEntity<>(courseService.addCourse(addCourseDTO), HttpStatus.CREATED);
    }

    @PostMapping("/edit-course")
    public ResponseEntity<CourseDTO> editCourse(@Valid @RequestBody EditCourseDTO editCourseDTO) {
        return new ResponseEntity<>(courseService.editCourse(editCourseDTO), HttpStatus.OK);
    }

    @DeleteMapping("/delete-course")
    public ResponseEntity<CoursesDTO> deleteCourse(@RequestParam Long courseId) {
        return new ResponseEntity<>(courseService.deleteCourse(courseId), HttpStatus.ACCEPTED);
    }

    @GetMapping("/get-photo")
    public ResponseEntity<PhotoDTO> getCourseImage(@RequestParam Long courseId) {
        return new ResponseEntity<>(courseService.getCourseImage(courseId), HttpStatus.OK);
    }

    @PostMapping("/upload-photo")
    public ResponseEntity<PhotoDTO> uploadCourseImage(@RequestParam Long courseId, @RequestParam MultipartFile photo) {
        return new ResponseEntity<>(courseService.uploadCourseImage(courseId, photo), HttpStatus.OK);
    }

    @DeleteMapping("/delete-photo")
    public ResponseEntity<PhotoDTO> deleteCourseImage(@RequestParam Long courseId) {
        return new ResponseEntity<>(courseService.deleteCourseImage(courseId), HttpStatus.ACCEPTED);
    }

    @PostMapping("/add-topic")
    public ResponseEntity<TopicsDTO> addTopicToCourse(@RequestParam Long courseId, @RequestBody TopicDTO topicDTO) {
        return new ResponseEntity<>(courseService.addTopic(courseId, topicDTO), HttpStatus.CREATED);
    }
}
