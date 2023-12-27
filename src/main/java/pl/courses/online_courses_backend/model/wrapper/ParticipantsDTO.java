package pl.courses.online_courses_backend.model.wrapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.courses.online_courses_backend.model.ParticipantDTO;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ParticipantsDTO {

    private List<ParticipantDTO> participants;
}
