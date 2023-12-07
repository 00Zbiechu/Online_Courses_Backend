package pl.courses.online_courses_backend.mapper;

import org.mapstruct.Mapper;
import pl.courses.online_courses_backend.entity.FileEntity;
import pl.courses.online_courses_backend.model.FileDTO;

@Mapper(componentModel = "spring")
public interface FileMapper extends BaseMapper<FileEntity, FileDTO> {
}
