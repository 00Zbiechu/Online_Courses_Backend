package pl.courses.online_courses_backend.exception.wrapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.courses.online_courses_backend.exception.model.ErrorDTO;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorList {

    private List<ErrorDTO> errorList;
}
