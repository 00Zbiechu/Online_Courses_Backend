package pl.courses.online_courses_backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.courses.online_courses_backend.model.AuthenticationRequestDTO;
import pl.courses.online_courses_backend.model.AuthenticationResponseDTO;
import pl.courses.online_courses_backend.authentication.JwtService;
import pl.courses.online_courses_backend.authentication.Role;
import pl.courses.online_courses_backend.entity.UsersEntity;
import pl.courses.online_courses_backend.mapper.BaseMapper;
import pl.courses.online_courses_backend.mapper.UsersMapper;
import pl.courses.online_courses_backend.model.UsersDTO;
import pl.courses.online_courses_backend.repository.UsersRepository;

@Service
@RequiredArgsConstructor
public class UsersServiceImpl extends AbstractService<UsersEntity, UsersDTO> implements UserService {

    private final UsersRepository usersRepository;

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

    @Override
    public AuthenticationResponseDTO register(UsersDTO usersDTO) {
        usersDTO.setPassword(passwordEncoder.encode(usersDTO.getPassword()));
        usersDTO.setRole(Role.USER);
        UsersEntity user = usersMapper.toEntity(usersDTO);
        usersRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponseDTO.builder()
                .token(jwtToken).build();
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
        return AuthenticationResponseDTO.builder()
                .token(jwtToken).build();

    }
}
