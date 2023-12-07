package pl.courses.online_courses_backend.mapper;

import org.mapstruct.Mapper;
import pl.courses.online_courses_backend.entity.TopicEntity;
import pl.courses.online_courses_backend.model.TopicDTO;

@Mapper(componentModel = "spring", uses = {
        NoteMapper.class,
        FileMapper.class
})
public interface TopicMapper extends BaseMapper<TopicEntity, TopicDTO> {
}
