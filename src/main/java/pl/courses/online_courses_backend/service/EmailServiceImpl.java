package pl.courses.online_courses_backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import pl.courses.online_courses_backend.event.UserAndMailDTO;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    @Value("${online-courses.kafka.topics.email.name}")
    private String topic;

    private final KafkaTemplate<String, UserAndMailDTO> kafkaTemplate;

    @Override
    public void sendMail(UserAndMailDTO event) {
        kafkaTemplate.send(topic, event);
    }
}
