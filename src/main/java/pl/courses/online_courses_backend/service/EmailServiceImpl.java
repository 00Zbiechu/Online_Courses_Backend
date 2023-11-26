package pl.courses.online_courses_backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import pl.courses.online_courses_backend.event.UsernameAndMailDTO;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    @Value("${online-courses.kafka.topics.email.name}")
    private String topic;

    private final KafkaTemplate<String, UsernameAndMailDTO> kafkaTemplate;

    @Override
    public void sendMail(UsernameAndMailDTO event) {
        kafkaTemplate.send(topic, event);
    }
}
