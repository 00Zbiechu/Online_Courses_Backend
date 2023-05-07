package pl.courses.online_courses_backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.courses.online_courses_backend.authentication.AuthenticationRequest;
import pl.courses.online_courses_backend.authentication.AuthenticationResponse;
import pl.courses.online_courses_backend.authentication.JwtService;
import pl.courses.online_courses_backend.authentication.Role;
import pl.courses.online_courses_backend.entity.UsersEntity;
import pl.courses.online_courses_backend.mapper.BaseMapper;
import pl.courses.online_courses_backend.mapper.UsersMapper;
import pl.courses.online_courses_backend.model.UsersDTO;
import pl.courses.online_courses_backend.repository.UsersRepository;

@Service
@RequiredArgsConstructor
public class UsersService extends AbstractService<UsersEntity, UsersDTO> {

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


    public AuthenticationResponse register(UsersDTO usersDTO) {
        usersDTO.setPassword(passwordEncoder.encode(usersDTO.getPassword()));
        usersDTO.setRole(Role.USER);
        UsersEntity user = usersMapper.toEntity(usersDTO);
        usersRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()
                )
        );
        var user = usersRepository.findByUsername(authenticationRequest.getUsername())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken).build();

    }
}
