package pl.courses.online_courses_backend.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.courses.online_courses_backend.model.AuthenticationRequestDTO;
import pl.courses.online_courses_backend.model.AuthenticationResponseDTO;
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
    public ResponseEntity<AuthenticationResponseDTO> register(@Valid @RequestBody UsersDTO usersDTO) {
        return ResponseEntity.ok(userService.register(usersDTO));
    }


    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponseDTO> authenticate(@Valid @RequestBody AuthenticationRequestDTO authenticationRequestDTO) {
        return ResponseEntity.ok(userService.authenticate(authenticationRequestDTO));
    }


    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponseDTO> refreshToken(@RequestBody AuthenticationResponseDTO authenticationResponseDTO) {
        return ResponseEntity.ok(userService.refreshToken(authenticationResponseDTO));
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request) throws ServletException {
        request.logout();
    }
}
