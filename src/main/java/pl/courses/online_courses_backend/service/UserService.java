package pl.courses.online_courses_backend.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pl.courses.online_courses_backend.model.AuthenticationRequestDTO;
import pl.courses.online_courses_backend.model.AuthenticationResponseDTO;
import pl.courses.online_courses_backend.model.UsersDTO;

import java.io.IOException;

public interface UserService extends BaseService<UsersDTO> {

    AuthenticationResponseDTO register(UsersDTO usersDTO);

    AuthenticationResponseDTO authenticate(AuthenticationRequestDTO authenticationRequestDTO);

    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
