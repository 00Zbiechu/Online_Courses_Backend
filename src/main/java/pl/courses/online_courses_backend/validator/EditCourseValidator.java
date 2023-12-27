package pl.courses.online_courses_backend.validator;

import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import pl.courses.online_courses_backend.entity.CourseEntity;
import pl.courses.online_courses_backend.exception.CustomErrorException;
import pl.courses.online_courses_backend.exception.errors.ErrorCodes;
import pl.courses.online_courses_backend.model.EditCourseDTO;
import pl.courses.online_courses_backend.repository.CourseRepository;

import java.util.Optional;

@Component
public class EditCourseValidator extends AddCourseValidator implements Validator {

    public EditCourseValidator(CourseRepository courseRepository) {
        super(courseRepository);
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(EditCourseDTO.class);
    }

    @Override
    public void validate(@NotNull Object target, @NotNull Errors errors) {
        var dto = (EditCourseDTO) target;
        validateIsCourseTitleIsUnique(dto);
        validateIsStartDateNotNull(dto);
        validateIsEndDateNotNull(dto);
        validateIsStartAndEndDateAreCurrentOrFuture(dto);
        validateIsStartDateIsBeforeEndDate(dto);
        validatePasswordSizeIfIsSet(dto);
    }

    private void validateIsCourseTitleIsUnique(EditCourseDTO dto) {
        Optional<CourseEntity> courseEntity = courseRepository.findByTitleAndDeletedFalse(dto.getTitle());
        if (courseEntity.isPresent() && !(courseEntity.get().getId().equals(dto.getId()))) {
            throw new CustomErrorException("title", ErrorCodes.ENTITY_ALREADY_EXIST, HttpStatus.BAD_REQUEST);
        }
    }
}
