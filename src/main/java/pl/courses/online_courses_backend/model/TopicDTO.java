package pl.courses.online_courses_backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TopicDTO {

    private Long id;

    private String title;

    private Set<NoteDTO> notes;

    private Set<FileDTO> files;
}
