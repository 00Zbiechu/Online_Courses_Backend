package pl.courses.online_courses_backend.service;

import pl.courses.online_courses_backend.event.UserAndMailDTO;

public interface EmailService {

    void sendMail(UserAndMailDTO event);
}
