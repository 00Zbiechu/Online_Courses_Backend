package pl.courses.online_courses_backend.validator;


import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import pl.courses.online_courses_backend.exception.CustomErrorException;
import pl.courses.online_courses_backend.exception.errors.ErrorCodes;
import pl.courses.online_courses_backend.model.AddCourseDTO;
import pl.courses.online_courses_backend.repository.CourseRepository;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class AddCourseValidator implements Validator {

    private final CourseRepository courseRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(AddCourseDTO.class);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        var dto = (AddCourseDTO) target;
        validateIsCourseTitleIsUnique(dto);
        validateIsStartDateNotNull(dto);
        validateIsEndDateNotNull(dto);
        validateIsStartAndEndDateAreCurrentOrFuture(dto);
        validateIsStartDateIsBeforeEndDate(dto);
    }

    private void validateIsCourseTitleIsUnique(AddCourseDTO dto) {
        courseRepository.findByTitle(dto.getTitle()).ifPresent(course -> {
            throw new CustomErrorException("title", ErrorCodes.ENTITY_ALREADY_EXIST, HttpStatus.BAD_REQUEST);
        });
    }

    private void validateIsEndDateNotNull(AddCourseDTO dto) {
        if (dto.getEndDate() == null) {
            throw new CustomErrorException("endDate", ErrorCodes.FIELD_REQUIRED, HttpStatus.BAD_REQUEST);
        }
    }

    private void validateIsStartDateNotNull(AddCourseDTO dto) {
        if (dto.getStartDate() == null) {
            throw new CustomErrorException("startDate", ErrorCodes.FIELD_REQUIRED, HttpStatus.BAD_REQUEST);
        }
    }

    private void validateIsStartAndEndDateAreCurrentOrFuture(AddCourseDTO dto) {
        if (dto.getStartDate().isAfter(dto.getEndDate())) {
            throw new CustomErrorException("endDate", ErrorCodes.WRONG_DATE_RANGE, HttpStatus.BAD_REQUEST);
        }
    }

    private void validateIsStartDateIsBeforeEndDate(AddCourseDTO dto) {
        if (dto.getStartDate().isBefore(LocalDate.now())) {
            throw new CustomErrorException("startDate", ErrorCodes.WRONG_DATE_RANGE, HttpStatus.BAD_REQUEST);
        } else if (dto.getEndDate().isBefore(LocalDate.now())) {
            throw new CustomErrorException("endDate", ErrorCodes.WRONG_DATE_RANGE, HttpStatus.BAD_REQUEST);
        }
    }
}
