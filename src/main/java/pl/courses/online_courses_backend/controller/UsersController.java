package pl.courses.online_courses_backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.courses.online_courses_backend.model.UsersDTO;
import pl.courses.online_courses_backend.service.UsersServiceImpl;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UsersController extends BaseController<UsersDTO, UsersServiceImpl> {

    private final UsersServiceImpl usersService;

    @Override
    protected UsersServiceImpl getService() {
        return usersService;
    }
}
