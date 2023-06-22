package pl.courses.online_courses_backend.service;

public interface EmailSenderService {

    void sendEmail(String toEmail, String subject, String body);

}
