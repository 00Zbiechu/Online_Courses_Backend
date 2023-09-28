package pl.courses.online_courses_backend.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import static pl.courses.online_courses_backend.exception.errors.ErrorCodes.FIELD_REQUIRED;
import static pl.courses.online_courses_backend.exception.errors.ErrorCodes.WRONG_FIELD_SIZE;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddCourseDTO {

    @NotBlank(message = FIELD_REQUIRED)
    @Size(min = 3, max = 30, message = WRONG_FIELD_SIZE)
    private String title;

    private LocalDate startDate;

    private LocalDate endDate;

    @NotBlank(message = FIELD_REQUIRED)
    @Size(min = 10, max = 30, message = WRONG_FIELD_SIZE)
    private String topic;

    @NotBlank(message = FIELD_REQUIRED)
    @Size(min = 6, max = 30, message = WRONG_FIELD_SIZE)
    private String password;

    @NotBlank(message = FIELD_REQUIRED)
    @Size(min = 10, max = 50, message = WRONG_FIELD_SIZE)
    private String description;
}
