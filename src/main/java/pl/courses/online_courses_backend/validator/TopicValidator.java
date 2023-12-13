package pl.courses.online_courses_backend.validator;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import pl.courses.online_courses_backend.entity.CourseEntity;
import pl.courses.online_courses_backend.exception.CustomErrorException;
import pl.courses.online_courses_backend.exception.errors.ErrorCodes;
import pl.courses.online_courses_backend.model.AddTopicDTO;

@Component
public class TopicValidator {

    public void validateIsTopicUnique(AddTopicDTO addTopicDTO, CourseEntity courseEntity) {
        if (courseEntity.getTopics() != null) {
            courseEntity.getTopics().forEach(topic -> {
                if (topic.getTitle().equals(addTopicDTO.getTitle())) {
                    throw new CustomErrorException("topic", ErrorCodes.ENTITY_ALREADY_EXIST, HttpStatus.BAD_REQUEST);
                }
            });
        }
    }
}
