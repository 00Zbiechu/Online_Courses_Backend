package pl.courses.online_courses_backend.mapper;

import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import pl.courses.online_courses_backend.entity.CourseEntity;
import pl.courses.online_courses_backend.mapper.decorator.CourseMapperDecorator;
import pl.courses.online_courses_backend.model.AddCourseDTO;
import pl.courses.online_courses_backend.model.CourseDTO;
import pl.courses.online_courses_backend.model.CourseForListDTO;
import pl.courses.online_courses_backend.model.CourseForUserDTO;

@DecoratedWith(CourseMapperDecorator.class)
@Mapper(componentModel = "spring")
public interface CourseMapper extends BaseMapper<CourseEntity, CourseDTO> {

    CourseEntity toEntity(AddCourseDTO addCourseDTO);

    CourseForListDTO toCourseForList(CourseEntity courseEntity);

    CourseForUserDTO toCourseForAdmin(CourseEntity courseEntity);
}
