package pl.courses.online_courses_backend.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import pl.courses.online_courses_backend.model.EditCourseDTO;

@Component
@RequiredArgsConstructor
public class EditCourseValidator implements Validator {

    private final AddCourseValidator addCourseValidator;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(EditCourseDTO.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        addCourseValidator.validate(target, errors);
    }
}
