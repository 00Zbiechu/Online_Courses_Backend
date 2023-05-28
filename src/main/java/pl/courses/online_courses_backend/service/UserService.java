package pl.courses.online_courses_backend.service;

import pl.courses.online_courses_backend.model.AuthenticationRequestDTO;
import pl.courses.online_courses_backend.model.AuthenticationResponseDTO;
import pl.courses.online_courses_backend.model.UsersDTO;
import pl.courses.online_courses_backend.projection.UsernameAndEmailForEachToken;

public interface UserService extends BaseService<UsersDTO> {

    AuthenticationResponseDTO register(UsersDTO usersDTO);

    AuthenticationResponseDTO authenticate(AuthenticationRequestDTO authenticationRequestDTO);

    AuthenticationResponseDTO refreshToken(AuthenticationResponseDTO authenticationResponseDTO);

    UsernameAndEmailForEachToken findUsersEntitiesByAccessToken(String accessToken);
}
