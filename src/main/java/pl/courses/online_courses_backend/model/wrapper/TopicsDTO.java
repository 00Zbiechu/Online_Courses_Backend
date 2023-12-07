package pl.courses.online_courses_backend.model.wrapper;

import lombok.*;
import pl.courses.online_courses_backend.model.TopicDTO;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TopicsDTO {

    private List<TopicDTO> topics;
}
