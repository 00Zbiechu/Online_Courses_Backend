package pl.courses.online_courses_backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.courses.online_courses_backend.authentication.AuthenticationRequest;
import pl.courses.online_courses_backend.authentication.AuthenticationResponse;
import pl.courses.online_courses_backend.model.UsersDTO;
import pl.courses.online_courses_backend.service.UserService;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class UsersController extends BaseController<UsersDTO, UserService> {

    private final UserService userService;

    @Override
    protected UserService getService() {
        return userService;
    }


    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody UsersDTO usersDTO) {
        return ResponseEntity.ok(userService.register(usersDTO));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
        return ResponseEntity.ok(userService.authenticate(authenticationRequest));
    }

}
