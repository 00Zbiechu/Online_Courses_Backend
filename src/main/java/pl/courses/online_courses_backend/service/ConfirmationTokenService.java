package pl.courses.online_courses_backend.service;

public interface ConfirmationTokenService {

    void confirmAccount(String token);
}
