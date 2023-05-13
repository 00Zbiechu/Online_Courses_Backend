package pl.courses.online_courses_backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.courses.online_courses_backend.authentication.JwtService;
import pl.courses.online_courses_backend.authentication.Role;
import pl.courses.online_courses_backend.authentication.TokenType;
import pl.courses.online_courses_backend.entity.TokenEntity;
import pl.courses.online_courses_backend.entity.UsersEntity;
import pl.courses.online_courses_backend.mapper.BaseMapper;
import pl.courses.online_courses_backend.mapper.UsersMapper;
import pl.courses.online_courses_backend.model.AuthenticationRequestDTO;
import pl.courses.online_courses_backend.model.AuthenticationResponseDTO;
import pl.courses.online_courses_backend.model.UsersDTO;
import pl.courses.online_courses_backend.repository.TokenRepository;
import pl.courses.online_courses_backend.repository.UsersRepository;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class UsersServiceImpl extends AbstractService<UsersEntity, UsersDTO> implements UserService {

    private final UsersRepository usersRepository;

    private final TokenRepository tokenRepository;

    private final UsersMapper usersMapper;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    @Override
    protected JpaRepository<UsersEntity, Long> getRepository() {
        return usersRepository;
    }

    @Override
    protected BaseMapper<UsersEntity, UsersDTO> getMapper() {
        return usersMapper;
    }


    private void saveUserToken(UsersEntity user, String jwtToken) {
        var token = TokenEntity.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .build();

        tokenRepository.save(token);
    }

    @Override
    public AuthenticationResponseDTO register(UsersDTO usersDTO) {
        usersDTO.setPassword(passwordEncoder.encode(usersDTO.getPassword()));
        usersDTO.setRole(Role.USER);
        UsersEntity user = usersMapper.toEntity(usersDTO);

        var savedUser = usersRepository.save(user);
        var jwtToken = jwtService.generateToken(user);

        var refreshToken = jwtService.generateRefreshToken(user);

        saveUserToken(savedUser, jwtToken);

        return AuthenticationResponseDTO.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }


    @Override
    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO authenticationRequestDTO) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequestDTO.getUsername(),
                        authenticationRequestDTO.getPassword()
                )
        );
        var user = usersRepository.findByUsername(authenticationRequestDTO.getUsername())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        saveUserToken(user, jwtToken);
        var refreshToken = jwtService.generateRefreshToken(user);

        return AuthenticationResponseDTO.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();

    }

    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader("Authorization");
        final String refreshToken;
        final String username;


        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }


        refreshToken = authHeader.substring(7);
        username = jwtService.extractUsername(refreshToken);


        if (username != null) {

            var userDetails = this.usersRepository.findByUsername(username).orElseThrow();

            if (jwtService.isTokenValid(refreshToken, userDetails)) {

                var accessToken = jwtService.generateToken(userDetails);
                var authResponse = AuthenticationResponseDTO.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();

                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);

            }

        }
    }
}
