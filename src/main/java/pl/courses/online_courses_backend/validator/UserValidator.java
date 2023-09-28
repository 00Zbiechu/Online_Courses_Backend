package pl.courses.online_courses_backend.validator;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import pl.courses.online_courses_backend.exception.CustomErrorException;
import pl.courses.online_courses_backend.exception.errors.ErrorCodes;
import pl.courses.online_courses_backend.model.UserDTO;
import pl.courses.online_courses_backend.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class UserValidator implements Validator {

    private final UserRepository userRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(UserDTO.class);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        var dto = (UserDTO) target;
        validateIsUsernameUnique(dto);
        validateIsEmailUnique(dto);
    }

    private void validateIsUsernameUnique(UserDTO dto) {
        userRepository.findByUsername(dto.getUsername()).ifPresent(user -> {
            throw new CustomErrorException("username", ErrorCodes.ENTITY_ALREADY_EXIST, HttpStatus.BAD_REQUEST);
        });
    }

    private void validateIsEmailUnique(UserDTO dto) {
        userRepository.findByEmail(dto.getEmail()).ifPresent(userEntity -> {
            throw new CustomErrorException("email", ErrorCodes.ENTITY_ALREADY_EXIST, HttpStatus.BAD_REQUEST);
        });
    }
}
