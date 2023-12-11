package pl.courses.online_courses_backend.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

import static pl.courses.online_courses_backend.exception.errors.ErrorCodes.FIELD_REQUIRED;
import static pl.courses.online_courses_backend.exception.errors.ErrorCodes.WRONG_FIELD_SIZE;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TopicDTO {

    @NotBlank(message = FIELD_REQUIRED)
    @Size(min = 5, max = 50, message = WRONG_FIELD_SIZE)
    private String title;

    private Set<NoteDTO> notes;

    /*
    private Set<FileDTO> files;
     */
}
