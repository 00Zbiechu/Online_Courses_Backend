package pl.courses.online_courses_backend.controller;

import io.vavr.control.Try;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pl.courses.online_courses_backend.exception.CustomErrorException;
import pl.courses.online_courses_backend.exception.errors.ErrorCodes;
import pl.courses.online_courses_backend.model.AuthenticationRequestDTO;
import pl.courses.online_courses_backend.model.AuthenticationResponseDTO;
import pl.courses.online_courses_backend.model.RefreshTokenDTO;
import pl.courses.online_courses_backend.model.UserDTO;
import pl.courses.online_courses_backend.photo.PhotoDTO;
import pl.courses.online_courses_backend.service.UserService;
import pl.courses.online_courses_backend.validator.UserValidator;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class UserController extends BaseController<UserDTO, UserService> {

    private final UserService userService;

    private final UserValidator userValidator;

    @InitBinder("userDTO")
    public void addValidationForUserDTO(WebDataBinder binder) {
        binder.addValidators(userValidator);
    }

    @Override
    protected UserService getService() {
        return userService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody UserDTO userDTO) {
        userService.register(userDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponseDTO> authenticate(@Valid @RequestBody AuthenticationRequestDTO authenticationRequestDTO) {
        return ResponseEntity.ok(userService.authenticate(authenticationRequestDTO));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponseDTO> refreshToken(@Valid @RequestBody RefreshTokenDTO refreshTokenDTO) {
        return ResponseEntity.ok(userService.refreshToken(refreshTokenDTO));
    }

    //TODO: unresolved Exception - probably same situation as jwt expiration exception
    @PostMapping("/logout")
    public void logout(HttpServletRequest request) {
        Try.run(request::logout)
                .onFailure(e -> {
                    throw new CustomErrorException("token", ErrorCodes.ENTITY_DOES_NOT_EXIST, HttpStatus.NOT_FOUND);
                });
    }

    @GetMapping(value = "/get-photo")
    public ResponseEntity<PhotoDTO> getCourseImage() {
        return new ResponseEntity<>(userService.getUserImage(), HttpStatus.OK);
    }

    @PostMapping(value = "/upload-photo")
    public ResponseEntity<PhotoDTO> uploadUserImage(@RequestParam("photo") MultipartFile photo) {
        return new ResponseEntity<>(userService.uploadUserImage(photo), HttpStatus.OK);
    }
}
