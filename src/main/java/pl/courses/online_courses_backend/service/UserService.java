package pl.courses.online_courses_backend.service;

import pl.courses.online_courses_backend.authentication.AuthenticationRequest;
import pl.courses.online_courses_backend.authentication.AuthenticationResponse;
import pl.courses.online_courses_backend.model.UsersDTO;

public interface UserService extends BaseService<UsersDTO> {

    AuthenticationResponse register(UsersDTO usersDTO);

    AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest);

}
