package pl.courses.online_courses_backend.service;

import pl.courses.online_courses_backend.event.UsernameAndMailDTO;

public interface EmailService {

    void sendMail(UsernameAndMailDTO event);
}
