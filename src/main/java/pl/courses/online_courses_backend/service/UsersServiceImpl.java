package pl.courses.online_courses_backend.service;

import com.google.common.collect.Sets;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.courses.online_courses_backend.authentication.CurrentUser;
import pl.courses.online_courses_backend.authentication.Role;
import pl.courses.online_courses_backend.authentication.TokenType;
import pl.courses.online_courses_backend.entity.TokenEntity;
import pl.courses.online_courses_backend.entity.UserEntity;
import pl.courses.online_courses_backend.exception.CustomErrorException;
import pl.courses.online_courses_backend.exception.errors.ErrorCodes;
import pl.courses.online_courses_backend.mapper.BaseMapper;
import pl.courses.online_courses_backend.mapper.UserMapper;
import pl.courses.online_courses_backend.model.*;
import pl.courses.online_courses_backend.photo.PhotoCompressor;
import pl.courses.online_courses_backend.photo.PhotoDTO;
import pl.courses.online_courses_backend.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UsersServiceImpl extends AbstractService<UserEntity, UserDTO> implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final PhotoCompressor photoCompressor;

    private final CurrentUser currentUser;

    private final EmailService emailService;

    @Override
    protected JpaRepository<UserEntity, Long> getRepository() {
        return userRepository;
    }

    @Override
    protected BaseMapper<UserEntity, UserDTO> getMapper() {
        return userMapper;
    }

    @Override
    public AuthenticationResponseDTO register(UserDTO userDTO) {

        UserEntity user = userMapper.toEntity(userDTO);
        user.setRole(Role.USER);

        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserWithToken(user, jwtToken);

        emailService.sendMail(
                UsernameAndMailDTO.builder()
                        .username(userDTO.getUsername())
                        .mail(userDTO.getEmail())
                        .build()
        );

        return AuthenticationResponseDTO.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO authenticationRequestDTO) {

        var user = userRepository
                .findByUsername(authenticationRequestDTO.getUsername())
                .orElseThrow(() -> new CustomErrorException("username", ErrorCodes.ENTITY_DOES_NOT_EXIST, HttpStatus.NOT_FOUND));

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequestDTO.getUsername(),
                        authenticationRequestDTO.getPassword()
                )
        );

        var jwtToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserWithToken(user, jwtToken);
        var refreshToken = jwtService.generateRefreshToken(user);

        return AuthenticationResponseDTO.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public AuthenticationResponseDTO refreshToken(RefreshTokenDTO refreshTokenDTO) {

        var username = jwtService.extractUsername(refreshTokenDTO.getRefreshToken());

        if (username == null) {
            throw new CustomErrorException("username", ErrorCodes.ENTITY_DOES_NOT_EXIST, HttpStatus.NOT_FOUND);
        }

        var userDetails = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomErrorException("username", ErrorCodes.ENTITY_DOES_NOT_EXIST, HttpStatus.NOT_FOUND));

        if (!jwtService.isTokenValid(refreshTokenDTO.getRefreshToken(), userDetails)) {
            throw new CustomErrorException("refreshToken", ErrorCodes.TOKEN_ERROR, HttpStatus.BAD_REQUEST);
        }

        var newAccessToken = jwtService.generateToken(userDetails);
        var newRefreshToken = jwtService.generateRefreshToken(userDetails);

        revokeAllUserTokens(userDetails);
        saveUserWithToken(userDetails, newAccessToken);

        return AuthenticationResponseDTO.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    @Override
    public PhotoDTO getUserImage() {
        return PhotoDTO.builder().photo(currentUser.getCurrentlyLoggedUser().getPhoto()).build();
    }

    //TODO: Make generic method for course and user
    public PhotoDTO uploadUserImage(MultipartFile photo) {

        Try.run(() -> {
            var compressedPhoto = photoCompressor.resizeImage(photo, 400, 400);
            currentUser.getCurrentlyLoggedUser().setPhoto(compressedPhoto);
            userRepository.save(currentUser.getCurrentlyLoggedUser());
        }).onFailure(image -> {
            throw new CustomErrorException("photo", ErrorCodes.WRONG_FIELD_FORMAT, HttpStatus.BAD_REQUEST);
        });
        return PhotoDTO.builder().photo(currentUser.getCurrentlyLoggedUser().getPhoto()).build();
    }

    private void saveUserWithToken(UserEntity user, String jwtToken) {
        var token = TokenEntity.builder()
                .userEntity(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .build();

        if (user.getTokens() == null) {
            user.setTokens(Sets.newHashSet(token));
        } else {
            user.getTokens().add(token);
        }

        userRepository.save(user);
    }

    private void revokeAllUserTokens(UserEntity userEntity) {
        userEntity.getTokens().clear();
        userRepository.save(userEntity);
    }
}
