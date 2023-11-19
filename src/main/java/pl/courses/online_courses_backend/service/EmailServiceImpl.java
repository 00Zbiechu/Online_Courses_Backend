package pl.courses.online_courses_backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import pl.courses.online_courses_backend.model.UsernameAndMailDTO;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final KafkaTemplate<String, UsernameAndMailDTO> kafkaTemplate;

    @Override
    public void sendMail(UsernameAndMailDTO usernameAndMailDTO) {
        kafkaTemplate.send("registration", usernameAndMailDTO);
    }
}
