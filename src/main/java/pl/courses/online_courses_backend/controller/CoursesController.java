package pl.courses.online_courses_backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.courses.online_courses_backend.model.CoursesDTO;
import pl.courses.online_courses_backend.service.CoursesServiceImpl;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CoursesController extends BaseController<CoursesDTO, CoursesServiceImpl> {

    private final CoursesServiceImpl coursesService;

    @Override
    protected CoursesServiceImpl getService() {
        return coursesService;
    }
}
