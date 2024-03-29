package pl.courses.online_courses_backend.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pl.courses.online_courses_backend.dataprovider.CourseDataProvider;
import pl.courses.online_courses_backend.model.AddCourseDTO;
import pl.courses.online_courses_backend.model.AddTopicDTO;
import pl.courses.online_courses_backend.model.CourseDTO;
import pl.courses.online_courses_backend.model.CourseWithAuthorDTO;
import pl.courses.online_courses_backend.model.EditCourseDTO;
import pl.courses.online_courses_backend.model.FileDataDTO;
import pl.courses.online_courses_backend.model.PaginationForCourseListDTO;
import pl.courses.online_courses_backend.model.SearchForCourseDTO;
import pl.courses.online_courses_backend.model.wrapper.CoursesDTO;
import pl.courses.online_courses_backend.model.wrapper.ParticipantsDTO;
import pl.courses.online_courses_backend.model.wrapper.TopicsDTO;
import pl.courses.online_courses_backend.photo.PhotoDTO;
import pl.courses.online_courses_backend.service.CourseService;
import pl.courses.online_courses_backend.validator.AddCourseValidator;
import pl.courses.online_courses_backend.validator.EditCourseValidator;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController extends BaseController<CourseDTO, CourseService> {

    private final CourseService courseService;

    private final CourseDataProvider courseDataProvider;

    private final AddCourseValidator addCourseValidator;

    private final EditCourseValidator editCourseValidator;

    @InitBinder("addCourseDTO")
    public void addValidationForAddCourseDTO(WebDataBinder binder) {
        binder.addValidators(addCourseValidator);
    }

    @InitBinder("editCourseDTO")
    public void addValidationForEditCourseDTO(WebDataBinder binder) {
        binder.addValidators(editCourseValidator);
    }

    @Override
    protected CourseService getService() {
        return courseService;
    }

    @PostMapping("/get-course-page")
    public ResponseEntity<Page<CourseWithAuthorDTO>> findCoursesPage(@Valid @RequestBody PaginationForCourseListDTO paginationForCourseListDTO) {
        return new ResponseEntity<>(courseService.findCoursesPage(paginationForCourseListDTO), HttpStatus.OK);
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
    public ResponseEntity<CourseWithAuthorDTO> getCourse(@RequestParam Long courseId, @RequestParam(required = false) String password) {
        return new ResponseEntity<>(courseService.getCourse(courseId, password), HttpStatus.OK);
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
    public ResponseEntity<TopicsDTO> addTopicToCourse(@RequestParam Long courseId,
                                                      @RequestPart(required = false) MultipartFile[] files,
                                                      @Valid @RequestPart AddTopicDTO addTopicDTO) {
        return new ResponseEntity<>(courseService.addTopic(courseId, files, addTopicDTO), HttpStatus.CREATED);
    }

    @GetMapping("/get-topics")
    public ResponseEntity<TopicsDTO> getTopicsForCourse(@RequestParam Long courseId, @RequestParam(required = false) String password) {
        return new ResponseEntity<>(courseService.getTopics(courseId, password), HttpStatus.OK);
    }

    @DeleteMapping("/delete-topic")
    public ResponseEntity<TopicsDTO> deleteTopicForCourse(@RequestParam Long courseId, @RequestParam Long topicId) {
        return new ResponseEntity<>(courseService.deleteTopic(courseId, topicId), HttpStatus.OK);
    }

    @GetMapping("/get-attachment")
    public ResponseEntity<FileDataDTO> getCourseAttachment(@RequestParam Long courseId, @RequestParam Long topicId, @RequestParam Long fileId, @RequestParam(required = false) String password) {
        return new ResponseEntity<>(courseService.getAttachment(courseId, topicId, fileId, password), HttpStatus.OK);
    }

    @GetMapping("/get-course-participants")
    public ResponseEntity<ParticipantsDTO> getCourseParticipants(@RequestParam Long courseId) {
        return new ResponseEntity<>(courseService.getCourseParticipants(courseId), HttpStatus.OK);
    }

    @PostMapping("/add-course-participant")
    public ResponseEntity<Void> addCourseParticipant(@RequestParam Long courseId, @RequestParam String username) {
        courseService.addCourseParticipant(courseId, username);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete-course-participant")
    public ResponseEntity<ParticipantsDTO> deleteCourseParticipant(@RequestParam Long courseId, @RequestParam Long userId) {
        return new ResponseEntity<>(courseService.deleteCourseParticipant(courseId, userId), HttpStatus.ACCEPTED);
    }

    @GetMapping("/get-courses-where-user-is-participant")
    public ResponseEntity<List<CourseWithAuthorDTO>> getCoursesWhereUserIsParticipant() {
        return new ResponseEntity<>(courseService.getCoursesWhereUserIsParticipant(), HttpStatus.OK);
    }
}
