package pl.courses.online_courses_backend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import pl.courses.online_courses_backend.entity.CoursesEntity;
import pl.courses.online_courses_backend.model.AddCoursesDTO;
import pl.courses.online_courses_backend.model.CoursesDTO;
import pl.courses.online_courses_backend.specification.FoundCourse;

@Mapper(componentModel = "spring", uses = {CoursesMapper.class})
public interface CoursesMapper extends BaseMapper<CoursesEntity, CoursesDTO> {

    CoursesMapper INSTANCE = Mappers.getMapper(CoursesMapper.class);

    CoursesDTO toCoursesDTOFromAddCoursesDTO(AddCoursesDTO addCoursesDTO);

    FoundCourse toFoundCourse(CoursesEntity courses);

}
