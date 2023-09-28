package pl.courses.online_courses_backend.mapper.decorator;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.courses.online_courses_backend.entity.UserEntity;
import pl.courses.online_courses_backend.mapper.UserMapper;
import pl.courses.online_courses_backend.model.UserDTO;

@NoArgsConstructor
public abstract class UserMapperDecorator implements UserMapper {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserEntity toEntity(UserDTO dto) {
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        return userMapper.toEntity(dto);
    }
}
