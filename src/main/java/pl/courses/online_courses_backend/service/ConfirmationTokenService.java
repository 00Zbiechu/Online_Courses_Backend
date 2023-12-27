package pl.courses.online_courses_backend.service;

public interface ConfirmationTokenService {

    String confirmAccount(String token);
}
