package pl.courses.online_courses_backend.exception.wrapper;

import lombok.*;
import pl.courses.online_courses_backend.exception.ErrorDTO;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorsDTO {

    private List<ErrorDTO> errorList;

}
