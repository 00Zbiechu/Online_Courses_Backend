package pl.courses.online_courses_backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.courses.online_courses_backend.authentication.AuthenticationRequest;
import pl.courses.online_courses_backend.authentication.AuthenticationResponse;
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


    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody UsersDTO usersDTO) {
        return ResponseEntity.ok(usersService.register(usersDTO));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
        return ResponseEntity.ok(usersService.authenticate(authenticationRequest));
    }

}
