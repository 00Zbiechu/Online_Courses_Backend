package pl.courses.online_courses_backend.mapper.decorator;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import pl.courses.online_courses_backend.entity.NoteEntity;
import pl.courses.online_courses_backend.entity.TopicEntity;
import pl.courses.online_courses_backend.mapper.TopicMapper;
import pl.courses.online_courses_backend.model.AddTopicDTO;

import java.util.Optional;
import java.util.Set;


@NoArgsConstructor
public abstract class TopicMapperDecorator implements TopicMapper {

    @Autowired
    private TopicMapper topicMapper;

    @Override
    public TopicEntity toEntity(AddTopicDTO dto) {
        var topicEntity = topicMapper.toEntity(dto);
        topicEntity.setNotes(Set.of(NoteEntity.builder().data(dto.getNote()).build()));

        Optional.ofNullable(topicEntity.getNotes())
                .ifPresent(noteEntities -> noteEntities.forEach(noteEntity -> noteEntity.setTopicEntity(topicEntity)));

        return topicEntity;
    }
}
