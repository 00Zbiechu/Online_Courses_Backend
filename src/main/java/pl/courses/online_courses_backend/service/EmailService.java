package pl.courses.online_courses_backend.service;

import pl.courses.online_courses_backend.model.UsernameAndMailDTO;

public interface EmailService {

    void sendMail(UsernameAndMailDTO usernameAndMailDTO);
}
