package pl.courses.online_courses_backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import pl.courses.online_courses_backend.entity.UsersEntity;
import pl.courses.online_courses_backend.mapper.BaseMapper;
import pl.courses.online_courses_backend.mapper.UsersMapper;
import pl.courses.online_courses_backend.model.UsersDTO;
import pl.courses.online_courses_backend.repository.UsersRepository;

@Service
@RequiredArgsConstructor
public class UsersServiceImpl extends AbstractService<UsersEntity, UsersDTO> {

    private final UsersRepository usersRepository;

    private final UsersMapper usersMapper;

    @Override
    protected JpaRepository<UsersEntity, Long> getRepository() {
        return usersRepository;
    }

    @Override
    protected BaseMapper<UsersEntity, UsersDTO> getMapper() {
        return usersMapper;
    }
}
