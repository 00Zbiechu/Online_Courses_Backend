package pl.courses.online_courses_backend.exception;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorsDTO {

    private List<ErrorDTO> errorList;

}
