package pl.courses.online_courses_backend.mapper;

import org.mapstruct.Mapper;
import pl.courses.online_courses_backend.entity.CoursesEntity;
import pl.courses.online_courses_backend.model.CoursesDTO;

@Mapper(uses = CoursesMapper.class, componentModel = "spring")
public interface CoursesMapper extends BaseMapper<CoursesEntity, CoursesDTO> {


}
