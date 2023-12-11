package pl.courses.online_courses_backend.mapper;

import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import pl.courses.online_courses_backend.entity.TopicEntity;
import pl.courses.online_courses_backend.mapper.decorator.TopicMapperDecorator;
import pl.courses.online_courses_backend.model.TopicDTO;

@DecoratedWith(TopicMapperDecorator.class)
@Mapper(componentModel = "spring", uses = NoteMapper.class)
public interface TopicMapper extends BaseMapper<TopicEntity, TopicDTO> {
}
