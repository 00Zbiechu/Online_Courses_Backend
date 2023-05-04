package pl.courses.online_courses_backend.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.courses.online_courses_backend.model.CoursesDTO;
import pl.courses.online_courses_backend.search.FoundCourses;
import pl.courses.online_courses_backend.service.CoursesService;

@RestController
@RequestMapping("/api/courses")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class CoursesController extends BaseController<CoursesDTO, CoursesService> {

    private final CoursesService coursesService;


    @Override
    protected CoursesService getService() {
        return coursesService;
    }


    @GetMapping("/how-many-courses")
    public ResponseEntity<Long> howManyCoursesIsInDatabase() {
        return new ResponseEntity<>(coursesService.howManyCoursesIsInDatabase(), HttpStatus.OK);
    }

    @GetMapping("/get-course-page")
    public ResponseEntity<Page<CoursesDTO>> findCoursesPage(@RequestParam(name = "page") int page,
                                                            @RequestParam(name = "size") int size) {

        PageRequest pageRequest = PageRequest.of(page, size);
        return new ResponseEntity<>(coursesService.findCoursesPage(pageRequest), HttpStatus.OK);
    }


    @GetMapping("/search-for-courses")
    public ResponseEntity<FoundCourses> findCourseWithCriteria(@RequestBody CoursesDTO coursesDTO){

        return new ResponseEntity<>(coursesService.searchForCourses(coursesDTO),HttpStatus.OK);

    }

    @PostMapping(value = "/add-course")
    public ResponseEntity<CoursesDTO> addCourse(@RequestBody CoursesDTO course) {

        return new ResponseEntity<>(coursesService.addCourseWithRandomImageName(course), HttpStatus.CREATED);

    }

    @PostMapping(value = "/upload-file")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile multipartFile) {

        return new ResponseEntity<>(coursesService.uploadImageCourseImage(multipartFile), HttpStatus.CREATED);

    }


}
