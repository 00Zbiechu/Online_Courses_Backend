package pl.courses.online_courses_backend.mapper;

import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import pl.courses.online_courses_backend.entity.CourseEntity;
import pl.courses.online_courses_backend.mapper.decorator.CourseMapperDecorator;
import pl.courses.online_courses_backend.model.AddCourseDTO;
import pl.courses.online_courses_backend.model.CourseDTO;
import pl.courses.online_courses_backend.model.CourseWithAuthorDTO;
import pl.courses.online_courses_backend.model.EditCourseDTO;

@DecoratedWith(CourseMapperDecorator.class)
@Mapper(componentModel = "spring")
public interface CourseMapper extends BaseMapper<CourseEntity, CourseDTO> {

    void updateCourseEntityFromEditCourseDTO(EditCourseDTO editCourseDTO, @MappingTarget CourseEntity courseEntity);

    CourseEntity toEntity(AddCourseDTO addCourseDTO);

    CourseWithAuthorDTO toCourseForList(CourseEntity courseEntity);
}
