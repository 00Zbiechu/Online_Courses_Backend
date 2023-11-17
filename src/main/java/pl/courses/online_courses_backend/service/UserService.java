package pl.courses.online_courses_backend.service;

import org.springframework.web.multipart.MultipartFile;
import pl.courses.online_courses_backend.model.AuthenticationRequestDTO;
import pl.courses.online_courses_backend.model.AuthenticationResponseDTO;
import pl.courses.online_courses_backend.model.RefreshTokenDTO;
import pl.courses.online_courses_backend.model.UserDTO;
import pl.courses.online_courses_backend.photo.PhotoDTO;

public interface UserService extends BaseService<UserDTO> {

    AuthenticationResponseDTO register(UserDTO userDTO);

    AuthenticationResponseDTO authenticate(AuthenticationRequestDTO authenticationRequestDTO);

    AuthenticationResponseDTO refreshToken(RefreshTokenDTO refreshTokenDTO);

    PhotoDTO getUserImage();

    PhotoDTO uploadUserImage(MultipartFile photo);
}
