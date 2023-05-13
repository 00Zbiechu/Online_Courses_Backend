package pl.courses.online_courses_backend.service;

import pl.courses.online_courses_backend.model.AuthenticationRequestDTO;
import pl.courses.online_courses_backend.model.AuthenticationResponseDTO;
import pl.courses.online_courses_backend.model.UsersDTO;

public interface UserService extends BaseService<UsersDTO> {

    AuthenticationResponseDTO register(UsersDTO usersDTO);

    AuthenticationResponseDTO authenticate(AuthenticationRequestDTO authenticationRequestDTO);

}
