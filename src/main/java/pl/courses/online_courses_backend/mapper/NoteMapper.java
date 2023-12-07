package pl.courses.online_courses_backend.mapper;

import org.mapstruct.Mapper;
import pl.courses.online_courses_backend.entity.NoteEntity;
import pl.courses.online_courses_backend.model.NoteDTO;

@Mapper(componentModel = "spring")
public interface NoteMapper extends BaseMapper<NoteEntity, NoteDTO> {
}
