package pl.courses.online_courses_backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import pl.courses.online_courses_backend.model.CourseConfirmationDTO;
import pl.courses.online_courses_backend.model.UsernameAndMailDTO;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    @Value("${online-courses.kafka.topics.registration.email.name}")
    private String registrationTopic;

    @Value("${online-courses.kafka.topics.participation.email.name}")
    private String participationTopic;

    private final KafkaTemplate<String, UsernameAndMailDTO> registrationTemplate;

    private final KafkaTemplate<String, CourseConfirmationDTO> courseConfirmationTemplate;

    @Override
    public void registrationMail(UsernameAndMailDTO event) {
        registrationTemplate.send(registrationTopic, event);
    }

    @Override
    public void participantConfirmation(CourseConfirmationDTO event) {
        courseConfirmationTemplate.send(participationTopic, event);
    }
}
