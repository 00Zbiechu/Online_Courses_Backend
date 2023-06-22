package pl.courses.online_courses_backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
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

@Service
@RequiredArgsConstructor
public class UsersServiceImpl extends AbstractService<UsersEntity, UsersDTO> implements UserService {

    private final UsersRepository usersRepository;

    private final TokenRepository tokenRepository;

    private final UsersMapper usersMapper;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final EmailSenderService emailSenderService;

    @Override
    protected JpaRepository<UsersEntity, Long> getRepository() {
        return usersRepository;
    }

    @Override
    protected BaseMapper<UsersEntity, UsersDTO> getMapper() {
        return usersMapper;
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

        emailSenderService.sendEmail(user.getEmail(), "Registration", "Registration was successful");

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
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        var refreshToken = jwtService.generateRefreshToken(user);

        return AuthenticationResponseDTO.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();

    }


    @Override
    public AuthenticationResponseDTO refreshToken(AuthenticationResponseDTO authenticationResponseDTO) {

        final String username;

        username = jwtService.extractUsername(authenticationResponseDTO.getRefreshToken());


        if (username != null) {

            var userDetails = this.usersRepository.findByUsername(username).orElseThrow();


            var newAccessToken = jwtService.generateToken(userDetails);
            var newRefreshToken = jwtService.generateRefreshToken(userDetails);

            revokeAllUserTokens(userDetails);
            saveUserToken(userDetails, newAccessToken);

            return AuthenticationResponseDTO.builder()
                    .accessToken(newAccessToken)
                    .refreshToken(newRefreshToken)
                    .build();


        }

        return null;
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


    private void revokeAllUserTokens(UsersEntity usersEntity) {
        var validToken = tokenRepository.findAllValidTokensByUser(usersEntity.getId());
        if (validToken.isEmpty()) {
            return;
        }
        validToken.forEach(t -> {
            t.setExpired(true);
            t.setRevoked(true);
        });
        tokenRepository.saveAll(validToken);


    }
}
