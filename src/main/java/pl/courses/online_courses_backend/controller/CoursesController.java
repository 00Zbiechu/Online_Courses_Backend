package pl.courses.online_courses_backend.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.courses.online_courses_backend.model.CoursesDTO;
import pl.courses.online_courses_backend.other.FileStorageUtil;
import pl.courses.online_courses_backend.service.CoursesService;

import java.io.IOException;

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


    @GetMapping("/get-course-page")
    public ResponseEntity<Page<CoursesDTO>> findCoursesPage(Pageable pageable) {
        return new ResponseEntity<>(coursesService.findCoursesPage(pageable), HttpStatus.OK);
    }


    @PostMapping(value = "/add-course", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CoursesDTO> uploadFile(
            @RequestPart CoursesDTO coursesDTO, @RequestPart("file") MultipartFile multipartFile)
            throws IOException {

        String codeName = fileStorageUtil.saveFile(multipartFile);
        coursesDTO.setImage(codeName);
        getService().create(coursesDTO);


        return new ResponseEntity<>(coursesDTO, HttpStatus.CREATED);


    }


}
