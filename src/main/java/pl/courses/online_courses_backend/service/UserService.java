package pl.courses.online_courses_backend.service;

import pl.courses.online_courses_backend.model.AuthenticationRequestDTO;
import pl.courses.online_courses_backend.model.AuthenticationResponseDTO;
import pl.courses.online_courses_backend.model.UserDTO;

public interface UserService extends BaseService<UserDTO> {

    AuthenticationResponseDTO register(UserDTO userDTO);

    AuthenticationResponseDTO authenticate(AuthenticationRequestDTO authenticationRequestDTO);

    AuthenticationResponseDTO refreshToken(AuthenticationResponseDTO authenticationResponseDTO);
}
