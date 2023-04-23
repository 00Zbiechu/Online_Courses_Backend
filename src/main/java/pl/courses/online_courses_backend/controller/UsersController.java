package pl.courses.online_courses_backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.courses.online_courses_backend.model.UsersDTO;
import pl.courses.online_courses_backend.service.UsersService;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class UsersController extends BaseController<UsersDTO, UsersService> {

    private final UsersService usersService;

    @Override
    protected UsersService getService() {
        return usersService;
    }
}
