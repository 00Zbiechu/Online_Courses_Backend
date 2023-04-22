package pl.courses.online_courses_backend.mapper;

import org.mapstruct.Mapper;
import pl.courses.online_courses_backend.entity.UsersEntity;
import pl.courses.online_courses_backend.model.UsersDTO;

@Mapper(uses = UsersMapper.class, componentModel = "spring")
public interface UsersMapper extends BaseMapper<UsersEntity, UsersDTO> {
}
