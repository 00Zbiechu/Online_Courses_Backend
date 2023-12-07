package pl.courses.online_courses_backend.validator;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import pl.courses.online_courses_backend.exception.CustomErrorException;
import pl.courses.online_courses_backend.exception.errors.ErrorCodes;
import pl.courses.online_courses_backend.model.TopicDTO;
import pl.courses.online_courses_backend.repository.CourseRepository;

@Component
@RequiredArgsConstructor
public class TopicValidator implements Validator {

    private final CourseRepository courseRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(TopicDTO.class);
    }

    @Override
    public void validate(@NotNull Object target, @NotNull Errors errors) {
        var dto = (TopicDTO) target;
        validateIsCourseExist(dto);
        validateIsTopicTitleDoesNotExistForCourse(dto);
    }

    private void validateIsCourseExist(TopicDTO dto) {
        courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new CustomErrorException("course", ErrorCodes.ENTITY_DOES_NOT_EXIST, HttpStatus.NOT_FOUND));
    }

    private void validateIsTopicTitleDoesNotExistForCourse(TopicDTO dto) {
        var courseEntity = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new CustomErrorException("course", ErrorCodes.ENTITY_DOES_NOT_EXIST, HttpStatus.NOT_FOUND));

        courseEntity.getTopics().forEach(topic -> {
            if (topic.getTitle().equals(dto.getTitle())) {
                throw new CustomErrorException("title", ErrorCodes.ENTITY_ALREADY_EXIST, HttpStatus.BAD_REQUEST);
            }
        });
    }
}
