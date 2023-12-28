package pl.courses.online_courses_backend.service;

import pl.courses.online_courses_backend.event.CourseConfirmationDTO;
import pl.courses.online_courses_backend.event.UsernameAndMailDTO;

public interface EmailService {

    void registrationMail(UsernameAndMailDTO event);

    void participantConfirmation(CourseConfirmationDTO courseConfirmationDTO);
}
