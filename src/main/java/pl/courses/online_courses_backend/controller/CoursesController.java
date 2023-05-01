package pl.courses.online_courses_backend.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.courses.online_courses_backend.model.CoursesDTO;
import pl.courses.online_courses_backend.other.FileStorageUtil;
import pl.courses.online_courses_backend.service.CoursesService;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api/courses")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class CoursesController extends BaseController<CoursesDTO, CoursesService> {

    private final CoursesService coursesService;

    private final FileStorageUtil fileStorageUtil;

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

    @PostMapping(value = "/add-course")
    public ResponseEntity<CoursesDTO> addCourse(@RequestBody CoursesDTO course) {

        course.setImage(coursesService.generateRandomImageName(16));
        return new ResponseEntity<>(coursesService.create(course), HttpStatus.CREATED);

    }

    @PostMapping(value = "/upload-file")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile multipartFile) throws IOException {

        fileStorageUtil.saveFile(multipartFile);
        return new ResponseEntity<>("Image saved", HttpStatus.CREATED);

    }


}
