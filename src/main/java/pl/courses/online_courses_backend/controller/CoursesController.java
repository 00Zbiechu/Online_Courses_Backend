package pl.courses.online_courses_backend.controller;


import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.courses.online_courses_backend.model.CoursesDTO;
import pl.courses.online_courses_backend.service.CoursesService;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CoursesController extends BaseController<CoursesDTO, CoursesService> {

    private final CoursesService coursesService;

    @Override
    protected CoursesService getService() {
        return coursesService;
    }
}
