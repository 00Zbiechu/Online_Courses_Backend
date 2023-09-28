package pl.courses.online_courses_backend.mapper;

import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import pl.courses.online_courses_backend.entity.UserEntity;
import pl.courses.online_courses_backend.mapper.decorator.UserMapperDecorator;
import pl.courses.online_courses_backend.model.UserDTO;

@DecoratedWith(UserMapperDecorator.class)
@Mapper(componentModel = "spring")
public interface UserMapper extends BaseMapper<UserEntity, UserDTO> {
}
